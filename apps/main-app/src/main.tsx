import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";

import App from "./App.tsx";
import { Provider } from "./provider.tsx";

import "@/styles/globals.css";
import { GlobalProvider } from "@/contexts/GlobalContext.tsx";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      <GlobalProvider>
        <Provider>
          <App />
        </Provider>
      </GlobalProvider>
    </BrowserRouter>
  </React.StrictMode>,
);
