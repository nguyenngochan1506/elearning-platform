# 1. Vài điểm cần lưu ý
##   1.1 Nguyên tắc chung
  - Tầng `service` chỉ gọi `repository` của chính nó, cấm gọi repo của thằng khác (hoặc là cùng domain).
  - Mật khẩu thì tất nhiên là phải hash, không được lưu raw text trong DB.
##  1.2 Đặt tên cho nó chuẩn
- **Class**, **Entity**, **Service** các kiểu: Dùng `PascalCase`, nhớ là Số Ít. Ví dụ: `UserEntity`, `RoleRepository`. Đừng có `UsersEntity` nhé, một object là một thằng user thôi.
- **Table** trong DB: Dùng **snake_case**, và là Số **Nhiều**. Ví dụ: `tbl_users`, `tbl_roles_permissions`.
## 2. Cái trò phân quyền mới (RBAC Động)
  - Cái hệ thống phân quyền cũ hard-code trong code tù lắm, mỗi lần sửa lại phải build lại mệt người. Giờ tôi đổi rồi, nó sẽ tự động hết, ngon hơn nhiều.
<p align="center">
<img src="img_3.png" alt="Sơ đồ ERD" title="Sơ đồ ERD">
</p>

### Đây là cách nó chạy:
#### Khi app khởi động:
1. Thằng `DataInitializer` sẽ vào việc. Đầu tiên nó đọc `file src/main/resources/migrations/permissions.json`.
2. Nó sẽ so sánh file này với CSDL (bảng `tbl_permissions`) rồi tự động thêm, sửa, hoặc xóa mềm các quyền cho khớp. Giờ file permissions.json là "sếp", CSDL phải nghe theo. 
3. Tiếp theo, nó tạo/cập nhật 2 role cứng:
   1. `super_admin`: Được bem cho TẤT CẢ các quyền có trong hệ thống. Full mẹ nó quyền.
   2. `default_user`: Chỉ được cấp mấy quyền cơ bản cho người dùng mới đăng ký (là mấy quyền có "isDefault": true trong file json ấy).
   3. Cuối cùng, nó check xem đã có thằng `super_admin` nào chưa. Nếu chưa, nó sẽ tạo một tài khoản (info lấy từ file `application.yml`) và gán cho vai trò `super_admin`.
#### Khi gọi API:
   1. Thằng `PermissionFilter` nó sẽ chặn lại.
   2. Nó xem mình là ai, có những role gì. 
   3. Nó vào DB lôi hết permission của đống role đó ra.
   4. Nó so sánh cái API mình đang gọi (path + method) với đống permission mình có.
      1. Khớp: Ok mời vào.
      2. Không khớp: 403 Forbidden, `đi về nhà với góc vườn nhiều chó nhều gà`. 
#### Ngon ở chỗ nào?
   1. Muốn đổi quyền cho role? Vào API mà sửa, không cần đụng code.
   2. Muốn thêm/bớt một quyền của hệ thống? Cứ sửa file `permissions.json` rồi khởi động lại app là xong.
## Danh sách API
### Đám Public API (Công khai)
#### Mấy cái này thì không cần đăng nhập cũng gọi được.
#### 1. Đăng ký
- **Endpoint**: **POST** `/api/auth/register`
- **Để làm gì?:** Đăng ký user mới. Tự gán role `default_user` rồi `gửi mail bắt xác thực`.
- **Request**:
```json
{
  "username: "newuser",
  "fullName": "New User Name",
  "email": "newuser@example.com",
  "password": "password123"
}
```
- **Response** (OK thì nó ra vầy):
```json
{
  "status": 201,
  "message": "Đăng ký người dùng thành công, vui lòng xác minh email",
  "data": 15
}
```

