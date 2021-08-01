package com.example.establishmentandroid;

public class Location_Model {
    private String Location;
    private String Date;
    private String TimeIn;
    private String TimeOut;
    private Boolean Status;

    public Location_Model(){

    }



    public Location_Model(String Location, String Date, String TimeIn, String TimeOut, Boolean Status){
        this.Location = Location;
        this.Date = Date;
        this.TimeIn = TimeIn;
        this.TimeOut = TimeOut;
        this.Status = Status;

    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTimeIn() {
        return TimeIn;
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }
}
