package com.afs.visaApplication.rest;

import com.afs.visaApplication.POJO.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/application")
public interface ApplicationRest {

    @PostMapping(path = "/App")
    ResponseEntity<String> generateApplication(@RequestBody Map<String, Object> requestMap);

    @GetMapping(path = "/getApplications")
    ResponseEntity<List<Application>> getApps();





}
