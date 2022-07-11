package com.olyves.authentication.util;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.authentication.service.user.UserDetailsImpl;
import com.olyves.onboarding.common.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTest {
    private UserUtils underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private String email;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        email = "burnaboy@spaceship.com";
        underTest = new UserUtils(userRepository);

        UserDetailsImpl userDetails = createUserDetails(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
    }

    @Test
    public void shouldGetUser() {
        //Arrange
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        //Act
        User result = underTest.getUser();
        //Assert
        assertEquals(result, user);
    }

    @Test
    public void shouldReturnNullOnNullUser() {
        //Arrange
        when(userRepository.findByEmail(email)).thenReturn(null);
        //Act
        User result = underTest.getUser();
        //Assert
        assertNull(result);
    }

    private UserDetailsImpl createUserDetails(String email) {
        return new UserDetailsImpl(null, email, null, null);
    }

}
