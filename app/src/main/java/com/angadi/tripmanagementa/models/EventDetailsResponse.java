package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailsResponse {
    @SerializedName("pea_id")
    @Expose
    private String peaId;
    @SerializedName("pea_uid")
    @Expose
    private String peaUid;
    @SerializedName("pea_name")
    @Expose
    private String peaName;
    @SerializedName("pea_price")
    @Expose
    private String peaPrice;
    @SerializedName("pea_tickets")
    @Expose
    private String peaTickets;
    @SerializedName("pea_location")
    @Expose
    private String peaLocation;
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
    @SerializedName("pea_report")
    @Expose
    private String peaReport;
    @SerializedName("pea_logo")
    @Expose
    private String peaLogo;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pea_org")
    @Expose
    private String pea_org;

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

    public String getPeaPrice() {
        return peaPrice;
    }

    public void setPeaPrice(String peaPrice) {
        this.peaPrice = peaPrice;
    }

    public String getPeaTickets() {
        return peaTickets;
    }

    public void setPeaTickets(String peaTickets) {
        this.peaTickets = peaTickets;
    }

    public String getPeaLocation() {
        return peaLocation;
    }

    public void setPeaLocation(String peaLocation) {
        this.peaLocation = peaLocation;
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

    public String getPeaReport() {
        return peaReport;
    }

    public void setPeaReport(String peaReport) {
        this.peaReport = peaReport;
    }

    public String getPeaLogo() {
        return peaLogo;
    }

    public void setPeaLogo(String peaLogo) {
        this.peaLogo = peaLogo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPea_org() {
        return pea_org;
    }

    public void setPea_org(String pea_org) {
        this.pea_org = pea_org;
    }
}
