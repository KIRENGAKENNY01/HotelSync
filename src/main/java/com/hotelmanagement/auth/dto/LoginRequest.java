package com.hotelmanagement.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class LoginRequest {
    @NotBlank
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @NotBlank
    private String password;
}
