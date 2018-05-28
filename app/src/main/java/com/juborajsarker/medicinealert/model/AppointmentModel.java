package com.juborajsarker.medicinealert.model;

public class AppointmentModel {

    int id;
    String appointmentTitle;
    String doctorName;
    String doctorSpeciality;
    String date;
    String time;
    String rememberBefore;
    String location;
    String notes;


    public AppointmentModel(int id, String appointmentTitle, String doctorName, String doctorSpeciality,
                            String date, String time, String rememberBefore, String location, String notes) {

        this.id = id;
        this.appointmentTitle = appointmentTitle;
        this.doctorName = doctorName;
        this.doctorSpeciality = doctorSpeciality;
        this.date = date;
        this.time = time;
        this.rememberBefore = rememberBefore;
        this.location = location;
        this.notes = notes;

    }

    public AppointmentModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRememberBefore() {
        return rememberBefore;
    }

    public void setRememberBefore(String rememberBefore) {
        this.rememberBefore = rememberBefore;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
