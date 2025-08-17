import { Link } from "@heroui/link";

import { GithubIcon, TwitterIcon, DiscordIcon, Logo } from "./icons";

import { siteConfig } from "@/config/site";

export const Footer = () => {
  return (
    <footer className="w-full bg-default-100 py-12">
      <div className="container mx-auto max-w-7xl px-6 grid grid-cols-1 md:grid-cols-4 gap-8">
        <div className="flex flex-col gap-4 items-start">
          <div className="flex items-center gap-2">
            <Logo size={32} />
            <span className="font-bold text-lg">NgochanDev</span>
          </div>
          <p className="text-sm text-default-600">
            Nền tảng học trực tuyến giúp bạn mở khóa tiềm năng.
          </p>
          <div className="flex gap-4">
            <Link
              isExternal
              aria-label="Twitter"
              href={siteConfig.links.twitter}
            >
              <TwitterIcon className="text-default-500" />
            </Link>
            <Link
              isExternal
              aria-label="Discord"
              href={siteConfig.links.discord}
            >
              <DiscordIcon className="text-default-500" />
            </Link>
            <Link isExternal aria-label="Github" href={siteConfig.links.github}>
              <GithubIcon className="text-default-500" />
            </Link>
          </div>
        </div>

        <div>
          <h4 className="font-bold mb-4">Khóa học</h4>
          <div className="flex flex-col gap-2">
            <Link color="foreground" href="#" size="sm">
              Lập trình Web
            </Link>
            <Link color="foreground" href="#" size="sm">
              Khoa học Dữ liệu
            </Link>
            <Link color="foreground" href="#" size="sm">
              Thiết kế UI/UX
            </Link>
            <Link color="foreground" href="#" size="sm">
              Marketing
            </Link>
          </div>
        </div>

        <div>
          <h4 className="font-bold mb-4">Công ty</h4>
          <div className="flex flex-col gap-2">
            <Link color="foreground" href="/about" size="sm">
              Về chúng tôi
            </Link>
            <Link color="foreground" href="/blog" size="sm">
              Blog
            </Link>
            <Link color="foreground" href="#" size="sm">
              Tuyển dụng
            </Link>
          </div>
        </div>

        <div>
          <h4 className="font-bold mb-4">Hỗ trợ</h4>
          <div className="flex flex-col gap-2">
            <Link color="foreground" href="#" size="sm">
              Câu hỏi thường gặp
            </Link>
            <Link color="foreground" href="#" size="sm">
              Liên hệ
            </Link>
            <Link color="foreground" href="#" size="sm">
              Chính sách
            </Link>
          </div>
        </div>
      </div>
      <div className="container mx-auto max-w-7xl px-6 mt-8 text-center text-sm text-default-500 border-t border-default-200 pt-4">
        © {new Date().getFullYear()} NgochanDev. All Rights Reserved.
      </div>
    </footer>
  );
};
