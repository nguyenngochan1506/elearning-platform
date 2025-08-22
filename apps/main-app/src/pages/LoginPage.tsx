import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";
import { Link as RouterLink } from "react-router-dom";

import { Logo } from "@/components/icons";

export default function LoginPage() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-default-50">
      <Card className="w-full max-w-md p-6">
        <CardHeader className="flex flex-col items-center text-center">
          <RouterLink to="/">
            <Logo size={40} />
          </RouterLink>
          <h1 className="text-2xl font-bold mt-4">Chào mừng trở lại!</h1>
          <p className="text-default-500 text-sm mt-1">
            Đăng nhập để tiếp tục khóa học của bạn.
          </p>
        </CardHeader>
        <CardBody>
          <form className="flex flex-col gap-4">
            <Input
              isRequired
              label="Email"
              placeholder="Nhập địa chỉ email của bạn"
              type="email"
            />
            <Input
              isRequired
              label="Mật khẩu"
              placeholder="Nhập mật khẩu của bạn"
              type="password"
            />
            <div className="flex justify-end">
              <Link as={RouterLink} size="sm" to="/forgot-password">
                Quên mật khẩu?
              </Link>
            </div>
            <Button
              className="w-full font-semibold"
              color="primary"
              size="lg"
              type="submit"
            >
              Đăng nhập
            </Button>
          </form>
        </CardBody>
        <CardFooter className="flex justify-center">
          <Link as={RouterLink} size="sm" to="/register">
            Chưa có tài khoản? Đăng ký ngay
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}
