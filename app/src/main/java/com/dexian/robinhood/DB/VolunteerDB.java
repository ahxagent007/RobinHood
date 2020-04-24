package com.dexian.robinhood.DB;

public class VolunteerDB {

    long ID;
    String name;
    String FatherName;
    String MotherName;
    String NID;
    String EDU;
    String WhyWork;
    String Pets;
    String Help;
    String Reff;
    String Phone;
    String Status = "PENDING";

    public VolunteerDB(long ID, String name, String fatherName, String motherName, String NID, String EDU, String whyWork, String pets, String help, String reff, String phone) {
        this.ID = ID;
        this.name = name;
        FatherName = fatherName;
        MotherName = motherName;
        this.NID = NID;
        this.EDU = EDU;
        WhyWork = whyWork;
        Pets = pets;
        Help = help;
        Reff = reff;
        Phone = phone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public VolunteerDB() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }

    public String getEDU() {
        return EDU;
    }

    public void setEDU(String EDU) {
        this.EDU = EDU;
    }

    public String getWhyWork() {
        return WhyWork;
    }

    public void setWhyWork(String whyWork) {
        WhyWork = whyWork;
    }

    public String getPets() {
        return Pets;
    }

    public void setPets(String pets) {
        Pets = pets;
    }

    public String getHelp() {
        return Help;
    }

    public void setHelp(String help) {
        Help = help;
    }

    public String getReff() {
        return Reff;
    }

    public void setReff(String reff) {
        Reff = reff;
    }
}
