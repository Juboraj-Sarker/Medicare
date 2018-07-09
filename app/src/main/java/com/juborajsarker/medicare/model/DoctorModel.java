package com.juborajsarker.medicare.model;

public class DoctorModel {

    int id;
    String name;
    String phoneNumber;
    String speciality;
    String address;
    String chamber;
    String email;

    public DoctorModel(int id, String name, String phoneNumber, String speciality, String address, String chamber, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.speciality = speciality;
        this.address = address;
        this.chamber = chamber;
        this.email = email;
    }

    public DoctorModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
