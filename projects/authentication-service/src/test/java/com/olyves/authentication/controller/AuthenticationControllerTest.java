package com.olyves.authentication.controller;

import com.olyves.authentication.payload.request.SignUpRequest;
import com.olyves.authentication.service.jwt.AuthenticationEntryPointJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    AuthenticationEntryPointJwt authenticationEntryPointJwt;

    private AuthenticationController underTest;

    @BeforeEach
    void setup() {
        underTest = new AuthenticationController();
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
    }

    @Test
    void getAccount() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder().build();
        mockMvc.perform(post("/sign-up").content(signUpRequest.toString())).andExpect(status().isOk());
    }

}
