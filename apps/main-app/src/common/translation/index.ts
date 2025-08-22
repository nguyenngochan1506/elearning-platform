import { COMMON_TRANSLATIONS } from "./common";
import { COURSE_TRANSLATIONS } from "./course";
import { FOOTER_TRANSLATIONS } from "./footer";
import { HERO_TRANSLATIONS } from "./hero";
import { NAVBAR_TRANSLATIONS } from "./navbar";

export type LanguageCode = "VN" | "EN";

export const translations = {
  ...COMMON_TRANSLATIONS,
  ...NAVBAR_TRANSLATIONS,
  ...HERO_TRANSLATIONS,
  ...COURSE_TRANSLATIONS,
  ...FOOTER_TRANSLATIONS,
};

export type TranslationKey = keyof typeof translations;
