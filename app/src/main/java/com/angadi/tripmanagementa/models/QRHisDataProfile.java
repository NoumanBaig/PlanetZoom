package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QRHisDataProfile {
    @SerializedName("sraa_id")
    @Expose
    private String sraaId;
    @SerializedName("sraa_first_name")
    @Expose
    private String sraaFirstName;
    @SerializedName("sraa_login_id")
    @Expose
    private String sraaLoginId;
    @SerializedName("sraa_uid")
    @Expose
    private String sraaUid;
    @SerializedName("sraa_whose_uid")
    @Expose
    private String sraaWhoseUid;
    @SerializedName("sraa_qr_id")
    @Expose
    private String sraaQrId;
    @SerializedName("sraa_time")
    @Expose
    private String sraaTime;
    @SerializedName("sraa_time_ago")
    @Expose
    private String sraaTimeAgo;
    @SerializedName("sraa_date")
    @Expose
    private String sraaDate;
    @SerializedName("sraa_type")
    @Expose
    private String sraaType;

    public String getSraaId() {
        return sraaId;
    }

    public void setSraaId(String sraaId) {
        this.sraaId = sraaId;
    }

    public String getSraaFirstName() {
        return sraaFirstName;
    }

    public void setSraaFirstName(String sraaFirstName) {
        this.sraaFirstName = sraaFirstName;
    }

    public String getSraaLoginId() {
        return sraaLoginId;
    }

    public void setSraaLoginId(String sraaLoginId) {
        this.sraaLoginId = sraaLoginId;
    }

    public String getSraaUid() {
        return sraaUid;
    }

    public void setSraaUid(String sraaUid) {
        this.sraaUid = sraaUid;
    }

    public String getSraaWhoseUid() {
        return sraaWhoseUid;
    }

    public void setSraaWhoseUid(String sraaWhoseUid) {
        this.sraaWhoseUid = sraaWhoseUid;
    }

    public String getSraaQrId() {
        return sraaQrId;
    }

    public void setSraaQrId(String sraaQrId) {
        this.sraaQrId = sraaQrId;
    }

    public String getSraaTime() {
        return sraaTime;
    }

    public void setSraaTime(String sraaTime) {
        this.sraaTime = sraaTime;
    }

    public String getSraaTimeAgo() {
        return sraaTimeAgo;
    }

    public void setSraaTimeAgo(String sraaTimeAgo) {
        this.sraaTimeAgo = sraaTimeAgo;
    }

    public String getSraaDate() {
        return sraaDate;
    }

    public void setSraaDate(String sraaDate) {
        this.sraaDate = sraaDate;
    }

    public String getSraaType() {
        return sraaType;
    }

    public void setSraaType(String sraaType) {
        this.sraaType = sraaType;
    }
}
