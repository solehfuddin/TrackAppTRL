package com.sofudev.trackapptrl.Data;

public class Data_sp_history {
    private String tipesp, dateout, status, approve, reject, durationunit, approvalName;

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public String getTipesp() {
        return tipesp;
    }

    public void setTipesp(String tipesp) {
        this.tipesp = tipesp;
    }

    public String getDateout() {
        return dateout;
    }

    public void setDateout(String dateout) {
        this.dateout = dateout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public String getDurationunit() {
        return durationunit;
    }

    public void setDurationunit(String durationunit) {
        this.durationunit = durationunit;
    }
}
