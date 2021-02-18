package com.mediscreen.records.model;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity
@Table(name = "patients")
public class PatientModel {

    private Integer id;

    private String givenName;

    private String familyName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate birthdate;

    private String gender;

    private AddressModel address;

    private String emailAddress;

    private String phoneNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}