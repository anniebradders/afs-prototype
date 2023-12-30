package com.afs.visaApplication.restImpl;

import com.afs.visaApplication.POJO.Application;
import com.afs.visaApplication.constants.VisaConstants;
import com.afs.visaApplication.rest.ApplicationRest;
import com.afs.visaApplication.service.ApplicationService;
import com.afs.visaApplication.utils.VisaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ApplicationRestImpl implements ApplicationRest {

    @Autowired
    ApplicationService applicationService;

    @Override
    public ResponseEntity<String> generateApplication(Map<String, Object> requestMap) {
        try{
            return applicationService.generateApplication(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Application>> getApps() {
        try{
            return applicationService.getApps();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
