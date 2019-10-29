package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowAllMembersResult {
    @SerializedName("eauqa_id")
    @Expose
    private String eauqaId;
    @SerializedName("eauqa_uid")
    @Expose
    private String eauqaUid;
    @SerializedName("eauqa_time")
    @Expose
    private String eauqaTime;
    @SerializedName("eauqa_date")
    @Expose
    private String eauqaDate;
    @SerializedName("eauqa_name")
    @Expose
    private String eauqaName;
    @SerializedName("eauqa_email")
    @Expose
    private String eauqaEmail;
    @SerializedName("eauqa_phone")
    @Expose
    private String eauqaPhone;
    @SerializedName("eauqa_about")
    @Expose
    private String eauqaAbout;
    @SerializedName("eauqa_event_id")
    @Expose
    private String eauqaEventId;
    @SerializedName("eauqa_users_type")
    @Expose
    private String eauqaUsersType;

    public String getEauqaId() {
        return eauqaId;
    }

    public void setEauqaId(String eauqaId) {
        this.eauqaId = eauqaId;
    }

    public String getEauqaUid() {
        return eauqaUid;
    }

    public void setEauqaUid(String eauqaUid) {
        this.eauqaUid = eauqaUid;
    }

    public String getEauqaTime() {
        return eauqaTime;
    }

    public void setEauqaTime(String eauqaTime) {
        this.eauqaTime = eauqaTime;
    }

    public String getEauqaDate() {
        return eauqaDate;
    }

    public void setEauqaDate(String eauqaDate) {
        this.eauqaDate = eauqaDate;
    }

    public String getEauqaName() {
        return eauqaName;
    }

    public void setEauqaName(String eauqaName) {
        this.eauqaName = eauqaName;
    }

    public String getEauqaEmail() {
        return eauqaEmail;
    }

    public void setEauqaEmail(String eauqaEmail) {
        this.eauqaEmail = eauqaEmail;
    }

    public String getEauqaPhone() {
        return eauqaPhone;
    }

    public void setEauqaPhone(String eauqaPhone) {
        this.eauqaPhone = eauqaPhone;
    }

    public String getEauqaAbout() {
        return eauqaAbout;
    }

    public void setEauqaAbout(String eauqaAbout) {
        this.eauqaAbout = eauqaAbout;
    }

    public String getEauqaEventId() {
        return eauqaEventId;
    }

    public void setEauqaEventId(String eauqaEventId) {
        this.eauqaEventId = eauqaEventId;
    }

    public String getEauqaUsersType() {
        return eauqaUsersType;
    }

    public void setEauqaUsersType(String eauqaUsersType) {
        this.eauqaUsersType = eauqaUsersType;
    }
}
