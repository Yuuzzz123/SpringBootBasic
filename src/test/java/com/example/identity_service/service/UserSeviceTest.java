package com.example.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
public class UserSeviceTest {

    @Autowired
    UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;

    private UserResponse userResponse;

    private User user;

    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("user")
                .password("123456789")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("6c8910dd72a9")
                .username("user")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        user = User.builder()
                .id("6c8910dd72a9")
                .username("user")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(Boolean.FALSE);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("6c8910dd72a9");
        Assertions.assertThat(response.getUsername()).isEqualTo("user");
        Assertions.assertThat(response.getFirstName()).isEqualTo("John");
        Assertions.assertThat(response.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(response.getDob()).isEqualTo(dob);
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(Boolean.TRUE);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    @WithMockUser(username = "user")
    void getMyInfo_valid_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        Assertions.assertThat(response.getId()).isEqualTo("6c8910dd72a9");
        Assertions.assertThat(response.getUsername()).isEqualTo("user");
        Assertions.assertThat(response.getFirstName()).isEqualTo("John");
        Assertions.assertThat(response.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(response.getDob()).isEqualTo(dob);
    }

    @Test
    @WithMockUser(username = "user")
    void getMyInfo_userNotFound_fail() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1006);
    }
}
