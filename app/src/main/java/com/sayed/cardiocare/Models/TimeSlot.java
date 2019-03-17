package com.sayed.cardiocare.Models;

/*
                "timeSlot": "18:00:00",
                "isBooked": false,
                "serialNo": 1
*/

public class TimeSlot {
    public String timeSlot;
    public String isBooked;
    public String serialNo;

    public TimeSlot(String timeSlot, String isBooked, String serialNo) {
        this.timeSlot = timeSlot;
        this.isBooked = isBooked;
        this.serialNo = serialNo;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setIsBooked(String isBooked) {
        this.isBooked = isBooked;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getIsBooked() {
        return isBooked;
    }

    public String getSerialNo() {
        return serialNo;
    }
}
