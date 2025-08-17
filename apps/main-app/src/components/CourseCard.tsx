import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Button } from "@heroui/button";
import { Image } from "@heroui/image";

interface CourseCardProps {
  title: string;
  instructor: string;
  price: string;
  imageUrl: string;
  courseUrl: string;
}

export const CourseCard = ({
  title,
  instructor,
  price,
  imageUrl,
  courseUrl,
}: CourseCardProps) => {
  return (
    <Card isHoverable isPressable className="w-full">
      <CardHeader className="p-0">
        <Image
          removeWrapper
          alt={`Thumbnail for ${title}`}
          className="z-0 w-full h-[180px] object-cover"
          src={imageUrl}
        />
      </CardHeader>
      <CardBody className="flex flex-col gap-2 p-4">
        <h4 className="font-bold text-large line-clamp-2">{title}</h4>
        <p className="text-sm text-default-500">{instructor}</p>
      </CardBody>
      <CardFooter className="justify-between p-4 pt-0">
        <p className="font-bold text-primary">{price}</p>
        <Button
          as="a"
          color="primary"
          href={courseUrl}
          size="sm"
          variant="flat"
        >
          Xem chi tiết
        </Button>
      </CardFooter>
    </Card>
  );
};
