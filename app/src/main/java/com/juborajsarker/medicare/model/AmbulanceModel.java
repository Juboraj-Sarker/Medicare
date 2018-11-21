package com.juborajsarker.medicare.model;

public class AmbulanceModel {

    String title;
    String address;
    String phone;

    public AmbulanceModel() {

    }

    public AmbulanceModel(String title, String address, String phone) {
        this.title = title;
        this.address = address;
        this.phone = phone;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
