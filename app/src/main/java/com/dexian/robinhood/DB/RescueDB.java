package com.dexian.robinhood.DB;

public class RescueDB {

    long ID;
    String name;
    String Phone;
    String Details;
    String PictureName;
    String Time;
    String Location;
    String Area;
    String Status;
    String IP;

    public RescueDB() {
    }

    public RescueDB(long ID, String name, String phone, String details, String pictureName, String time, String location, String area, String status, String IP) {
        this.ID = ID;
        this.name = name;
        Phone = phone;
        Details = details;
        PictureName = pictureName;
        Time = time;
        Location = location;
        Area = area;
        Status = status;
        this.IP = IP;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getPictureName() {
        return PictureName;
    }

    public void setPictureName(String pictureName) {
        PictureName = pictureName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
