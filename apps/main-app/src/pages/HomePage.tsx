import DefaultLayout from "@/layouts/default";
import { FeaturedCoursesSection } from "@/sections/home/FeaturedCoursesSection";
import { HeroSlideshow } from "@/sections/home/HeroSlideShow";

export default function HomePage() {
  return (
    <DefaultLayout>
      <HeroSlideshow />
      <FeaturedCoursesSection />
    </DefaultLayout>
  );
}
