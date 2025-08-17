import { Link } from "@heroui/link";
import { Button } from "@heroui/button";

import { title, subtitle } from "@/components/primitives";

export const HeroSection = () => {
  return (
    <section className="flex flex-col lg:flex-row items-center justify-center gap-12 py-8 md:py-20">
      <div className="max-w-xl text-center lg:text-left">
        <h1 className={title({ size: "lg" })}>
          Mở Khóa{" "}
          <span className={title({ color: "violet", size: "lg" })}>
            Tiềm Năng
          </span>
          ,
          <br />
          Vững Bước Tương Lai.
        </h1>
        <p className={subtitle({ class: "mt-4 !w-full" })}>
          Nền tảng học trực tuyến hàng đầu với các khóa học từ chuyên gia đầu
          ngành trong lĩnh vực công nghệ, kinh doanh và sáng tạo.
        </p>
        <div className="flex gap-4 mt-8 justify-center lg:justify-start">
          <Button
            as={Link}
            color="primary"
            href="#"
            radius="full"
            size="lg"
            variant="shadow"
          >
            Bắt đầu học ngay
          </Button>
          <Button
            as={Link}
            href="#featured-courses"
            radius="full"
            size="lg"
            variant="bordered"
          >
            Khám phá khóa học
          </Button>
        </div>
      </div>
      <div className="w-full max-w-md">
        <img
          alt="Illustration of students learning online"
          className="rounded-xl shadow-2xl"
          src="https://via.placeholder.com/500x400"
        />
      </div>
    </section>
  );
};
