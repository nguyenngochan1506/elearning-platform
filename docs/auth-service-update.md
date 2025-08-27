# Tài liệu Thiết kế Kỹ thuật: Auth Service (Kiến trúc Đa Tổ chức)

## 1. Nguyên tắc thiết kế

### 1.1. Nguyên tắc chung
-   **Tách biệt Service:** Tầng `service` chỉ gọi `repository` của chính nó. Giao tiếp giữa các service phải thông qua API Gateway hoặc Message Queue.
-   **Bảo mật Mật khẩu:** Mật khẩu phải được hash bằng thuật toán mạnh (ví dụ: BCrypt) trước khi lưu vào CSDL.
-   **Cách ly Dữ liệu (QUAN TRỌNG NHẤT):** Mọi tài nguyên (người dùng, vai trò, quyền hạn) phải được cách ly nghiêm ngặt theo từng Tổ chức (`Organization`). Không một tổ chức nào được phép thấy dữ liệu của tổ chức khác.

### 1.2. Quy ước Đặt tên
-   **Class/Entity/DTO:** `PascalCase`, Số Ít (VD: `UserEntity`, `RoleCreateDto`).
-   **Table (CSDL):** `snake_case`, Số Nhiều (VD: `tbl_users`, `tbl_roles`).

## 2. Kiến trúc Phân quyền Đa Tổ chức (Multi-tenant RBAC)

Hệ thống RBAC động là một ý tưởng tuyệt vời. Chúng ta sẽ phát triển nó trên một nền tảng hỗ trợ đa tổ chức để đảm bảo tính bảo mật và khả năng mở rộng.

### 2.1. Vấn đề của mô hình cũ
Sơ đồ ERD và logic ban đầu liên kết trực tiếp `User` với `Role`. Mô hình này chỉ hoạt động cho một hệ thống duy nhất (single-tenant). Nó không trả lời được câu hỏi: "User A có vai trò Admin **ở Tổ chức nào?**".

### 2.2. Mô hình mới: Lấy Tổ chức làm trung tâm

Chúng ta sẽ giới thiệu `Organization` như một thực thể trung tâm và thay đổi cách liên kết người dùng với vai trò.

**Sơ đồ ERD khái niệm mới:**

<p align="center">
    User  ‹--›  <strong>UserOrganizationRole</strong>  ‹--›  Role
                      |
                      v
                  Organization
</p>

**Điểm cốt lõi:**
1.  **Bảng `tbl_user_organization_roles`:** Đây là bảng liên kết quan trọng nhất, thay thế cho `tbl_users_roles` cũ. Nó chứa 3 khóa ngoại: `user_id`, `organization_id`, `role_id`. Bảng này định nghĩa rằng: "Một người dùng có một vai trò cụ thể **bên trong một tổ chức nhất định**".
2.  **Phân loại Vai trò (Roles):**
    *   **Vai trò Toàn cục (Global Roles):** Là các vai trò mặc định của hệ thống (`organization_id` trong bảng `tbl_roles` sẽ là `NULL`). Ví dụ: `super_admin`, `default_user`. Chúng không thể bị xóa hay sửa bởi người dùng.
    *   **Vai trò của Tổ chức (Organization Roles):** Là các vai trò do chính admin của một tổ chức tạo ra (`organization_id` sẽ có giá trị). Chúng chỉ có hiệu lực bên trong tổ chức đó.

## 3. Luồng hoạt động

### 3.1. Khởi tạo (DataInitializer)
1.  Đọc file `permissions.json` và đồng bộ hóa với bảng `tbl_permissions`. Logic này vẫn giữ nguyên.
2.  Tạo/cập nhật các **Vai trò Toàn cục** (`super_admin`, `default_user`) với `organization_id = NULL`.
3.  Tạo tài khoản `super_admin` từ `application.yml` và liên kết nó với một tổ chức hệ thống mặc định (nếu có) hoặc cho phép nó truy cập mọi tổ chức.

### 3.2. Xử lý API (API Handling Flow)
1.  **Gateway Service:**
    *   Chặn mọi request.
    *   **Nhận diện Tổ chức:** Xác định `organization_id` dựa trên subdomain (ví dụ: `wayne-corp.elearning.com`) hoặc từ path API.
    *   **Xác thực Token:** Gọi đến `auth-service` để xác thực JWT.
    *   **Thêm Context vào Header:** Thêm hai header quan trọng vào request trước khi chuyển tiếp đến các service nội bộ:
        *   `X-User-Id: 123`
        *   `X-Organization-Id: 456`
