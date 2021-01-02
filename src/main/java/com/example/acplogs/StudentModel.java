package com.example.acplogs;

public class StudentModel {

    private String name,year,userClass, email, contact, profile, userId, agency_name,
            certificate, work_desc,added_on , feedback, date, checkIn, checkOut ;

    private int work_hours;



    public StudentModel(){

    }

    public StudentModel(String name, String year, String userClass, String email, String contact,
                        String profile, String userId, String agency_name,
                        String certificate, String work_desc, String added_on,
                        String feedback, String date, String checkIn, String checkOut, int work_hours) {
        this.name = name;
        this.year = year;
        this.userClass = userClass;
        this.email = email;
        this.contact = contact;
        this.profile = profile;
        this.userId = userId;
        this.agency_name = agency_name;
        this.certificate = certificate;
        this.work_desc = work_desc;
        this.added_on = added_on;
        this.feedback = feedback;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.work_hours = work_hours;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getWork_desc() {
        return work_desc;
    }

    public void setWork_desc(String work_desc) {
        this.work_desc = work_desc;
    }

    public String getAdded_on() {
        return added_on;
    }

    public void setAdded_on(String added_on) {
        this.added_on = added_on;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWork_hours() {
        return work_hours;
    }

    public void setWork_hours(int work_hours) {
        this.work_hours = work_hours;
    }
}