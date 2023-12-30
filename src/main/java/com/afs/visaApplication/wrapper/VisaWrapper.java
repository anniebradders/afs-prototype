package com.afs.visaApplication.wrapper;

import com.afs.visaApplication.POJO.User;
import lombok.Data;

@Data
public class VisaWrapper {

    Integer id;
    String name;
    String biometric;
    String status;


    public VisaWrapper(){}

    public VisaWrapper(Integer id, String name, String biometric, String status){
        this.id = id;
        this.name = name;
        this.biometric = biometric;
        this.status = status;
    }

}
