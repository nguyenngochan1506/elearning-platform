# Kế hoạch Xây dựng Hệ thống eLearning

---

## Giai đoạn 1: Xây dựng Nền tảng Quản lý Khóa học (Core Learning Platform)

> Đây là trái tim của hệ thống eLearning.  
> Mục tiêu: Cho phép tạo và quản lý nội dung học tập cơ bản.

### Microservice: `course-service`
- **Ngôn ngữ/Framework**: Spring Boot (đồng bộ với hệ thống hiện tại).
- **Cơ sở dữ liệu**: PostgreSQL (tương tự `auth-service`).
- **Trách nhiệm**: Quản lý mọi thứ liên quan đến khóa học, chương, và bài học.

### Thiết kế Database cho `course-service`
- **courses**: `id`, `uuid`, `org_id`, `name`, `description`, `thumbnail_url`, `is_public`, `created_at`, `updated_at`
- **chapters**: `id`, `uuid`, `course_id`, `name`, `order`
- **activities**: `id`, `uuid`, `chapter_id`, `name`, `content_type` *(enum: TEXT, VIDEO, PDF)*, `content_data (JSONB)`, `order`
- **resource_authors**: `id`, `resource_uuid (của khóa học)`, `user_id`, `authorship_role` *(enum: CREATOR, CONTRIBUTOR)*

### API trong `course-service`
- CRUD cho **Courses**, **Chapters**, **Activities**
- API để **sắp xếp thứ tự**:
  - Chapters trong một Course
  - Activities trong một Chapter

### Tích hợp vào `gateway-service`
- Routing: `/api/v1/courses/**`, `/api/v1/chapters/**` → trỏ đến `course-service`
- Áp dụng **AuthenticationFilter** và **AuthorizationFilter**
- Định nghĩa quyền mới trong `permissions.json` của `auth-service`:
  - `course:create`
  - `course:update:own`
  - `activity:read`

---

## Giai đoạn 2: Nâng cao Nội dung và Tương tác (Enriching Content & Interaction)

### Microservice: `content-service` *(tùy chọn, khuyến khích)*
- **Trách nhiệm**: Xử lý upload, lưu trữ và phân phối file media (video, PDF, ảnh).
- **Lưu trữ**: bắt đầu local filesystem → nâng cấp lên Amazon S3 / Google Cloud Storage.

### Tính năng Upload
- API trong `content-service` để upload file → trả về **URL/ID** file.
- `course-service`: khi tạo/cập nhật Activity loại VIDEO/PDF, gọi `content-service` để upload → lưu URL/ID.

### Hệ thống Bài tập lớn (Assignments)
- Mở rộng `course-service` hoặc tạo `assignment-service`.
- **Database**: `assignments`, `assignment_tasks`, `assignment_submissions`.

**API:**
- Giảng viên:
  - Tạo bài tập
  - Thêm câu hỏi/nhiệm vụ
- Học viên:
  - Xem bài tập
  - Nộp bài (upload qua `content-service`)
- Giảng viên:
  - Xem bài nộp
  - Chấm điểm

### Hệ thống Theo dõi Tiến độ (`progress-service`)
- **Microservice**: `progress-service`
- **Database**: PostgreSQL hoặc NoSQL (MongoDB)
- **Logic**: lắng nghe sự kiện Kafka (`activity_completed_event`)
- **API**: Frontend có thể lấy tiến độ học tập người dùng

---

## Giai đoạn 3: Quản lý Nâng cao và Cộng đồng (Advanced Management & Community)

### Multi-tenancy (Tổ chức)
- **Microservice**: `organization-service`
- **Database**: PostgreSQL

**Database:**
- `organizations`: `id`, `uuid`, `name`, `slug`, `logo_url`, ...
- `organization_configs`: `id`, `org_id`, `config_data (JSONB)`

**Mở rộng `auth-service`:**
- `user_roles`: thêm cột `organization_id` → phân quyền theo tổ chức

### Cộng tác viên
- Hoàn thiện logic bảng `resource_authors` trong `course-service`
- API cho phép:
  - Người dùng đăng ký làm cộng tác viên
  - Chủ sở hữu khóa học duyệt/từ chối

### Hệ thống mời thành viên
- `organization-service`: API tạo mã mời
- Lưu mã mời: Redis hoặc Database
- `auth-service`: khi đăng ký với mã mời → tự động thêm vào tổ chức tương ứng

---

## Giai đoạn 4: Thương mại hóa và Tính năng AI (Monetization & AI Features)

### Microservice: `payment-service`
- **Tích hợp**: Stripe (thư viện Stripe cho Java)
- **Database**: `products`, `subscriptions`, `course_products`, `user_payments`

**API:**
- Quản lý sản phẩm
- Tạo phiên thanh toán (checkout session)
- Webhook lắng nghe sự kiện Stripe (thanh toán thành công, hủy đăng ký)

**Tích hợp Gateway:**
- `gateway-service` hoặc `course-service` gọi `payment-service` → kiểm tra quyền truy cập khóa học trả phí

### Microservice: `ai-service`
- **Ngôn ngữ**: Python (FastAPI/Flask)
- **Tích hợp LLM**: OpenAI, Anthropic, hoặc open-source models
- **Vector Database**: Pinecone, Weaviate hoặc ChromaDB

**Xử lý bất đồng bộ:**
- Khi Course được tạo/cập nhật → `course-service` gửi event qua Kafka
- `ai-service` lắng nghe, xử lý nội dung:
  - Chia nhỏ
  - Tạo embedding
  - Lưu vào vector DB
