import { title } from "@/components/primitives";
import { TestimonialCard } from "@/components/TestimonialCard";

// Dữ liệu giả
const testimonials = [
  {
    name: "Phạm Nhật V.",
    course: "Học viên khóa ReactJS Toàn diện",
    quote:
      "Khóa học thật tuyệt vời! Lượng kiến thức rất thực tế và giảng viên hỗ trợ rất nhiệt tình. Tôi đã tự tin hơn rất nhiều sau khi hoàn thành.",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026704d",
  },
  {
    name: "Đặng Lê N.",
    course: "Học viên khóa Mastering UI/UX",
    quote:
      "Cách tiếp cận của khóa học rất trực quan và dễ hiểu. Các dự án thực hành giúp tôi xây dựng portfolio ấn tượng. Rất khuyến khích!",
    avatarUrl: "https://i.pravatar.cc/150?u=a04258114e29026702d",
  },
  {
    name: "Trần Anh H.",
    course: "Học viên khóa Python for Data Science",
    quote:
      "Đây là khóa học về Khoa học Dữ liệu tốt nhất tôi từng tham gia. Nội dung sâu sắc, bài tập thử thách và cộng đồng hỗ trợ tuyệt vời.",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026706d",
  },
];

export const TestimonialsSection = () => {
  return (
    <section className="py-8 md:py-20 bg-default-50 rounded-2xl">
      <div className="text-center mb-12">
        <h2 className={title({ size: "md" })}>
          Học viên của chúng tôi nói gì?
        </h2>
        <p className="text-default-600 mt-2">
          Niềm tin và sự thành công của bạn là động lực của chúng tôi.
        </p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {testimonials.map((testimonial, index) => (
          <TestimonialCard key={index} {...testimonial} />
        ))}
      </div>
    </section>
  );
};
