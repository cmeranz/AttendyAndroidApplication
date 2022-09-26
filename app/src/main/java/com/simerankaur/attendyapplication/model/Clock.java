package com.simerankaur.attendyapplication.model;

public class Clock {
    private String date;

    public Clock(String date, String clockIn, String clockOut) {
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
    }

    private String clockIn;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }

    private String clockOut;
}

