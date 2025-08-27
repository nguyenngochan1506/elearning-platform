# Tài liệu Thiết kế Kỹ thuật: Payment Service

## 1. Tổng quan

### 1.1. Trách nhiệm

**Payment Service** là một microservice độc lập, chịu trách nhiệm hoàn toàn cho các chức năng thương mại hóa.

*   **Chức năng chính:**
    *   Quản lý Sản phẩm và các gói giá.
    *   Tích hợp an toàn với Stripe.
    *   Xử lý luồng thanh toán và quản lý đăng ký.
    *   Cung cấp API nội bộ để kiểm tra quyền truy cập trả phí.
    *   Xử lý webhook từ Stripe.

### 1.2. Công nghệ
*   **Framework:** Java & Spring Boot
*   **Cơ sở dữ liệu:** PostgreSQL
*   **Cổng thanh toán:** Stripe
*   **Giao tiếp:** REST API và Apache Kafka

## 2. Thiết kế Entity (JPA Entities)

#### Bảng `products`
| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` | `BIGINT` (PK) | Khóa chính. |
| `organization_id` | `BIGINT` | ID của tổ chức. |
| `name` | `VARCHAR(255)` | Tên sản phẩm. |
| `price` | `DECIMAL(10, 2)` | Giá. |
| `currency` | `VARCHAR(3)` | VD: "USD". |
| `type` | `VARCHAR(50)` | ENUM: `ONE_TIME`, `SUBSCRIPTION`. |
| `provider_product_id` | `VARCHAR(255)` | ID sản phẩm trên Stripe. |
| `provider_price_id` | `VARCHAR(255)` | ID giá trên Stripe. |
| `is_archived` | `BOOLEAN` | `true` nếu không còn được bán. |

#### Bảng `course_products`
Liên kết khóa học (định danh bằng ID ổn định) với sản phẩm.

| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `course_id` | `BIGINT` (PK) | ID của khóa học. **Không dùng slug**. |
| `product_id` | `BIGINT` (PK, FK) | Khóa ngoại đến `products`. |

#### Bảng `user_payments`
Lịch sử giao dịch của người dùng.

| Tên Cột | Kiểu Dữ liệu | Chú thích |
| :--- | :--- | :--- |
| `id` | `BIGINT` (PK) | Khóa chính. |
| `user_id` | `BIGINT` | ID của người dùng. |
| `product_id` | `BIGINT` (FK) | Khóa ngoại đến `products`. |
| `status` | `VARCHAR(50)` | ENUM: `PENDING`, `COMPLETED`, `FAILED`, `ACTIVE`. |
| `provider_session_id` | `VARCHAR(255)` | ID của Checkout Session trên Stripe. |
| `provider_subscription_id` | `VARCHAR(255)` | ID của Subscription trên Stripe. |
| `created_at` | `TIMESTAMP` | |

## 3. Luồng hoạt động chính

### 3.1. Luồng thanh toán
1.  **Client** gọi `POST /api/v1/checkout/product/{productId}`.
2.  **Payment Service:**
    a.  Tạo bản ghi `user_payments` với trạng thái `PENDING`.
    b.  Gọi API Stripe để tạo Checkout Session, đính kèm `user_payment_id` vào metadata.
    c.  Trả về `checkout_url`.
3.  **Client** điều hướng đến `checkout_url`.
4.  **Stripe** gửi webhook `checkout.session.completed` đến `POST /api/v1/webhooks/stripe`.
5.  **Payment Service (Webhook Handler):**
    a.  Xác thực webhook.
    b.  Cập nhật trạng thái `user_payments` thành `COMPLETED` / `ACTIVE`.
    c.  Truy vấn bảng `course_products` để lấy danh sách `courseId` liên quan đến `productId`.
    d.  **Phát sự kiện Kafka `payment.successful`** với `userId` và danh sách `courseIds`.

### 3.2. Luồng kiểm tra quyền truy cập (API Nội bộ)
1.  **Course Service** gọi `GET /api/v1/internal/access-check?userId={...}&courseId={...}`.
2.  **Payment Service:**
    a.  Tìm `productId` từ `courseId` trong bảng `course_products`. Nếu không tìm thấy, khóa học này miễn phí, trả về `{"hasAccess": true}`.
    b.  Kiểm tra trong bảng `user_payments` xem có bản ghi nào khớp `userId` và `productId` với trạng thái `COMPLETED` hoặc `ACTIVE` không.
    c.  Trả về `{"hasAccess": true}` hoặc `{"hasAccess": false}`.