package com.example.identity_service.controller;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;

    private UserResponse userResponse;

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

    }

    @Test
        // Create User - Success
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")
                        .value("6c8910dd72a9"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username")
                        .value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName")
                        .value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName")
                        .value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.dob")
                        .value(dob.toString())
                );
    }

    @Test
        // Create User - Success
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setUsername("use");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 4 characters long")
                );

    }

}
