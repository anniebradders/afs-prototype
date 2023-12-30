package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.JWT.JwtFilter;
import com.afs.visaApplication.POJO.Application;
import com.afs.visaApplication.POJO.Visa;
import com.afs.visaApplication.constants.VisaConstants;
import com.afs.visaApplication.dao.ApplicationDao;
import com.afs.visaApplication.service.ApplicationService;
import com.afs.visaApplication.utils.VisaUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ApplicationDao applicationDao;
    @Override
    public ResponseEntity<String> generateApplication(Map<String, Object> requestMap){
        try{
            String time;
            if(validateRequestMap(requestMap)){
                time = VisaUtils.getUUID();
                createApplication(time, requestMap);
                return new ResponseEntity<>("{\"uuid\":\"" + requestMap + "\"", HttpStatus.OK );
            }
            return VisaUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void createApplication(String time, Map<String, Object> requestMap) {
        try{
            Application application = new Application();
            application.setVisaDetails((String) requestMap.get("visadetails"));
            if(checkStatus(application.getVisaDetails())) {
                application.setUuid(time);
                application.setName((String) requestMap.get("name"));
                application.setNationality((String) requestMap.get("nationality"));
                application.setDestination((String) requestMap.get("destination"));
                application.setBiometric(getBiometric(application.getVisaDetails()));
                application.setDocumentation(Boolean.parseBoolean((String) requestMap.get("documentation")));
                application.setCreatedBy(jwtFilter.getCurrentUser());
                applicationDao.save(application);
            }else{
                VisaUtils.getResponseEntity("This Visa is not currently available", HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("nationality") &&
                requestMap.containsKey("visadetails") &&
                requestMap.containsKey("destination") &&
                requestMap.containsKey("documentation");
    }

    private String getBiometric(String details) throws JSONException, IOException {
        Gson gson = new Gson();
        Visa visa = gson.fromJson(details, Visa.class);
        String biometric = visa.getBiometric();
        if(biometric.equalsIgnoreCase("true")){
            return "false";
        }else{
            return "N/A";
        }
    }

    private boolean checkStatus(String details){
        Gson gson = new Gson();
        Visa visa = gson.fromJson(details, Visa.class);
        String status = visa.getStatus();
        if(status.equalsIgnoreCase("true")){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ResponseEntity<List<Application>> getApps() {
        List<Application> list = new ArrayList<>();
        if(jwtFilter.isBranchOfficial()){
            list = applicationDao.getAllApps();
        }else{
            list = applicationDao.getAppByEmail(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
