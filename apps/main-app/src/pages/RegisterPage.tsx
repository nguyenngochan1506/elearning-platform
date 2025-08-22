import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";
import { Link as RouterLink } from "react-router-dom";

import { Logo } from "@/components/icons";
import { useGlobal } from "@/contexts/GlobalContext";

export default function RegisterPage() {
  const { translate } = useGlobal();

  return (
    <div className="flex items-center justify-center min-h-screen bg-default-50">
      <Card className="w-full max-w-md p-6">
        <CardHeader className="flex flex-col items-center text-center">
          <RouterLink to="/">
            <Logo size={40} />
          </RouterLink>
          <h1 className="text-2xl font-bold mt-4">
            {translate("REGISTER_TITLE")}
          </h1>
          <p className="text-default-500 text-sm mt-1">
            {translate("REGISTER_PROMPT")}
          </p>
        </CardHeader>
        <CardBody>
          <form className="flex flex-col gap-4">
            <Input
              isRequired
              label={translate("COMMON_FULL_NAME")}
              placeholder={translate("COMMON_FULL_NAME_PLACEHOLDER")}
              type="text"
            />
            <Input
              isRequired
              label={translate("COMMON_EMAIL")}
              placeholder={translate("COMMON_EMAIL_PLACEHOLDER")}
              type="email"
            />
            <Input
              isRequired
              label={translate("COMMON_PASSWORD")}
              placeholder={translate("COMMON_PASSWORD_PLACEHOLDER")}
              type="password"
            />
            <Input
              isRequired
              label={translate("COMMON_CONFIRM_PASSWORD")}
              placeholder={translate("COMMON_CONFIRM_PASSWORD_PLACEHOLDER")}
              type="password"
            />
            <Button
              className="w-full font-semibold"
              color="primary"
              size="lg"
              type="submit"
            >
              {translate("REGISTER_SUBMIT")}
            </Button>
          </form>
        </CardBody>
        <CardFooter className="flex justify-center">
          <Link as={RouterLink} size="sm" to="/login">
            {translate("REGISTER_LOGIN_PROMPT")}
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
