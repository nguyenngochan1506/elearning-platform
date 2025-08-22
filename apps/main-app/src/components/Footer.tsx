import { Link } from "@heroui/link";

import { GithubIcon, TwitterIcon, DiscordIcon, Logo } from "./icons";

import { siteConfig } from "@/config/site";
import { useGlobal } from "@/contexts/GlobalContext";

export const Footer = () => {
  const { translate } = useGlobal();

  return (
    <footer className="w-full bg-default-100 py-12">
      <div className="container mx-auto max-w-7xl px-6 grid grid-cols-1 md:grid-cols-4 gap-8">
        <div className="flex flex-col gap-4 items-start">
          <div className="flex items-center gap-2">
            <Logo size={32} />
            <span className="font-bold text-lg">NgochanDev</span>
          </div>
          <p className="text-sm text-default-600">
            {translate("FOOTER_DESCRIPTION")}
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
          <h4 className="font-bold mb-4">{translate("FOOTER_COMPANY")}</h4>
          <div className="flex flex-col gap-2">
            <Link color="foreground" href="/about" size="sm">
              {translate("FOOTER_ABOUT")}
            </Link>
            <Link color="foreground" href="/blog" size="sm">
              Blog
            </Link>
            <Link color="foreground" href="#" size="sm">
              {translate("FOOTER_HR")}
            </Link>
          </div>
        </div>

        <div>
          <h4 className="font-bold mb-4">Hỗ trợ</h4>
          <div className="flex flex-col gap-2">
            <Link color="foreground" href="#" size="sm">
              {translate("FOOTER_QUESTION")}
            </Link>
            <Link color="foreground" href="#" size="sm">
              {translate("FOOTER_CONTACT")}
            </Link>
            <Link color="foreground" href="#" size="sm">
              {translate("FOOTER_PRIVACY")}
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
