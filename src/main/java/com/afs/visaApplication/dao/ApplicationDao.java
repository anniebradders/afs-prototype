package com.afs.visaApplication.dao;

import com.afs.visaApplication.POJO.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationDao extends JpaRepository<Application, Integer> {

    List<Application> getAllApps();

    List<Application> getAppByEmail(@Param("email") String email);

}
