# Tài liệu Thiết kế Kỹ thuật: File Service

## 1. Tổng quan

### 1.1. Trách nhiệm

**File Service** là một microservice chuyên dụng để quản lý việc lưu trữ file, trừu tượng hóa sự phức tạp của việc tương tác với cloud storage.

*   **Chức năng chính:**
    *   Quản lý metadata của file.
    *   Tạo **Presigned URLs** an toàn cho việc upload và download.
    *   Cung cấp API nội bộ cho các service khác.
    *   Tách biệt hoàn toàn logic lưu trữ khỏi logic nghiệp vụ.

### 1.2. Công nghệ
*   **Framework:** Java & Spring Boot
*   **Cơ sở dữ liệu:** PostgreSQL
*   **Cloud Storage:** Tương thích với S3 API (Amazon S3, Cloudflare R2, MinIO...).

## 2. Thiết kế Entity (JPA Entities)

#### Bảng `file_metadata`

| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` | `BIGINT` (PK) | Khóa chính. |
| `uuid` | `VARCHAR(36)` | UUID công khai, duy nhất của file. |
| `file_path` | `VARCHAR(512)` | Đường dẫn đầy đủ trên storage (VD: `courses/uuid/thumbnail.jpg`). |
| `original_name` | `VARCHAR(255)` | Tên file gốc. |
| `content_type` | `VARCHAR(100)` | MIME type. |
| `file_size` | `BIGINT` | Kích thước file (bytes). |
| `status` | `VARCHAR(50)` | ENUM: `PENDING_UPLOAD`, `UPLOADED`, `FAILED`. |
| `owner_user_id` | `BIGINT` | ID của người dùng tải file lên. |
| `resource_context` | `JSONB` | Lưu ngữ cảnh, VD: `{"type": "COURSE_THUMBNAIL", "courseId": 123}`. |
| `created_at` | `TIMESTAMP` | |

## 3. Thiết kế API Endpoints (Nội bộ)

Các API này chỉ được gọi bởi các service khác trong hệ thống, được bảo vệ bằng Internal Secret Key.

*   `POST /api/v1/internal/files/initiate-upload`:
    *   **Request Body:** `FileUploadRequestDto` (chứa metadata file và `resourceContext`).
    *   **Response Body:** `FileUploadResponseDto` (chứa `uploadUrl` và `filePath`).

*   `POST /api/v1/internal/files/confirm-upload`:
    *   **Request Body:** `{ "filePath": "path/to/file.jpg" }`.
    *   **Response:** HTTP 200 OK.

*   `POST /api/v1/internal/files/generate-download-url`:
    *   **Request Body:** `{ "filePath": "path/to/file.jpg" }`.
    *   **Response Body:** `{ "downloadUrl": "presigned_download_url" }`.

## 4. Luồng hoạt động chi tiết

### Luồng 1: Khởi tạo Upload
1.  Một service khác (VD: `Course Service`) gọi `POST /api/v1/internal/files/initiate-upload`.
2.  **File Service:**
    a.  Tạo một đường dẫn file duy nhất dựa trên `resourceContext`.
    b.  Lưu một bản ghi `file_metadata` mới với trạng thái `PENDING_UPLOAD`.
    c.  Sử dụng AWS S3 SDK để tạo một Presigned URL cho phương thức `PUT`.
    d.  Trả về `uploadUrl` và `filePath`.

### Luồng 2: Xác nhận Upload
1.  Sau khi client upload file thành công, service gọi (VD: `Course Service`) sẽ gọi `POST /api/v1/internal/files/confirm-upload`.
2.  **File Service:**
    a.  Tìm bản ghi `file_metadata` dựa trên `filePath`.
    b.  Cập nhật trạng thái thành `UPLOADED`.
    c.  (Tùy chọn) Có thể phát ra một sự kiện Kafka `file.uploaded` để các service khác có thể lắng nghe và xử lý (ví dụ: nén ảnh, quét virus).