package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse {
    @SerializedName("ura_id")
    @Expose
    private String uraId;
    @SerializedName("ura_fname")
    @Expose
    private String uraFname;
    @SerializedName("ura_lname")
    @Expose
    private String uraLname;
    @SerializedName("ura_login_id")
    @Expose
    private String uraLoginId;
    @SerializedName("ura_img")
    @Expose
    private String uraImg;
    @SerializedName("ura_about")
    @Expose
    private String uraAbout;
    @SerializedName("ura_address")
    @Expose
    private String uraAddress;
    @SerializedName("ura_company_name")
    @Expose
    private String uraCompanyName;
    @SerializedName("ura_designation")
    @Expose
    private String uraDesignation;
    @SerializedName("ura_website")
    @Expose
    private String uraWebsite;
    @SerializedName("ura_biz_phone")
    @Expose
    private String uraBizPhone;
    @SerializedName("ura_biz_email")
    @Expose
    private String uraBizEmail;
    @SerializedName("ura_facebook")
    @Expose
    private String uraFacebook;
    @SerializedName("ura_whatsapp")
    @Expose
    private String uraWhatsapp;
    @SerializedName("ura_linkedin")
    @Expose
    private String uraLinkedin;
    @SerializedName("ura_youtube")
    @Expose
    private String uraYoutube;
    @SerializedName("ura_instagram")
    @Expose
    private String uraInstagram;
    @SerializedName("ura_code_id_secure")
    @Expose
    private String uraCodeIdSecure;
    @SerializedName("ura_code_id_secure_link")
    @Expose
    private String uraCodeIdSecureLink;
    @SerializedName("ura_gallerys")
    @Expose
    private List<ProfileGallery> uraGallerys = null;
    @SerializedName("status")
    @Expose
    private String status;

    public String getUraId() {
        return uraId;
    }

    public void setUraId(String uraId) {
        this.uraId = uraId;
    }

    public String getUraFname() {
        return uraFname;
    }

    public void setUraFname(String uraFname) {
        this.uraFname = uraFname;
    }

    public String getUraLname() {
        return uraLname;
    }

    public void setUraLname(String uraLname) {
        this.uraLname = uraLname;
    }

    public String getUraLoginId() {
        return uraLoginId;
    }

    public void setUraLoginId(String uraLoginId) {
        this.uraLoginId = uraLoginId;
    }

    public String getUraImg() {
        return uraImg;
    }

    public void setUraImg(String uraImg) {
        this.uraImg = uraImg;
    }

    public String getUraAbout() {
        return uraAbout;
    }

    public void setUraAbout(String uraAbout) {
        this.uraAbout = uraAbout;
    }

    public String getUraAddress() {
        return uraAddress;
    }

    public void setUraAddress(String uraAddress) {
        this.uraAddress = uraAddress;
    }

    public String getUraCompanyName() {
        return uraCompanyName;
    }

    public void setUraCompanyName(String uraCompanyName) {
        this.uraCompanyName = uraCompanyName;
    }

    public String getUraDesignation() {
        return uraDesignation;
    }

    public void setUraDesignation(String uraDesignation) {
        this.uraDesignation = uraDesignation;
    }

    public String getUraWebsite() {
        return uraWebsite;
    }

    public void setUraWebsite(String uraWebsite) {
        this.uraWebsite = uraWebsite;
    }

    public String getUraBizPhone() {
        return uraBizPhone;
    }

    public void setUraBizPhone(String uraBizPhone) {
        this.uraBizPhone = uraBizPhone;
    }

    public String getUraBizEmail() {
        return uraBizEmail;
    }

    public void setUraBizEmail(String uraBizEmail) {
        this.uraBizEmail = uraBizEmail;
    }

    public String getUraFacebook() {
        return uraFacebook;
    }

    public void setUraFacebook(String uraFacebook) {
        this.uraFacebook = uraFacebook;
    }

    public String getUraWhatsapp() {
        return uraWhatsapp;
    }

    public void setUraWhatsapp(String uraWhatsapp) {
        this.uraWhatsapp = uraWhatsapp;
    }

    public String getUraLinkedin() {
        return uraLinkedin;
    }

    public void setUraLinkedin(String uraLinkedin) {
        this.uraLinkedin = uraLinkedin;
    }

    public String getUraYoutube() {
        return uraYoutube;
    }

    public void setUraYoutube(String uraYoutube) {
        this.uraYoutube = uraYoutube;
    }

    public String getUraInstagram() {
        return uraInstagram;
    }

    public void setUraInstagram(String uraInstagram) {
        this.uraInstagram = uraInstagram;
    }

    public String getUraCodeIdSecure() {
        return uraCodeIdSecure;
    }

    public void setUraCodeIdSecure(String uraCodeIdSecure) {
        this.uraCodeIdSecure = uraCodeIdSecure;
    }

    public String getUraCodeIdSecureLink() {
        return uraCodeIdSecureLink;
    }

    public void setUraCodeIdSecureLink(String uraCodeIdSecureLink) {
        this.uraCodeIdSecureLink = uraCodeIdSecureLink;
    }

    public List<ProfileGallery> getUraGallerys() {
        return uraGallerys;
    }

    public void setUraGallerys(List<ProfileGallery> uraGallerys) {
        this.uraGallerys = uraGallerys;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
