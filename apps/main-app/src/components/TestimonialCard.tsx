import { Card, CardHeader, CardBody } from "@heroui/card";
import { Avatar } from "@heroui/avatar";

interface TestimonialCardProps {
  name: string;
  course: string;
  quote: string;
  avatarUrl: string;
}

export const TestimonialCard = ({
  name,
  course,
  quote,
  avatarUrl,
}: TestimonialCardProps) => {
  return (
    <Card className="p-4 bg-default-50 border border-transparent dark:border-default-200">
      <CardHeader className="gap-3">
        <Avatar size="md" src={avatarUrl} />
        <div className="flex flex-col">
          <p className="font-semibold">{name}</p>
          <p className="text-sm text-default-500">{course}</p>
        </div>
      </CardHeader>
      <CardBody>
        <p className="italic text-default-700">"{quote}"</p>
      </CardBody>
    </Card>
  );
};
