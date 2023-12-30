package com.afs.visaApplication.service;

import com.afs.visaApplication.POJO.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ApplicationService {
    ResponseEntity<String> generateApplication(Map<String, Object> requestMap);

    ResponseEntity<List<Application>> getApps();
}
