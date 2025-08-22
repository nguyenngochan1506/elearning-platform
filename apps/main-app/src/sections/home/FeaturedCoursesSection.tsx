import { title } from "@/components/primitives";
import { CourseCard } from "@/components/CourseCard";
import { useGlobal } from "@/contexts/GlobalContext";

const featuredCourses = [
  {
    title: "Khóa học ReactJS Toàn diện từ Cơ bản đến Nâng cao",
    instructor: "Nguyễn Văn A",
    price: "1.200.000đ",
    imageUrl: "https://via.placeholder.com/400x225/A9A9A9/FFFFFF?text=ReactJS",
    courseUrl: "#",
  },
  {
    title: "Mastering UI/UX Design with Figma",
    instructor: "Trần Thị B",
    price: "990.000đ",
    imageUrl: "https://via.placeholder.com/400x225/7D3C98/FFFFFF?text=Figma",
    courseUrl: "#",
  },
  {
    title: "Python for Data Science and Machine Learning Bootcamp",
    instructor: "Lê Văn C",
    price: "1.500.000đ",
    imageUrl: "https://via.placeholder.com/400x225/1E8449/FFFFFF?text=Python",
    courseUrl: "#",
  },
];

export const FeaturedCoursesSection = () => {
  const { translate } = useGlobal();

  return (
    <section className="py-8 md:py-20" id="featured-courses">
      <div className="text-center mb-12">
        <h2 className={title({ size: "md" })}>
          {translate("FEATURED_COURSES_TITLE")}
        </h2>
        <p className="text-default-600 mt-2">
          {translate("FEATURED_COURSES_SUBTITLE")}
        </p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {featuredCourses.map((course, index) => (
          <CourseCard key={index} {...course} />
        ))}
      </div>
    </section>
  );
};
