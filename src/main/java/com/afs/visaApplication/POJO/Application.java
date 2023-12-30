package com.afs.visaApplication.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Application.getAllApps", query = "select a from Application a order by a.id desc")

@NamedQuery(name = "Application.getAppByEmail", query = "select a from Application a where a.createdBy=:email order by a.id desc")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "application")
public class Application implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "visadetails", columnDefinition = "json")
    private String visaDetails;

    @Column(name = "destination")
    private String destination;

    @Column(name = "biometric")
    private String biometric;

    @Column(name = "documentation")
    private boolean documentation;

    @Column(name = "createdBy")
    private String createdBy;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getVisaDetails() {
        return visaDetails;
    }

    public void setVisaDetails(String visaDetails) {
        this.visaDetails = visaDetails;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBiometric() {
        return biometric;
    }

    public void setBiometric(String biometric) {
        this.biometric = biometric;
    }

    public boolean isDocumentation() {
        return documentation;
    }

    public void setDocumentation(boolean documentation) {
        this.documentation = documentation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


}
