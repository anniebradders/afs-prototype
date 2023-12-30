package com.afs.visaApplication.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Visa.getAllVisa", query= "select new com.afs.visaApplication.wrapper.VisaWrapper(v.id, v.name, v.biometric, v.status) from Visa v")

@NamedQuery(name = "Visa.updateVisaStatus", query = "update Visa v set v.status=:status where v.id=:id")

@NamedQuery(name = "Visa.getVisaById", query = "select new com.afs.visaApplication.wrapper.VisaWrapper(v.id, v.name, v.biometric, v.status) from Visa v where v.id=:id")


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "visa")
public class Visa implements Serializable {

    public static final Long serialVersionUid = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column (name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "biometric")
    private String biometric;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiometric() {
        return biometric;
    }

    public void setBiometric(String biometric) {
        this.biometric = biometric;
    }
}
