package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.JWT.JwtFilter;
import com.afs.visaApplication.POJO.Application;
import com.afs.visaApplication.dao.ApplicationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private ApplicationDao applicationDao;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateApplicationSuccess() {
        // Arrange
        when(jwtFilter.getCurrentUser()).thenReturn("user1");
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("name", "John Doe");
        requestMap.put("nationality", "US");
        requestMap.put("visadetails", "{\"status\":\"true\", \"biometric\":\"false\"}");
        requestMap.put("destination", "Europe");
        requestMap.put("documentation", "true");

        // Act
        ResponseEntity<String> responseEntity = applicationService.generateApplication(requestMap);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(applicationDao, times(1)).save(any(Application.class));
    }

    @Test
    void testGenerateApplicationBadRequest() {
        // Arrange
        Map<String, Object> requestMap = new HashMap<>();

        // Act
        ResponseEntity<String> responseEntity = applicationService.generateApplication(requestMap);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(applicationDao, never()).save(any(Application.class));
    }

    @Test
    void testGetAppsBranchOfficial() {
        // Arrange
        when(jwtFilter.isBranchOfficial()).thenReturn(true);
        when(applicationDao.getAllApps()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<Application>> responseEntity = applicationService.getApps();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(applicationDao, times(1)).getAllApps();
    }

    @Test
    void testGetAppsNotBranchOfficial() {
        // Arrange
        when(jwtFilter.isBranchOfficial()).thenReturn(false);
        when(jwtFilter.getCurrentUser()).thenReturn("user1");
        when(applicationDao.getAppByEmail("user1")).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<Application>> responseEntity = applicationService.getApps();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(applicationDao, times(1)).getAppByEmail("user1");
    }

}