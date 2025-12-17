package com.example.identity_service.dto.request;

import com.example.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(min = 4, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "FIELD_REQUIRED")
    String firstName;

    @NotBlank(message = "FIELD_REQUIRED")
    String lastName;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
}
