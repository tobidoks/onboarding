package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.dao.UserRoleRepository;
import com.olyves.authentication.exception.AuthenticationException;
import com.olyves.authentication.payload.request.SignUpRequest;
import com.olyves.authentication.util.JwtUtils;
import com.olyves.onboarding.common.model.User;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationManagerTest {

    private UserAuthenticationManager underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    private String email;
    private User user;

    @BeforeEach
    public void setUp() {

        underTest = new UserAuthenticationManager(authenticationManager, jwtUtils, userRepository, roleRepository, passwordEncoder);

        email = "love@damini.com";
    }

    @Test
    public void shouldAuthenticateUserOnSignUp() {

        if (userRepository.existsByEmail(email)) {
            Assert.hasText(String.format("lova@damini.com", email));
        }





        }

}
