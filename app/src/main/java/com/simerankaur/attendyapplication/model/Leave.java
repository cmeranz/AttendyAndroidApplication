package com.simerankaur.attendyapplication.model;

import java.io.Serializable;

public class Leave  implements Serializable {
    public Leave(String leaveId,
                 String name,
                 String leaveType,
                 String leaveDate,
                 String leaveReason,
                 String status,
                 String document) {
        this.leaveId=leaveId;
        this.name=name;
        this.leaveTitle = leaveType;
        this.leaveDate = leaveDate;
        this.status = status;
        this.leaveReason=leaveReason;
        this.document=document;
    }

    public String getLeaveTitle() {
        return leaveTitle;
    }

    public void setLeaveTitle(String leaveTitle) {
        this.leaveTitle = leaveTitle;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String leaveTitle;
    String leaveDate;

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    String leaveReason;
    String status;



    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    String document;

    public String getName() {
        return name;
    }

    String name;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    String leaveId;
}
