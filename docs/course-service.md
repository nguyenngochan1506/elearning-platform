# Tài liệu Thiết kế Kỹ thuật: Course Service

## 1. Tổng quan

### 1.1. Trách nhiệm

**Course Service** là microservice trung tâm, chịu trách nhiệm quản lý toàn bộ vòng đời và nội dung của các khóa học.

*   **Chức năng chính:**
    *   Quản lý Khóa học (Course), Chương (Chapter), và Bài học (Activity).
    *   Xử lý cấu trúc và thứ tự của nội dung học tập.
    *   Quản lý quyền sở hữu và vai trò cộng tác viên (Contributors) trên từng khóa học.
    *   Theo dõi tiến độ học tập của người dùng.
    *   Giao tiếp với các service khác để kiểm tra quyền truy cập trả phí và yêu cầu upload file.

### 1.2. Công nghệ
*   **Framework:** Java & Spring Boot
*   **Cơ sở dữ liệu:** PostgreSQL
*   **Giao tiếp:** REST API (cho các yêu cầu đồng bộ) và Apache Kafka (cho các sự kiện bất đồng bộ).

## 2. Thiết kế Entity (JPA Entities)

#### Bảng `courses`
Lưu thông tin chính của khóa học.

| Tên Cột           | Kiểu Dữ liệu | Chú thích                                                                                |
|:------------------| :--- |:-----------------------------------------------------------------------------------------|
| `id`              | `BIGINT` (PK) | Khóa chính tự tăng, định danh nội bộ.                                                    |
| `uuid`            | `VARCHAR(36)` | UUID công khai, bất biến. Dùng cho giao tiếp liên service.                               |
| `slug`            | `VARCHAR(255)` | **Định danh công khai**, thân thiện với URL. **Unique trong phạm vi `organization_id`**. |
| `organization_id` | `BIGINT` | ID của tổ chức sở hữu khóa học.                                                          |
| `name`            | `VARCHAR(255)` | Tên khóa học.                                                                            |
| `description`     | `TEXT` | Mô tả ngắn gọn.                                                                          |
| `thumbail`        | `TEXT` | UUID của file ảnh bìa (tham chiếu đến File Service).                                     |
| `is_public`       | `BOOLEAN` | `true` nếu khóa học hiển thị công khai.                                                  |
| `is_deleted`      | `BOOLEAN` | default false.                                                                           |
| `created_at`      | `TIMESTAMP` | Thời gian tạo.                                                                           |
| `updated_at`      | `TIMESTAMP` | Thời gian cập nhật.                                                                      |

#### Bảng `chapters`
Các chương học.

| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` | `BIGINT` (PK) | Khóa chính. |
| `uuid` | `VARCHAR(36)` | UUID công khai. |
| `course_id` | `BIGINT` | Khóa ngoại đến `courses`. |
| `name` | `VARCHAR(255)` | Tên chương. |

#### Bảng `lessons`
Các bài học/hoạt động.

| Tên Cột       | Kiểu Dữ liệu | Chú thích |
|:--------------| :--- | :--- |
| `id`          | `BIGINT` (PK) | Khóa chính. |
| `uuid`        | `VARCHAR(36)` | UUID công khai. |
| `name`        | `VARCHAR(255)` | Tên bài học. |
| `lesson_type` | `VARCHAR(50)` | ENUM: `VIDEO`, `DOCUMENT`, `ASSIGNMENT`, `DYNAMIC_CONTENT`. |
| `content`     | `JSONB` | Lưu trữ dữ liệu nội dung. |


#### Bảng `resource_authors`
Quản lý quyền sở hữu và cộng tác.

| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` | `BIGINT` (PK) | Khóa chính. |
| `resource_uuid` | `VARCHAR(36)` | UUID của `courses`. |
| `user_id` | `BIGINT` | ID của người dùng. |
| `authorship_role` | `VARCHAR(50)` | ENUM: `CREATOR`, `MAINTAINER`, `CONTRIBUTOR`. |

## 3. Luồng hoạt động chính
### Tạo categories
- api: POST /api/v1/courses/categories


### 3.1. Luồng tạo Khóa học

1.  **Client** gửi `POST /api/v1/courses` với `name`, `description`,...
2.  **Course Service:**
    a.  **Authorization:** Kiểm tra người dùng có quyền `course:create`.
    b.  **Slug Generation:**
    *   Tạo `slug` từ `name` (VD: "Java For Beginners" -> "java-for-beginners").
    *   Kiểm tra xem `slug` này đã tồn tại trong cùng `organization_id` chưa.
    *   Nếu đã tồn tại, thêm hậu tố (VD: "java-for-beginners-1").
    c.  Tạo và lưu `CourseEntity` với `uuid` và `slug` vừa tạo.
    d.  Tạo bản ghi `ResourceAuthorEntity` gán người dùng hiện tại làm `CREATOR`.
    e.  Trả về thông tin khóa học, bao gồm cả `uuid` và `slug`.

### 3.2. Luồng truy cập Khóa học (Công khai)

1.  **Client** truy cập URL `.../courses/java-for-beginners`.
2.  **Gateway** định tuyến request đến `GET /api/v1/courses/by-slug/java-for-beginners` của **Course Service**.
3.  **Course Service:**
    a.  Tìm `CourseEntity` bằng `slug` và `organization_id` (lấy từ domain).
    b.  **Access Check:**
    *   Kiểm tra `is_public`. Nếu `true`, cho phép truy cập.
    *   Nếu `false`, gọi nội bộ đến **Payment Service** (`GET /api/v1/internal/access-check?userId=...&courseId=...`) để kiểm tra quyền truy cập trả phí.
    c.  Nếu có quyền, trả về toàn bộ cấu trúc khóa học. Nếu không, trả về lỗi 403 Forbidden.

