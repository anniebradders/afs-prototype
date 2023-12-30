package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.JWT.JwtFilter;
import com.afs.visaApplication.POJO.User;
import com.afs.visaApplication.POJO.Visa;
import com.afs.visaApplication.constants.VisaConstants;
import com.afs.visaApplication.dao.VisaDao;
import com.afs.visaApplication.service.VisaService;
import com.afs.visaApplication.utils.VisaUtils;
import com.afs.visaApplication.wrapper.VisaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VisaServiceImpl implements VisaService {

    @Autowired
    VisaDao visaDao;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewVisa(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isBranchOfficial()) {
                if (validateVisaMap(requestMap, false)) {
                    visaDao.save(getVisaFromMap(requestMap, false));
                    return VisaUtils.getResponseEntity("Visa Added Sucessfully", HttpStatus.OK);
                }
                return VisaUtils.getResponseEntity(VisaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return VisaUtils.getResponseEntity(VisaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateVisaMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Visa getVisaFromMap(Map<String, String> requestMap, boolean isAdd) {
        Visa visa = new Visa();
        if(isAdd){
            visa.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            visa.setStatus("true");
        }
        visa.setName(requestMap.get("name"));
        visa.setBiometric(requestMap.get("biometric"));
        return visa;
    }

    @Override
    public ResponseEntity<List<VisaWrapper>> getAllVisa() {
        try{
            return new ResponseEntity<>(visaDao.getAllVisa(), HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateVisa(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isBranchOfficial()){
                if(validateVisaMap(requestMap, true)){
                    Optional<Visa> optional = visaDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        Visa visa = getVisaFromMap(requestMap, true);
                        visa.setStatus(optional.get().getStatus());
                        visaDao.save(visa);
                        return VisaUtils.getResponseEntity("Visa Updated", HttpStatus.OK);
                    }else{
                        return VisaUtils.getResponseEntity("Visa id does not exist", HttpStatus.OK);
                    }
                }else{
                    return VisaUtils.getResponseEntity(VisaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            }else{
                return VisaUtils.getResponseEntity(VisaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteVisa(Integer id) {
        try{
            if(jwtFilter.isBranchOfficial()){
                Optional optional = visaDao.findById(id);
                if(!optional.isEmpty()){
                    visaDao.deleteById(id);
                    return VisaUtils.getResponseEntity("Visa deleted", HttpStatus.OK);
                }
                return VisaUtils.getResponseEntity("Visa id does not exist", HttpStatus.OK);
            }else{
                return VisaUtils.getResponseEntity(VisaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isBranchOfficial()){
                Optional optional = visaDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                    visaDao.updateVisaStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    return VisaUtils.getResponseEntity("Visa status updated", HttpStatus.OK);
                }
                return VisaUtils.getResponseEntity("Visa id does not exist", HttpStatus.OK);
            }else{
                return VisaUtils.getResponseEntity(VisaConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch( Exception ex){
            ex.printStackTrace();
        }
        return VisaUtils.getResponseEntity(VisaConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<VisaWrapper> getVisaById(Integer id) {
        try{
            return new ResponseEntity<>(visaDao.getVisaById(id), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new VisaWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    Visa getVisaFromMapForTest(Map<String, String> requestMap, boolean isAdd) {
        return getVisaFromMap(requestMap, isAdd);
    }

    boolean validateVisaMapForTest(Map<String, String> requestMap, boolean validateId) {
        return validateVisaMap(requestMap, validateId);
    }
}
