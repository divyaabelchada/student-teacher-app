package com.example.acplogs;

public class AgencyModel {

    String agency_name, agencyTime,agencyLocation,agencyDesc,
            agencyContact,agencyAddress, agencyProfile, map_link,agency_added_on, updated_on, userFeedback, userName;

    public AgencyModel(){

    }

    public AgencyModel(String agency_name, String agencyTime, String agencyLocation,
                       String agencyDesc, String agencyContact, String agencyAddress,
                       String agencyProfile, String map_link, String agency_added_on,
                       String updated_on, String userFeedback, String userName) {
        this.agency_name = agency_name;
        this.agencyTime = agencyTime;
        this.agencyLocation = agencyLocation;
        this.agencyDesc = agencyDesc;
        this.agencyContact = agencyContact;
        this.agencyAddress = agencyAddress;
        this.agencyProfile = agencyProfile;
        this.map_link = map_link;
        this.agency_added_on = agency_added_on;
        this.updated_on = updated_on;
        this.userFeedback = userFeedback;
        this.userName = userName;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getAgencyTime() {
        return agencyTime;
    }

    public void setAgencyTime(String agencyTime) {
        this.agencyTime = agencyTime;
    }

    public String getAgencyLocation() {
        return agencyLocation;
    }

    public void setAgencyLocation(String agencyLocation) {
        this.agencyLocation = agencyLocation;
    }

    public String getAgencyDesc() {
        return agencyDesc;
    }

    public void setAgencyDesc(String agencyDesc) {
        this.agencyDesc = agencyDesc;
    }

    public String getAgencyContact() {
        return agencyContact;
    }

    public void setAgencyContact(String agencyContact) {
        this.agencyContact = agencyContact;
    }

    public String getAgencyAddress() {
        return agencyAddress;
    }

    public void setAgencyAddress(String agencyAddress) {
        this.agencyAddress = agencyAddress;
    }

    public String getAgencyProfile() {
        return agencyProfile;
    }

    public void setAgencyProfile(String agencyProfile) {
        this.agencyProfile = agencyProfile;
    }

    public String getMap_link() {
        return map_link;
    }

    public void setMap_link(String map_link) {
        this.map_link = map_link;
    }

    public String getAgency_added_on() {
        return agency_added_on;
    }

    public void setAgency_added_on(String agency_added_on) {
        this.agency_added_on = agency_added_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
