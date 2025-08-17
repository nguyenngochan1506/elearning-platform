import DefaultLayout from "@/layouts/default";
import { HeroSection } from "@/sections/home/HeroSection";
import { FeaturedCoursesSection } from "@/sections/home/FeaturedCoursesSection";
import { TestimonialsSection } from "@/sections/home/TestimonialsSection";

export default function HomePage() {
  return (
    <DefaultLayout>
      <HeroSection />
      <FeaturedCoursesSection />
      <TestimonialsSection />
    </DefaultLayout>
  );
}
