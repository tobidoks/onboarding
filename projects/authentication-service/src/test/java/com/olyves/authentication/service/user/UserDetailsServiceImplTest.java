package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;


import com.olyves.authentication.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    private UserDetailsServiceImpl underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    private String email;
    private User user;

    @BeforeEach
    public void setUp() {
        underTest = new UserDetailsServiceImpl(userRepository, jwtUtils);
        email = "bros@jay.com";




}









}
