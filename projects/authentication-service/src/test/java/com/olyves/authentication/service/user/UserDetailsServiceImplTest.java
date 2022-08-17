package com.olyves.authentication.service.user;

import com.olyves.authentication.dao.UserRepository;
import com.olyves.onboarding.common.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {
    @Autowired
    UserDetailsServiceImpl underTest;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void TestLoadUserByEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail("dudu@olyves");
        user.setPassword("qwerty");

        //when
        when( userRepository.findByEmail(user.getEmail())).
                thenReturn(Optional.of(user));
        //then
        assertEquals(user.getEmail(),"dudu@olyves");
    }
    @Test
    void TestExceptionThrown(){
        //when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            throw new UsernameNotFoundException("a message");
        });
        //then
        assertEquals("a message", exception.getMessage());
    }
}