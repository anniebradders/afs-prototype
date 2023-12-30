package com.afs.visaApplication.restImpl;

import com.afs.visaApplication.POJO.Visa;
import com.afs.visaApplication.constants.VisaConstants;
import com.afs.visaApplication.rest.VisaRest;
import com.afs.visaApplication.service.VisaService;
import com.afs.visaApplication.utils.VisaUtils;
import com.afs.visaApplication.wrapper.VisaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class VisaRestImpl implements VisaRest {

    @Autowired
    VisaService visaService;

    @Override
    public ResponseEntity<String> addNewVisa(Map<String, String> requestMap) {
        try{
            return visaService.addNewVisa(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VisaWrapper>> getAllVisa() {
        try{
            return visaService.getAllVisa();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateVisa(Map<String, String> requestMap) {
        try{
            return visaService.updateVisa(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteVisa(Integer id) {
        try{
            return visaService.deleteVisa(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            return visaService.updateStatus(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<VisaWrapper> getVisaById(Integer id) {
        try{
            return visaService.getVisaById(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new VisaWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
