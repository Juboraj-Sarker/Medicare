package com.juborajsarker.medicinealert.model;

public class MedicineModel {

    int id;
    String date;
    String medicineName;
    String imagePath;
    int numberOfSlot;
    String firstSlotTime;
    String secondSlotTime;
    String thirdSlotTime;
    int numberOfDays;
    boolean isEveryday;
    boolean isSpecificDaysOfWeek;
    boolean isDaysInterval;
    String daysNameOfWeek;
    int daysInterval;
    String startDate;
    String status;

    public MedicineModel(int id, String date, String medicineName, String imagePath, int numberOfSlot, String firstSlotTime,
                         String secondSlotTime, String thirdSlotTime, int numberOfDays, boolean isEveryday,
                         boolean isSpecificDaysOfWeek, boolean isDaysInterval, String daysNameOfWeek, int daysInterval,
                         String startDate, String status) {

        this.id = id;
        this.date = date;
        this.medicineName = medicineName;
        this.imagePath = imagePath;
        this.numberOfSlot = numberOfSlot;
        this.firstSlotTime = firstSlotTime;
        this.secondSlotTime = secondSlotTime;
        this.thirdSlotTime = thirdSlotTime;
        this.numberOfDays = numberOfDays;
        this.isEveryday = isEveryday;
        this.isSpecificDaysOfWeek = isSpecificDaysOfWeek;
        this.isDaysInterval = isDaysInterval;
        this.daysNameOfWeek = daysNameOfWeek;
        this.daysInterval = daysInterval;
        this.startDate = startDate;
        this.status = status;
    }


    public MedicineModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(int numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public String getFirstSlotTime() {
        return firstSlotTime;
    }

    public void setFirstSlotTime(String firstSlotTime) {
        this.firstSlotTime = firstSlotTime;
    }

    public String getSecondSlotTime() {
        return secondSlotTime;
    }

    public void setSecondSlotTime(String secondSlotTime) {
        this.secondSlotTime = secondSlotTime;
    }

    public String getThirdSlotTime() {
        return thirdSlotTime;
    }

    public void setThirdSlotTime(String thirdSlotTime) {
        this.thirdSlotTime = thirdSlotTime;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public boolean isEveryday() {
        return isEveryday;
    }

    public void setEveryday(boolean everyday) {
        isEveryday = everyday;
    }

    public boolean isSpecificDaysOfWeek() {
        return isSpecificDaysOfWeek;
    }

    public void setSpecificDaysOfWeek(boolean specificDaysOfWeek) {
        isSpecificDaysOfWeek = specificDaysOfWeek;
    }

    public boolean isDaysInterval() {
        return isDaysInterval;
    }

    public void setDaysInterval(boolean daysInterval) {
        isDaysInterval = daysInterval;
    }

    public String getDaysNameOfWeek() {
        return daysNameOfWeek;
    }

    public void setDaysNameOfWeek(String daysNameOfWeek) {
        this.daysNameOfWeek = daysNameOfWeek;
    }

    public int getDaysInterval() {
        return daysInterval;
    }

    public void setDaysInterval(int daysInterval) {
        this.daysInterval = daysInterval;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
