package com.GoWalk.global.common;

import com.GoWalk.global.dto.ErrorResponse;
import java.util.Map;

public record ApiResponse<T>(
    T data,
    ErrorResponse error
) {
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(data, null);
  }

  public static ApiResponse<Void> error(String code, String message) {
    return new ApiResponse<>(null, ErrorResponse.of(code, message));
  }

  public static ApiResponse<Void> error(String code, String message, Map<String, String> details) {
    return new ApiResponse<>(null, ErrorResponse.of(code, message, details));
  }

  public static ApiResponse<Void> error(ErrorResponse error) {
    return new ApiResponse<>(null, error);
  }
}

