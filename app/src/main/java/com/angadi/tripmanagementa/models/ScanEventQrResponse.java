package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanEventQrResponse {
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
    @SerializedName("pea_org")
    @Expose
    private String peaOrg;
    @SerializedName("pea_date")
    @Expose
    private String peaDate;
    @SerializedName("pea_date_time")
    @Expose
    private String peaDateTime;
    @SerializedName("pea_time")
    @Expose
    private String peaTime;
    @SerializedName("pea_report")
    @Expose
    private String peaReport;
    @SerializedName("check_qr")
    @Expose
    private String checkQr;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pea_ticket_selfi")
    @Expose
    private String pea_ticket_selfi;
    @SerializedName("tickets_id")
    @Expose
    private String tickets_id;
    @SerializedName("ura_fname")
    @Expose
    private String ura_fname;
    @SerializedName("ura_login_id")
    @Expose
    private String ura_login_id;
    @SerializedName("tickets_category")
    @Expose
    private String tickets_category ;
    @SerializedName("tickets_count")
    @Expose
    private String tickets_count ;

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

    public String getPeaOrg() {
        return peaOrg;
    }

    public void setPeaOrg(String peaOrg) {
        this.peaOrg = peaOrg;
    }

    public String getPeaDate() {
        return peaDate;
    }

    public void setPeaDate(String peaDate) {
        this.peaDate = peaDate;
    }

    public String getPeaDateTime() {
        return peaDateTime;
    }

    public void setPeaDateTime(String peaDateTime) {
        this.peaDateTime = peaDateTime;
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

    public String getCheckQr() {
        return checkQr;
    }

    public void setCheckQr(String checkQr) {
        this.checkQr = checkQr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getPea_ticket_selfi() {
        return pea_ticket_selfi;
    }

    public void setPea_ticket_selfi(String pea_ticket_selfi) {
        this.pea_ticket_selfi = pea_ticket_selfi;
    }
    public String getTickets_id() {
        return tickets_id;
    }

    public void setTickets_id(String tickets_id) {
        this.tickets_id = tickets_id;
    }

    public String getUra_fname() {
        return ura_fname;
    }

    public void setUra_fname(String ura_fname) {
        this.ura_fname = ura_fname;
    }

    public String getUra_login_id() {
        return ura_login_id;
    }

    public void setUra_login_id(String ura_login_id) {
        this.ura_login_id = ura_login_id;
    }

    public String getTickets_category() {
        return tickets_category;
    }

    public void setTickets_category(String tickets_category) {
        this.tickets_category = tickets_category;
    }

    public String getTickets_count() {
        return tickets_count;
    }

    public void setTickets_count(String tickets_count) {
        this.tickets_count = tickets_count;
    }
}
