package com.afs.visaApplication.serviceImpl;

import com.afs.visaApplication.dao.ApplicationDao;
import com.afs.visaApplication.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    ApplicationDao applicationDao;

    @Autowired
    public DashboardServiceImpl(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("application", applicationDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
