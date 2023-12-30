package com.afs.visaApplication.service;

import com.afs.visaApplication.wrapper.VisaWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VisaService {

    ResponseEntity<String> addNewVisa(Map<String,String> requestMap);

    ResponseEntity<List<VisaWrapper>> getAllVisa();

    ResponseEntity<String> updateVisa(Map<String,String> requestMap);

    ResponseEntity<String> deleteVisa(Integer id);

    ResponseEntity<String> updateStatus(Map<String,String> requestMap);

    ResponseEntity<VisaWrapper> getVisaById(Integer id);

}