#### 2. Đăng nhập
- **Endpoint**: **POST** `/api/auth/authenticate`
- **Để làm gì?**: Lấy token để đi gọi các API khác.
- **Request:**
```json
{
  "identifier": "superadmin",
  "password": "superadminpassword"
}
```
- **Response** (OK thì nó ra vầy):
```json
{
    "status": 200,
    "message": "Xác thực người dùng thành công",
    "data": {
        "accessToken": "ey...",
        "refreshToken": "ey..."
    }
}
```
#### 3. Làm mới Token
- **Endpoint**: **POST** `/api/auth/refresh-token`
- **Để làm gì?**: Dùng `refreshToken` cũ lấy `accessToken` mới.
- **Request**:
```json
{
  "token": "cái_refreshToken_cũ_dán_vào_đây"
}
```
#### 4. Đổi mật khẩu
- **Endpoint**: **PATCH** `/api/auth/change-password`
- **Lưu ý:** Cái này cần đăng nhập rồi mới gọi được (này cũng tuỳ business).
- **Request**:
```json
{
  "userId": 1,
  "oldPassword": "mật_khẩu_hiện_tại",
  "newPassword": "mật_khẩu_mới",
  "confirmPassword": "nhập_lại_mật_khẩu_mới"
}
```
#### (Các API quên mật khẩu, reset, xác thực email tương tự, không có gì đặc biệt)
### Đám API Quản lý User (Chỉ Admin mới được nghịch)
#### Mấy cái này phải có Bearer Token (_là cái accessToken đó_) với quyền ngon mới gọi được.
#### 1. Lấy danh sách user (có filter, search, xoắn não)
- **Endpoint**: **POST** `/api/v1/users/list`
- **Request**:
```json
{
  "filters": [
    { "field": "username", "operator": "contains", "value": "admin" },
    { "field": "status", "operator": "in", "value": ["ACTIVE", "INACTIVE"] }
  ],
  "sort": "createdAt:DESC",
  "page": 1,
  "size": 10
}
```
- **Response** (OK thì nó ra vầy):
```json
{
    "status": 200,
    "message": "Lấy thông tin người dùng thành công",
    "data": {
        "currentPage": 1,
        "totalElements": 1,
        "totalPages": 1,
        "items": [
            {
                "id": 1,
                "username": "superadmin",
                "fullName": "Super Administrator",
                "email": "super.admin@example.com",
                "status": "ACTIVE",
                "roles": [ { "id": 1, "name": "super_admin" } ]
            }
        ]
    }
}
```
#### 2. Tạo user (do Admin tạo)
- **Endpoint**: **POST** `/api/v1/users`
- **Request**:
```json
{
  "username": "staffuser",
  "fullName": "Staff User",
  "email": "staff@example.com",
  "password": "password123",
  "status": "ACTIVE",
  "roleIds": [2] 
}
```
#### 3. Sửa user
- **Endpoint**: **PUT** `/api/v1/users/update`
- **Request**:
```json
{
  "id": 15,
  "fullName": "Tên Mới Của User",
  "status": "BLOCKED",
  "roleIds": [2, 3] 
}
```
#### 4. Xóa 1 user
**Endpoint**: **DELETE** `/api/v1/users/{id}`
#### 5. Xóa cả đống user
- **Endpoint**: **DELETE** `/api/v1/users/batch`
- **Request**:
```json
{
  "ids": [15, 16, 17]
}
```

### Đám API Quản lý Role
#### Cũng cần quyền mới được sờ vào.
#### 1. Lấy danh sách role
- **Endpoint**: **POST** `/api/v1/roles/list`
2. Lấy chi tiết 1 role (xem nó có những quyền gì)
- **Endpoint**: **GET** **/api/v1/roles/{id}**
- **Response** (OK thì nó ra vầy):
```json
{
  "status": 200,
  "message": "Lấy thông tin vai trò thành công",
  "data": {
    "id": 1,
    "name": "super_admin",
    "description": "Full system access",
    "permissions": [
      { "id": 1, "name": "Đổi mật khẩu người dùng", ... },
      { "id": 2, "name": "Làm mới token người dùng", ... }
    ]
  }
}
```
#### 3. Tạo role mới
- **Endpoint**: **POST** `/api/v1/roles`
- **Request:**
```json
{
  "name": "Content Manager",
  "description": "Chỉ được sửa bài viết thôi",
  "permissionIds": [1, 5, 8, 12]
}
```

#### (Xóa 1 role, xóa nhiều role tương tự như User)
### API Quản lý Quyền
#### 1. Lấy danh sách quyền
- **Endpoint**: **POST** `/api/v1/permissions/list`
- **Để làm gì?**: Chỉ để xem hệ thống có những quyền gì thôi (lấy từ file `permissions.json` ra). API này chỉ để `READ-ONLY`. Không có tạo, sửa, xóa gì ở đây hết.
- Request:
```json
{
  "page": 1,
  "size": 10,
  "search": "user"
}
```
