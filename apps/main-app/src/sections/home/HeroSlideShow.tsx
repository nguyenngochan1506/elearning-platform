// src/sections/home/HeroSlideshow.tsx

import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";

// Dữ liệu cho các slide - bạn có thể dễ dàng tùy chỉnh tại đây
const slidesData = [
  {
    imageUrl:
      "https://images.unsplash.com/photo-1556740738-b6a63e27c4df?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&w=1600",
    title: "Mở Khóa Tiềm Năng, Vững Bước Tương Lai",
    subtitle:
      "Nền tảng học trực tuyến hàng đầu với các khóa học từ chuyên gia đầu ngành.",
    buttonText: "Bắt đầu học ngay",
    buttonLink: "#featured-courses",
  },
  {
    imageUrl:
      "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&w=1600",
    title: "Cộng Đồng Học Tập Năng Động và Sáng Tạo",
    subtitle: "Tham gia cùng hàng ngàn học viên, kết nối và chia sẻ kiến thức.",
    buttonText: "Tham gia cộng đồng",
    buttonLink: "#",
  },
  {
    imageUrl:
      "https://images.unsplash.com/photo-1588196749333-e184a6c4383b?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&w=1600",
    title: "Học Mọi Lúc, Mọi Nơi, Trên Mọi Thiết Bị",
    subtitle:
      "Truy cập không giới hạn vào thư viện khóa học phong phú của chúng tôi.",
    buttonText: "Khám phá khóa học",
    buttonLink: "#featured-courses",
  },
];

const slideVariants = {
  hidden: { opacity: 0, x: 50 },
  visible: { opacity: 1, x: 0 },
  exit: { opacity: 0, x: -50 },
};

export const HeroSlideshow = () => {
  const [activeIndex, setActiveIndex] = useState(0);

  // Hàm chuyển tới slide tiếp theo
  const goToNext = () => {
    setActiveIndex((prevIndex) =>
      prevIndex === slidesData.length - 1 ? 0 : prevIndex + 1,
    );
  };

  // Hàm quay lại slide trước
  const goToPrev = () => {
    setActiveIndex((prevIndex) =>
      prevIndex === 0 ? slidesData.length - 1 : prevIndex - 1,
    );
  };

  // Tự động chuyển slide sau một khoảng thời gian
  useEffect(() => {
    const timer = setInterval(() => {
      goToNext();
    }, 5000); // Chuyển slide mỗi 5 giây

    // Xóa timer khi component bị unmount hoặc khi activeIndex thay đổi
    // để reset bộ đếm, tránh hành vi không mong muốn.
    return () => clearInterval(timer);
  }, [activeIndex]);

  return (
    <section className="relative w-full h-[400px] md:h-[500px] overflow-hidden rounded-2xl shadow-xl my-8">
      {/* Container cho các slide */}
      <AnimatePresence mode="wait">
        <motion.div
          key={activeIndex} // Quan trọng: key thay đổi để AnimatePresence nhận diện component mới
          animate="visible"
          className="absolute inset-0"
          exit="exit"
          initial="hidden"
          transition={{ duration: 0.5, ease: "easeInOut" }}
          variants={slideVariants}
        >
          {/* Hình nền slide */}
          <img
            alt={slidesData[activeIndex].title}
            className="w-full h-full object-cover"
            src={slidesData[activeIndex].imageUrl}
          />
          {/* Lớp phủ màu tối để chữ nổi bật */}
          <div className="absolute inset-0 bg-black/60" />

          {/* Nội dung text */}
          <div className="absolute inset-0 flex flex-col items-center justify-center text-center p-6 text-white">
            <motion.h1
              animate={{ opacity: 1, y: 0 }}
              className="font-bold text-3xl md:text-5xl leading-tight mb-4"
              initial={{ opacity: 0, y: 20 }}
              transition={{ delay: 0.2, duration: 0.5 }}
            >
              {slidesData[activeIndex].title}
            </motion.h1>
            <motion.p
              animate={{ opacity: 1, y: 0 }}
              className="text-lg md:text-xl max-w-2xl mb-8"
              initial={{ opacity: 0, y: 20 }}
              transition={{ delay: 0.4, duration: 0.5 }}
            >
              {slidesData[activeIndex].subtitle}
            </motion.p>
            <motion.div
              animate={{ opacity: 1, y: 0 }}
              initial={{ opacity: 0, y: 20 }}
              transition={{ delay: 0.6, duration: 0.5 }}
            >
              <Button
                as={Link}
                className="font-semibold"
                color="primary"
                href={slidesData[activeIndex].buttonLink}
                radius="full"
                size="lg"
                variant="shadow"
              >
                {slidesData[activeIndex].buttonText}
              </Button>
            </motion.div>
          </div>
        </motion.div>
      </AnimatePresence>

      {/* Nút điều hướng */}
      <div className="absolute inset-0 flex items-center justify-between p-4">
        <button
          aria-label="Previous slide"
          className="bg-white/20 hover:bg-white/40 text-white p-2 rounded-full transition-colors focus:outline-none"
          onClick={goToPrev}
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            strokeWidth={2}
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M15.75 19.5L8.25 12l7.5-7.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>
        <button
          aria-label="Next slide"
          className="bg-white/20 hover:bg-white/40 text-white p-2 rounded-full transition-colors focus:outline-none"
          onClick={goToNext}
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            strokeWidth={2}
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M8.25 4.5l7.5 7.5-7.5 7.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>
      </div>

      {/* Dấu chấm điều hướng */}
      <div className="absolute bottom-5 left-1/2 -translate-x-1/2 flex gap-2">
        {slidesData.map((_, index) => (
          <button
            key={index}
            aria-label={`Go to slide ${index + 1}`}
            className={`w-3 h-3 rounded-full transition-all duration-300 ${
              activeIndex === index ? "bg-white scale-125" : "bg-white/50"
            }`}
            onClick={() => setActiveIndex(index)}
          />
        ))}
      </div>
    </section>
  );
};
