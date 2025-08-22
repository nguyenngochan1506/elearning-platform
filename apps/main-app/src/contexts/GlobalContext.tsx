import {
  createContext,
  useState,
  useContext,
  ReactNode,
  useCallback,
} from "react";

import {
  translations,
  TranslationKey,
  LanguageCode,
} from "@/common/translation";

interface GlobalContextType {
  language: LanguageCode;
  setLanguage: (language: LanguageCode) => void;
  translate: (key: TranslationKey) => string;
}

const GlobalContext = createContext<GlobalContextType | undefined>(undefined);

export const GlobalProvider = ({ children }: { children: ReactNode }) => {
  const [language, setLanguage] = useState<LanguageCode>("VN");

  const translate = useCallback(
    (key: TranslationKey): string => {
      const translationSet = translations[key];

      return translationSet?.[language] || key;
    },
    [language],
  );

  const value = {
    language,
    setLanguage,
    translate,
  };

  return (
    <GlobalContext.Provider value={value}>{children}</GlobalContext.Provider>
  );
};

export const useGlobal = () => {
  const context = useContext(GlobalContext);

  if (context === undefined) {
    throw new Error("useGlobal must be used within a GlobalProvider");
  }

  return context;
};
