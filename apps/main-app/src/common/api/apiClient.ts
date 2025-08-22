import axios, { AxiosError, AxiosResponse } from "axios";

import { ErrorResponse, SuccessResponse } from "./response.interface";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

apiClient.interceptors.response.use(
  <T>(response: AxiosResponse<SuccessResponse<T>>): T => {
    return response.data.data;
  },

  (error: AxiosError): Promise<ErrorResponse> => {
    if (error.response && error.response.data) {
      return Promise.reject(error.response.data as ErrorResponse);
    }

    const genericError: ErrorResponse = {
      status: error.status || 500,
      error: "Network Error",
      message: error.message || "An unexpected network error occurred.",
      timestamp: new Date().toISOString(),
      path: error.config?.url || "",
    };

    return Promise.reject(genericError);
  },
);

export default apiClient;
