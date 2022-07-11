package com.olyves.authentication.util;

import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private JwtUtils underTest;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() {
        underTest = new JwtUtils();
        String secret = RandomStringUtils.randomAlphabetic(100);
        ReflectionTestUtils.setField(underTest, "jwtSecret", secret);
        ReflectionTestUtils.setField(underTest, "jwtExpiration", 5000);
    }

    @Test
    public void shouldGenerateJwtTokenWithUsername() {
        String result = underTest.generateJwtToken("kilometer");
        assertNotNull(result);
    }

    @Test
    public void shouldReturnTrueOnValidJwtToken() {
        String jwtToken = createToken("russlewestbrook");
        boolean result = underTest.validateJwtToken(jwtToken);
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseOnExpiredToken() {
        ReflectionTestUtils.setField(underTest, "jwtExpiration", 1);
        String jwtToken = createToken("jamesharden");
        boolean result = underTest.validateJwtToken(jwtToken);
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseOnEmptyJwtToken() {
        boolean result = underTest.validateJwtToken(StringUtils.EMPTY);
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseRandomStringJwtToken() {
        String jwtToken = RandomStringUtils.randomAlphabetic(50);
        boolean result = underTest.validateJwtToken(jwtToken);
        assertFalse(result);
    }

    @Test
    public void shouldReturnNullIfAuthorizationHeaderIsNotPresent() {
        when(httpServletRequest.getHeader(anyString())).thenReturn(null);
        String result = underTest.extractJwt(httpServletRequest);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullIfAuthorizationHeaderDoesNotStartWithBearer() {
        when(httpServletRequest.getHeader(anyString())).thenReturn("Testing");
        String result = underTest.extractJwt(httpServletRequest);
        assertNull(result);
    }

    @Test
    public void shouldExtractJwtToken() {
        when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer testing");
        String result = underTest.extractJwt(httpServletRequest);
        assertEquals(result, "testing");
    }

    @Test
    public void shouldGetUserNameFromJwtToken() {
        String username = "kevindurant";
        String jwtToken = createToken(username);
        String result = underTest.getUserNameFromJwtToken(jwtToken);
        assertEquals(result, "kevindurant");
    }

    @Test
    public void shouldRaiseExceptionOnInvalidJwtTokenWhenGettingUserName() {
        String jwtToken = RandomStringUtils.randomAlphabetic(50);
        assertThrows(MalformedJwtException.class, () -> {
            underTest.getUserNameFromJwtToken(jwtToken);
        });
    }

    private String createToken(String username) {
        return underTest.generateJwtToken(username);
    }

}
