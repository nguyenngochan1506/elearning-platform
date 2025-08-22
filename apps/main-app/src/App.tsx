import { Route, Routes } from "react-router-dom";

import { HomePage, LoginPage, RegisterPage } from "@/pages";

function App() {
  return (
    <Routes>
      <Route element={<HomePage />} path="/" />
      <Route element={<LoginPage />} path="/login" />
      <Route element={<RegisterPage />} path="/register" />
    </Routes>
  );
}

export default App;
