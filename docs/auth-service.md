

## 1 vài điểm cần lưu ý
- Tầng service chỉ nên gọi tầng repository của chính nó và cùng domain, không nên gọi tầng repository của domain khác.

## mẫu data response

```json
{
  "status": 200,
  "message": "user created successfully",
  "data": {
    "id": 123,
    "username": "john_doe",
    "email": "admin@gmail.com"
  }
}
```