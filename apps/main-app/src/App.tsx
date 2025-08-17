import { Route, Routes } from "react-router-dom";

import HomePage from "@/pages/index";

function App() {
  return (
    <Routes>
      <Route element={<HomePage />} path="/" />
    </Routes>
  );
}

export default App;
