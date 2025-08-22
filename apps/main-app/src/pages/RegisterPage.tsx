import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";
import { Link as RouterLink } from "react-router-dom";

import { Logo } from "@/components/icons";

export default function RegisterPage() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-default-50">
      <Card className="w-full max-w-md p-6">
        <CardHeader className="flex flex-col items-center text-center">
          <RouterLink to="/">
            <Logo size={40} />
          </RouterLink>
          <h1 className="text-2xl font-bold mt-4">Tạo tài khoản</h1>
          <p className="text-default-500 text-sm mt-1">
            Bắt đầu hành trình học tập của bạn ngay hôm nay.
          </p>
        </CardHeader>
        <CardBody>
          <form className="flex flex-col gap-4">
            <Input
              isRequired
              label="Họ và tên"
              placeholder="Nhập họ và tên của bạn"
              type="text"
            />
            <Input
              isRequired
              label="Email"
              placeholder="Nhập địa chỉ email"
              type="email"
            />
            <Input
              isRequired
              label="Mật khẩu"
              placeholder="Tạo mật khẩu của bạn"
              type="password"
            />
            <Input
              isRequired
              label="Xác nhận mật khẩu"
              placeholder="Nhập lại mật khẩu"
              type="password"
            />
            <Button
              className="w-full font-semibold"
              color="primary"
              size="lg"
              type="submit"
            >
              Đăng ký
            </Button>
          </form>
        </CardBody>
        <CardFooter className="flex justify-center">
          <Link as={RouterLink} size="sm" to="/login">
            Đã có tài khoản? Đăng nhập
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}
