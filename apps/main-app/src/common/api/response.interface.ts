export interface ErrorResponse {
  status: number;
  error: string;
  message: string | string[];
  timestamp: string;
  path: string;
}

export interface SuccessResponse<T> {
  status: number;
  message: string;
  data: T;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
}
