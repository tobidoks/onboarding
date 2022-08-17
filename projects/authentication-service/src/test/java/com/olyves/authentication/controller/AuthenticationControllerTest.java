package com.olyves.authentication.controller;

import com.olyves.authentication.service.user.UserDetailsServiceImpl;
import com.olyves.onboarding.common.model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootApplication
@Transactional
@WebAppConfiguration
class AuthenticationControllerTest {
    @InjectMocks
    AuthenticationController   underTest;

    private MockMvc mockMvc;

    @Mock
    UserDetailsServiceImpl userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();

    }
    @Test
    void shouldCreateMockMvc(){
        assertNotNull(mockMvc);
    }

    @Test
    void shouldTestUserSignUp() throws Exception {
        User user = new User();
        user.setEmail("karishika@olyves");

        //when
        this.mockMvc.perform(post("/api/v1/auth").contentType(MediaType.APPLICATION_JSON).content("{\"username\": \"duke\", \"email\": \"duke@olyves\"}")).
                andExpect(MockMvcResultMatchers.status().isCreated()).
                andExpect(MockMvcResultMatchers.header().exists("Location")).
                andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("duke")));

        //then
        //PageNotFound Error, Expected:201, Actual:404.
        verify(userService).loadUserByUsername("karishika@olyves");


    }
}