package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminEventResult {
    @SerializedName("pea_id")
    @Expose
    private String peaId;
    @SerializedName("pea_uid")
    @Expose
    private String peaUid;
    @SerializedName("pea_name")
    @Expose
    private String peaName;
    @SerializedName("pea_venue")
    @Expose
    private String peaVenue;
    @SerializedName("pea_desc")
    @Expose
    private String peaDesc;
    @SerializedName("pea_date")
    @Expose
    private String peaDate;
    @SerializedName("pea_time")
    @Expose
    private String peaTime;
    @SerializedName("pea_org")
    @Expose
    private String peaOrg;

    public String getPeaId() {
        return peaId;
    }

    public void setPeaId(String peaId) {
        this.peaId = peaId;
    }

    public String getPeaUid() {
        return peaUid;
    }

    public void setPeaUid(String peaUid) {
        this.peaUid = peaUid;
    }

    public String getPeaName() {
        return peaName;
    }

    public void setPeaName(String peaName) {
        this.peaName = peaName;
    }

    public String getPeaVenue() {
        return peaVenue;
    }

    public void setPeaVenue(String peaVenue) {
        this.peaVenue = peaVenue;
    }

    public String getPeaDesc() {
        return peaDesc;
    }

    public void setPeaDesc(String peaDesc) {
        this.peaDesc = peaDesc;
    }

    public String getPeaDate() {
        return peaDate;
    }

    public void setPeaDate(String peaDate) {
        this.peaDate = peaDate;
    }

    public String getPeaTime() {
        return peaTime;
    }

    public void setPeaTime(String peaTime) {
        this.peaTime = peaTime;
    }

    public String getPeaOrg() {
        return peaOrg;
    }

    public void setPeaOrg(String peaOrg) {
        this.peaOrg = peaOrg;
    }

}
