package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.JWT.JwtFilter;
import com.afs.visaApplication.POJO.Visa;
import com.afs.visaApplication.dao.VisaDao;
import com.afs.visaApplication.wrapper.VisaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VisaServiceImplTest {

    @Mock
    private VisaDao visaDao;

    @Mock
    private JwtFilter jwtFilter;

    @InjectMocks
    private VisaServiceImpl visaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddNewVisa() {
        // Mocking jwtFilter
        when(jwtFilter.isBranchOfficial()).thenReturn(true);

        // Mocking visaDao behavior
        when(visaDao.save(any())).thenReturn(null);

        // Test data
        Map<String, String> requestMap = Collections.singletonMap("name", "TestVisa");

        // Perform the test
        ResponseEntity<String> response = visaService.addNewVisa(requestMap);

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Visa Added Sucessfully\"}", response.getBody());
    }

    @Test
    void testGetAllVisa() {
        // Mocking visaDao behavior
        when(visaDao.getAllVisa()).thenReturn(Collections.emptyList());

        // Perform the test
        ResponseEntity<List<VisaWrapper>> response = visaService.getAllVisa();

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testUpdateStatus() {
        // Mocking jwtFilter
        when(jwtFilter.isBranchOfficial()).thenReturn(true);

        // Mocking visaDao behavior
        when(visaDao.updateVisaStatus(anyString(), anyInt())).thenReturn(1); // Assuming 1 is returned on success

        // Test data
        Map<String, String> requestMap = Map.of("id", "1", "status", "ACTIVE");

        // Perform the test
        ResponseEntity<String> response = visaService.updateStatus(requestMap);

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Visa id does not exist\"}", response.getBody());
    }

    @Test
    void testDeleteVisa() {
        // Mocking jwtFilter
        when(jwtFilter.isBranchOfficial()).thenReturn(true);

        // Mocking visaDao behavior
        when(visaDao.findById(anyInt())).thenReturn(Optional.of(new Visa()));

        // Perform the test
        ResponseEntity<String> response = visaService.deleteVisa(1);

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Visa deleted\"}", response.getBody());

        // Verify that deleteById method is called
        verify(visaDao, times(1)).deleteById(1);
    }

    @Test
    void testGetVisaById() {
        // Mocking visaDao behavior
        when(visaDao.getVisaById(anyInt())).thenReturn(new VisaWrapper());

        // Perform the test
        ResponseEntity<VisaWrapper> response = visaService.getVisaById(1);

        // Assert the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify that getVisaById method is called
        verify(visaDao, times(1)).getVisaById(1);
    }

    void testGetVisaFromMap() {
        // Test data
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("id", "1");
        requestMap.put("name", "TestVisa");

        // Perform the test
        Visa visa = visaService.getVisaFromMapForTest(requestMap, true);

        // Assert the result
        assertEquals(1, visa.getId());
        assertEquals("TestVisa", visa.getName());
        assertNull(visa.getBiometric()); // Assuming biometric is not present in the map
        assertNull(visa.getStatus()); // Assuming status is not present in the map
    }

    @Test
    void testValidateVisaMap() {
        // Test data
        Map<String, String> validRequestMap = new HashMap<>();
        validRequestMap.put("name", "TestVisa");

        Map<String, String> invalidRequestMap = new HashMap<>();
        invalidRequestMap.put("id", "1"); // Missing "name"

        // Perform the tests
        assertTrue(visaService.validateVisaMapForTest(validRequestMap, false));
        assertFalse(visaService.validateVisaMapForTest(invalidRequestMap, true));
        assertFalse(visaService.validateVisaMapForTest(invalidRequestMap, false));
    }
}