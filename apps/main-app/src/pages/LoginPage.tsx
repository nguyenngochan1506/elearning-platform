import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";
import { Link as RouterLink } from "react-router-dom";

import { Logo } from "@/components/icons";
import { useGlobal } from "@/contexts/GlobalContext";

export default function LoginPage() {
  const { translate } = useGlobal();

  return (
    <div className="flex items-center justify-center min-h-screen bg-default-50">
      <Card className="w-full max-w-md p-6">
        <CardHeader className="flex flex-col items-center text-center">
          <RouterLink to="/">
            <Logo size={40} />
          </RouterLink>
          <h1 className="text-2xl font-bold mt-4">
            {translate("LOGIN_WELCOME")}
          </h1>
          <p className="text-default-500 text-sm mt-1">
            {translate("LOGIN_PROMPT")}
          </p>
        </CardHeader>
        <CardBody>
          <form className="flex flex-col gap-4">
            <Input
              isRequired
              label="Email"
              placeholder={translate("LOGIN_EMAIL_PLACEHOLDER")}
              type="email"
            />
            <Input
              isRequired
              label={translate("COMMON_PASSWORD")}
              placeholder={translate("LOGIN_PASSWORD_PLACEHOLDER")}
              type="password"
            />
            <div className="flex justify-end">
              <Link as={RouterLink} size="sm" to="/forgot-password">
                {translate("LOGIN_FORGOT_PASSWORD")}
              </Link>
            </div>
            <Button
              className="w-full font-semibold"
              color="primary"
              size="lg"
              type="submit"
            >
              {translate("LOGIN_SUBMIT")}
            </Button>
          </form>
        </CardBody>
        <CardFooter className="flex justify-center">
          <Link as={RouterLink} size="sm" to="/register">
            {translate("LOGIN_REGISTER_PROMPT")}
          </Link>
          <span className="mx-2">|</span>
          <Link as={RouterLink} size="sm" to="/">
            {translate("LOGIN_BACK_TO_HOME")}
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}
