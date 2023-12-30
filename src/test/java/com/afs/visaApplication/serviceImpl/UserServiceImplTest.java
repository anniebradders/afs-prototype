package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.JWT.JwtFilter;
import com.afs.visaApplication.JWT.JwtUtil;
import com.afs.visaApplication.JWT.UsersDetailsService;
import com.afs.visaApplication.POJO.User;
import com.afs.visaApplication.constants.VisaConstants;
import com.afs.visaApplication.dao.UserDao;
import com.afs.visaApplication.utils.EmailUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {


    @Mock
    private UserDao userDao;

    @Mock
    private UsersDetailsService usersDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private EmailUtils emailUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUpSuccess() {
        // Mock data
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "TestUser");
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password123");

        // Mock behavior
        when(userDao.findByEmailId("test@example.com")).thenReturn(null);
        when(userDao.save(any())).thenReturn(null);

        // Perform the test
        ResponseEntity<String> response = userService.signUp(requestMap);

        // Verify behavior
        verify(userDao, times(1)).findByEmailId("test@example.com");
        verify(userDao, times(1)).save(any());

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Registration was successful\"}", response.getBody());
    }

    @Test
    void testSignUpEmailInUse() {
        // Mock data
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "TestUser");
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password123");

        // Mock behavior
        when(userDao.findByEmailId("test@example.com")).thenReturn(new User());

        // Perform the test
        ResponseEntity<String> response = userService.signUp(requestMap);

        // Verify behavior
        verify(userDao, times(1)).findByEmailId("test@example.com");

        // Assert the result
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Email already in use\"}", response.getBody());
    }

    @Test
    void testSignUpInvalidData() {
        // Mock data
        Map<String, String> requestMap = new HashMap<>();

        // Perform the test
        ResponseEntity<String> response = userService.signUp(requestMap);

        // Assert the result
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Invalid Data\"}", response.getBody());
    }

    @Test
    public void testLoginSuccess() {
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password");

        // Mocking authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Mocking user details
        User user = mock(User.class); // Assuming User is the actual class returned by getUserDetail()
        when(usersDetailsService.getUserDetail()).thenReturn(user);
        when(user.getStatus()).thenReturn("true");

        // Mocking JWT token generation
        when(jwtUtil.generateToken(any(), any())).thenReturn("mocked-token");

        // Act
        ResponseEntity<String> response = userService.login(requestMap);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected status code");
        assertEquals("{\"token\":\"mocked-token\"}", response.getBody(), "Unexpected response body");
    }

    @Test
    void testLoginBadCredentials() {
        // Mock data
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "incorrectPassword");

        // Mock behavior
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Perform the test
        ResponseEntity<String> response = userService.login(requestMap);

        // Verify behavior
        verify(authenticationManager, times(1)).authenticate(any());

        // Assert the result
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Bad Credentials\"}", response.getBody());
    }

    @Test
    void testUpdateStatusSuccess() {
        // Mock data
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("id", "1");
        requestMap.put("status", "true");

        // Mock behavior
        when(jwtFilter.isBranchOfficial()).thenReturn(true);
        when(userDao.findById(1)).thenReturn(Optional.of(userService.getMockUser("test@example.com", "user", "false")));

        // Perform the test
        ResponseEntity<String> response = userService.update(requestMap);

        // Verify behavior
        verify(jwtFilter, times(1)).isBranchOfficial();
        verify(userDao, times(1)).findById(1);
        verify(userDao, times(1)).updateStatus("true", 1);
        verify(emailUtils, times(1)).sendSimpleMessage(any(), any(), any(), any());

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"User Status Updated\"}", response.getBody());
    }

    @Test
    void testUpdateStatusUserNotFound() {
        // Mock data
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("id", "1");
        requestMap.put("status", "true");

        // Mock behavior
        when(jwtFilter.isBranchOfficial()).thenReturn(true);
        when(userDao.findById(1)).thenReturn(Optional.empty());

        // Perform the test
        ResponseEntity<String> response = userService.update(requestMap);

        // Verify behavior
        verify(jwtFilter, times(1)).isBranchOfficial();
        verify(userDao, times(1)).findById(1);

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"User id does not exist\"}", response.getBody());
    }

}