package com.afs.visaApplication.dao;

import com.afs.visaApplication.POJO.Visa;
import com.afs.visaApplication.wrapper.VisaWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface VisaDao extends JpaRepository<Visa, Integer> {
    List<VisaWrapper> getAllVisa();

    @Modifying
    @Transactional
    Integer updateVisaStatus(@Param("status") String status, @Param("id") Integer id);

    VisaWrapper getVisaById(@Param("id") Integer id);
}
