package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QrScanResponse {
    @SerializedName("qcaa_uid")
    @Expose
    private String qcaaUid;
    @SerializedName("qcaa_date")
    @Expose
    private String qcaaDate;
    @SerializedName("qcaa_time")
    @Expose
    private String qcaaTime;
    @SerializedName("qcaa_cat_id")
    @Expose
    private String qcaaCatId;
    @SerializedName("qcaa_sub_cat_id")
    @Expose
    private String qcaaSubCatId;
    @SerializedName("qcaa_cat")
    @Expose
    private String qcaaCat;
    @SerializedName("qcaa_sub_cat")
    @Expose
    private String qcaaSubCat;
    @SerializedName("qcaa_name")
    @Expose
    private String qcaaName;
    @SerializedName("qcaa_desc")
    @Expose
    private String qcaaDesc;
    @SerializedName("qcaa_email_id")
    @Expose
    private String qcaaEmailId;
    @SerializedName("qcaa_phone_no")
    @Expose
    private String qcaaPhoneNo;
    @SerializedName("qcaa_social_whatsapp")
    @Expose
    private String qcaaSocialWhatsapp;
    @SerializedName("qcaa_social_facebook")
    @Expose
    private String qcaaSocialFacebook;
    @SerializedName("qcaa_social_youtube")
    @Expose
    private String qcaaSocialYoutube;
    @SerializedName("qcaa_website")
    @Expose
    private String qcaaWebsite;
    @SerializedName("qcaa_place")
    @Expose
    private String qcaaPlace;
    @SerializedName("qcaa_profile_logo")
    @Expose
    private String qcaaProfileLogo;
    @SerializedName("qcaa_gallerys")
    @Expose
    private List<QRCodeGallery> qcaaGallerys = null;
    @SerializedName("status")
    @Expose
    private String status;

    public String getQcaaUid() {
        return qcaaUid;
    }

    public void setQcaaUid(String qcaaUid) {
        this.qcaaUid = qcaaUid;
    }

    public String getQcaaDate() {
        return qcaaDate;
    }

    public void setQcaaDate(String qcaaDate) {
        this.qcaaDate = qcaaDate;
    }

    public String getQcaaTime() {
        return qcaaTime;
    }

    public void setQcaaTime(String qcaaTime) {
        this.qcaaTime = qcaaTime;
    }

    public String getQcaaCatId() {
        return qcaaCatId;
    }

    public void setQcaaCatId(String qcaaCatId) {
        this.qcaaCatId = qcaaCatId;
    }

    public String getQcaaSubCatId() {
        return qcaaSubCatId;
    }

    public void setQcaaSubCatId(String qcaaSubCatId) {
        this.qcaaSubCatId = qcaaSubCatId;
    }

    public String getQcaaCat() {
        return qcaaCat;
    }

    public void setQcaaCat(String qcaaCat) {
        this.qcaaCat = qcaaCat;
    }

    public String getQcaaSubCat() {
        return qcaaSubCat;
    }

    public void setQcaaSubCat(String qcaaSubCat) {
        this.qcaaSubCat = qcaaSubCat;
    }

    public String getQcaaName() {
        return qcaaName;
    }

    public void setQcaaName(String qcaaName) {
        this.qcaaName = qcaaName;
    }

    public String getQcaaDesc() {
        return qcaaDesc;
    }

    public void setQcaaDesc(String qcaaDesc) {
        this.qcaaDesc = qcaaDesc;
    }

    public String getQcaaEmailId() {
        return qcaaEmailId;
    }

    public void setQcaaEmailId(String qcaaEmailId) {
        this.qcaaEmailId = qcaaEmailId;
    }

    public String getQcaaPhoneNo() {
        return qcaaPhoneNo;
    }

    public void setQcaaPhoneNo(String qcaaPhoneNo) {
        this.qcaaPhoneNo = qcaaPhoneNo;
    }

    public String getQcaaSocialWhatsapp() {
        return qcaaSocialWhatsapp;
    }

    public void setQcaaSocialWhatsapp(String qcaaSocialWhatsapp) {
        this.qcaaSocialWhatsapp = qcaaSocialWhatsapp;
    }

    public String getQcaaSocialFacebook() {
        return qcaaSocialFacebook;
    }

    public void setQcaaSocialFacebook(String qcaaSocialFacebook) {
        this.qcaaSocialFacebook = qcaaSocialFacebook;
    }

    public String getQcaaSocialYoutube() {
        return qcaaSocialYoutube;
    }

    public void setQcaaSocialYoutube(String qcaaSocialYoutube) {
        this.qcaaSocialYoutube = qcaaSocialYoutube;
    }

    public String getQcaaWebsite() {
        return qcaaWebsite;
    }

    public void setQcaaWebsite(String qcaaWebsite) {
        this.qcaaWebsite = qcaaWebsite;
    }

    public String getQcaaPlace() {
        return qcaaPlace;
    }

    public void setQcaaPlace(String qcaaPlace) {
        this.qcaaPlace = qcaaPlace;
    }

    public String getQcaaProfileLogo() {
        return qcaaProfileLogo;
    }

    public void setQcaaProfileLogo(String qcaaProfileLogo) {
        this.qcaaProfileLogo = qcaaProfileLogo;
    }

    public List<QRCodeGallery> getQcaaGallerys() {
        return qcaaGallerys;
    }

    public void setQcaaGallerys(List<QRCodeGallery> qcaaGallerys) {
        this.qcaaGallerys = qcaaGallerys;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
