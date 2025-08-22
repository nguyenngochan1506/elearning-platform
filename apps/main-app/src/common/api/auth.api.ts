import apiClient from "./apiClient";
import { AuthResponse } from "./response.interface";

interface AuthRequest {
  identifier: string;
  password: string;
}

export const login = async (
  credentials: AuthRequest,
): Promise<AuthResponse> => {
  const responseData = await apiClient.post<AuthResponse>(
    "/api/auth/authenticate",
    credentials,
  );

  return responseData.data;
};
