import {
  Navbar as HeroUINavbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenuToggle,
  NavbarMenu,
  NavbarMenuItem,
} from "@heroui/navbar";
import { Button } from "@heroui/button";
import {
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
} from "@heroui/dropdown";
import { Link } from "@heroui/link";
import { Input } from "@heroui/input";
import { Kbd } from "@heroui/kbd";
import clsx from "clsx";
import { link as linkStyles } from "@heroui/theme";

import { siteConfig } from "@/config/site";
import { ThemeSwitch } from "@/components/theme-switch";
import {
  GithubIcon,
  SearchIcon,
  Logo,
  ChevronDownIcon,
} from "@/components/icons";

export const Navbar = () => {
  // Component tìm kiếm
  const searchInput = (
    <Input
      aria-label="Search"
      classNames={{
        inputWrapper: "bg-default-100",
        input: "text-sm",
      }}
      endContent={
        <Kbd className="hidden lg:inline-block" keys={["command"]}>
          K
        </Kbd>
      }
      labelPlacement="outside"
      placeholder="Tìm kiếm khóa học..."
      startContent={
        <SearchIcon className="text-base text-default-400 pointer-events-none flex-shrink-0" />
      }
      type="search"
    />
  );

  // Component chuyển đổi ngôn ngữ
  const languageSwitcher = (
    <Dropdown>
      <DropdownTrigger>
        <Button className="gap-1" variant="light">
          VN
          <ChevronDownIcon className="text-default-500" />
        </Button>
      </DropdownTrigger>
      <DropdownMenu aria-label="Language Actions">
        <DropdownItem key="vietnamese">Tiếng Việt (VN)</DropdownItem>
        <DropdownItem key="english">English (EN)</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  );

  return (
    <HeroUINavbar maxWidth="xl" position="sticky">
      {/* ===== PHẦN BÊN TRÁI ===== */}
      <NavbarContent justify="start">
        <NavbarBrand className="gap-3 max-w-fit">
          <Link
            className="flex justify-start items-center gap-2"
            color="foreground"
            href="/"
          >
            <Logo />
            <p className="font-bold text-inherit">ACME</p>
          </Link>
        </NavbarBrand>
        <div className="hidden lg:flex gap-4 justify-start ml-2">
          {siteConfig.navItems.map((item) => (
            <NavbarItem key={item.href}>
              <Link
                className={clsx(
                  linkStyles({ color: "foreground" }),
                  "data-[active=true]:text-primary data-[active=true]:font-medium",
                )}
                color="foreground"
                href={item.href}
              >
                {item.label}
              </Link>
            </NavbarItem>
          ))}
        </div>
      </NavbarContent>

      {/* ===== PHẦN Ở GIỮA (Tìm kiếm) ===== */}
      <NavbarContent className="hidden md:flex" justify="center">
        <NavbarItem className="w-[300px] lg:w-[400px]">
          {searchInput}
        </NavbarItem>
      </NavbarContent>

      {/* ===== PHẦN BÊN PHẢI ===== */}
      <NavbarContent justify="end">
        <NavbarItem className="hidden sm:flex items-center gap-2">
          <Link isExternal aria-label="GitHub" href={siteConfig.links.github}>
            <GithubIcon className="text-default-500" />
          </Link>
          <ThemeSwitch />
          {languageSwitcher}
        </NavbarItem>
        <NavbarItem className="hidden sm:flex">
          <Button as={Link} href="#" variant="light">
            Đăng nhập
          </Button>
        </NavbarItem>
        <NavbarItem className="hidden sm:flex">
          <Button as={Link} color="primary" href="#" variant="flat">
            Đăng ký
          </Button>
        </NavbarItem>

        {/* Nút Menu cho màn hình nhỏ */}
        <NavbarItem className="sm:hidden">
          <NavbarMenuToggle />
        </NavbarItem>
      </NavbarContent>

      {/* ===== MENU CHO MÀN HÌNH NHỎ (MOBILE) ===== */}
      <NavbarMenu>
        {searchInput}
        <div className="mx-4 mt-2 flex flex-col gap-2">
          {siteConfig.navItems.map((item, index) => (
            <NavbarMenuItem key={`${item}-${index}`}>
              <Link color="foreground" href={item.href} size="lg">
                {item.label}
              </Link>
            </NavbarMenuItem>
          ))}
          <NavbarMenuItem>
            <Link color="foreground" href="#" size="lg">
              Đăng nhập
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Button
              as={Link}
              className="w-full"
              color="primary"
              href="#"
              variant="flat"
            >
              Đăng ký
            </Button>
          </NavbarMenuItem>
        </div>
      </NavbarMenu>
    </HeroUINavbar>
  );
};