2.  **Auth Service (và các service khác):**
    *   **Đọc Context:** Đọc `X-User-Id` và `X-Organization-Id` từ header của request.
    *   **Lọc dữ liệu:** **Mọi câu lệnh truy vấn CSDL** (`SELECT`, `UPDATE`, `DELETE`) **BẮT BUỘC** phải có điều kiện `WHERE organization_id = ?` để đảm bảo chỉ thao tác trên dữ liệu của tổ chức hiện tại.
    *   **Kiểm tra quyền (Authorization Filter):**
        *   Lấy `user_id` và `organization_id` từ header.
        *   Truy vấn bảng `tbl_user_organization_roles` để tìm vai trò của người dùng trong tổ chức đó.
        *   Lấy danh sách các quyền từ vai trò tìm được.
        *   So sánh với API (path + method) đang được gọi. Nếu khớp thì cho qua, không thì trả về 403 Forbidden.

## 4. Thiết kế Entity (Database Schema)

#### `tbl_organizations`
| Tên Cột | Kiểu Dữ liệu |
| :--- | :--- |
| `id` (PK) | `BIGINT` |
| `uuid` | `VARCHAR(36)` |
| `slug` | `VARCHAR(100)` |
| `name` | `VARCHAR(255)` |

#### `tbl_roles`
| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` (PK) | `BIGINT` | |
| `name` | `VARCHAR(100)` | |
| `description` | `TEXT` | |
| `organization_id` | `BIGINT` | `NULL` cho vai trò toàn cục. |

#### `tbl_user_organization_roles`
| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` (PK) | `BIGINT` | |
| `user_id` (FK) | `BIGINT` | Khóa ngoại đến `tbl_users`. |
| `organization_id` (FK) | `BIGINT` | Khóa ngoại đến `tbl_organizations`. |
| `role_id` (FK) | `BIGINT` | Khóa ngoại đến `tbl_roles`. |

## 5. Danh sách API (Cập nhật cho Multi-tenancy)

### 5.1. Public APIs

#### 1. Đăng ký vào một Tổ chức
-   **Endpoint:** `POST /api/auth/register/{orgSlug}`
-   **Mục đích:** Đăng ký người dùng mới vào một tổ chức cụ thể.
-   **Request:**
    ```json
    {
      "username": "newuser",
      "fullName": "New User Name",
      "email": "newuser@example.com",
      "password": "password123"
    }
    ```

#### 2. Đăng nhập
-   **Endpoint:** `POST /api/auth/authenticate`
-   **Request:**
    ```json
    {
      "identifier": "superadmin", // username hoặc email
      "password": "superadminpassword"
    }
    ```
-   **Response:**
    ```json
    {
        "status": 200,
        "message": "Xác thực người dùng thành công",
        "data": {
            "accessToken": "ey...",
            "refreshToken": "ey..."
            // JWT payload nên chứa thông tin về các tổ chức mà user thuộc về
        }
    }
    ```
*(Các API public khác như refresh-token, forgot-password giữ nguyên)*

### 5.2. Admin APIs (Yêu cầu xác thực và quyền hạn)

**Lưu ý:** Tất cả các API quản trị giờ đây đều được lồng trong context của một tổ chức.

#### 1. Quản lý Người dùng trong một Tổ chức
-   **Lấy danh sách:** `POST /api/v1/orgs/{orgId}/users/list`
-   **Tạo người dùng mới:** `POST /api/v1/orgs/{orgId}/users`
-   **Cập nhật người dùng:** `PUT /api/v1/orgs/{orgId}/users/{userId}`
    *   **Request Body:**
        ```json
        {
          "fullName": "Tên Mới Của User",
          "status": "BLOCKED",
          "roleIds": // roleIds này là của các vai trò thuộc orgId đó
        }
        ```
-   **Xóa người dùng:** `DELETE /api/v1/orgs/{orgId}/users/{userId}`
-   **Xóa hàng loạt:** `DELETE /api/v1/orgs/{orgId}/users/batch`
    *   **Request Body:**
        ```json
        { "ids": }
        ```

#### 2. Quản lý Vai trò trong một Tổ chức
-   **Lấy danh sách:** `POST /api/v1/orgs/{orgId}/roles/list` (Sẽ trả về cả vai trò của tổ chức và vai trò toàn cục)
-   **Lấy chi tiết:** `GET /api/v1/orgs/{orgId}/roles/{roleId}`
-   **Tạo vai trò mới:** `POST /api/v1/orgs/{orgId}/roles`
    *   **Request Body:**
        ```json
        {
          "name": "Content Manager",
          "description": "Chỉ được sửa bài viết thôi",
          "permissionIds":
        }
        ```
-   **Xóa vai trò:** `DELETE /api/v1/orgs/{orgId}/roles/{roleId}` (Lưu ý: Chỉ xóa được vai trò của tổ chức, không xóa được vai trò toàn cục).

#### 3. Quản lý Quyền (Không đổi)
-   **Lấy danh sách:** `POST /api/v1/permissions/list` (Quyền là toàn cục, không phụ thuộc tổ chức).