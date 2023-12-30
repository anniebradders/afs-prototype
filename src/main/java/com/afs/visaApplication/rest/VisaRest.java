package com.afs.visaApplication.rest;


import com.afs.visaApplication.wrapper.VisaWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/visa")
public interface VisaRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewVisa(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<VisaWrapper>> getAllVisa();

    @PostMapping(path = "/update")
    ResponseEntity<String> updateVisa(@RequestBody Map<String, String> requestMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteVisa(@PathVariable Integer id);

    @PostMapping(path = "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<VisaWrapper> getVisaById(@PathVariable Integer id);

}
