package com.example.identity_service.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.identity_service.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotBlank(message = "FIELD_REQUIRED")
    @Size(min = 4, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "FIELD_REQUIRED")
    String firstName;

    @NotBlank(message = "FIELD_REQUIRED")
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}
