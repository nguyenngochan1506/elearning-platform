import { Navbar } from "@/components/navbar";
import { Footer } from "@/components/Footer";
export default function DefaultLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative flex flex-col min-h-screen">
      <Navbar />
      <div className="container mx-auto max-w-7xl px-6 flex-grow pt-16 flex">
        <main className="flex-grow">{children}</main>

        {/* <aside className="w-80 hidden xl:block pl-8 border-l border-default-200">
          <SocialSidebar />
        </aside> */}
      </div>
      <Footer />
    </div>
  );
}
