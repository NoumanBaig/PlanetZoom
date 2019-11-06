package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("pea_logo")
    @Expose
    private String peaLogo;
    @SerializedName("pea_blue_print")
    @Expose
    private List<Object> peaBluePrint = null;
    @SerializedName("pea_gallerys")
    @Expose
    private List<PeaGallery> peaGallerys = null;
    @SerializedName("pea_qr_code_id_secure")
    @Expose
    private String peaQrCodeIdSecure;
    @SerializedName("pea_qr_code_id_secure_link")
    @Expose
    private String peaQrCodeIdSecureLink;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pea_contact_no")
    @Expose
    private String pea_contact_no;
    @SerializedName("pea_emergency_no")
    @Expose
    private String pea_emergency_no;

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

    public String getPeaLogo() {
        return peaLogo;
    }

    public void setPeaLogo(String peaLogo) {
        this.peaLogo = peaLogo;
    }

    public List<Object> getPeaBluePrint() {
        return peaBluePrint;
    }

    public void setPeaBluePrint(List<Object> peaBluePrint) {
        this.peaBluePrint = peaBluePrint;
    }

    public List<PeaGallery> getPeaGallerys() {
        return peaGallerys;
    }

    public void setPeaGallerys(List<PeaGallery> peaGallerys) {
        this.peaGallerys = peaGallerys;
    }

    public String getPeaQrCodeIdSecure() {
        return peaQrCodeIdSecure;
    }

    public void setPeaQrCodeIdSecure(String peaQrCodeIdSecure) {
        this.peaQrCodeIdSecure = peaQrCodeIdSecure;
    }

    public String getPeaQrCodeIdSecureLink() {
        return peaQrCodeIdSecureLink;
    }

    public void setPeaQrCodeIdSecureLink(String peaQrCodeIdSecureLink) {
        this.peaQrCodeIdSecureLink = peaQrCodeIdSecureLink;
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

    public String getPea_contact_no() {
        return pea_contact_no;
    }

    public void setPea_contact_no(String pea_contact_no) {
        this.pea_contact_no = pea_contact_no;
    }

    public String getPea_emergency_no() {
        return pea_emergency_no;
    }

    public void setPea_emergency_no(String pea_emergency_no) {
        this.pea_emergency_no = pea_emergency_no;
    }

}
