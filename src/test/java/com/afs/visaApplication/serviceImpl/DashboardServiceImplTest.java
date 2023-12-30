package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.dao.ApplicationDao;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceImplTest {

    private ApplicationDao applicationDao = mock(ApplicationDao.class);
    private DashboardServiceImpl dashboardService = new DashboardServiceImpl(applicationDao);

    @Test
    public void testGetCount() {
        // Arrange
        when(applicationDao.count()).thenReturn(42L); // Mocking the count() method

        // Act
        ResponseEntity<Map<String, Object>> responseEntity = dashboardService.getCount();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Unexpected status code");

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("application", 42L);

        assertEquals(expectedMap, responseEntity.getBody(), "Unexpected response body");

        // Verify that the count() method was called once
        verify(applicationDao, times(1)).count();
    }
}