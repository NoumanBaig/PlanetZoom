package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowMembersResult {
    @SerializedName("eavqa_id")
    @Expose
    private String eavqaId;
    @SerializedName("eavqa_uid")
    @Expose
    private String eavqaUid;
    @SerializedName("eavqa_fname")
    @Expose
    private String eavqaFname;
    @SerializedName("eavqa_lname")
    @Expose
    private String eavqaLname;
    @SerializedName("eavqa_user_login_id")
    @Expose
    private String eavqaUserLoginId;
    @SerializedName("eavqa_creator")
    @Expose
    private String eavqaCreator;
    @SerializedName("eavqa_event_id")
    @Expose
    private String eavqaEventId;
    @SerializedName("eavqa_map_place_id")
    @Expose
    private String eavqaMapPlaceId;
    @SerializedName("eavqa_time")
    @Expose
    private String eavqaTime;
    @SerializedName("eavqa_date")
    @Expose
    private String eavqaDate;
    @SerializedName("eavqa_about")
    @Expose
    private String eavqaAbout;
    @SerializedName("eavqa_active")
    @Expose
    private String eavqaActive;

    public String getEavqaId() {
        return eavqaId;
    }

    public void setEavqaId(String eavqaId) {
        this.eavqaId = eavqaId;
    }

    public String getEavqaUid() {
        return eavqaUid;
    }

    public void setEavqaUid(String eavqaUid) {
        this.eavqaUid = eavqaUid;
    }

    public String getEavqaFname() {
        return eavqaFname;
    }

    public void setEavqaFname(String eavqaFname) {
        this.eavqaFname = eavqaFname;
    }

    public String getEavqaLname() {
        return eavqaLname;
    }

    public void setEavqaLname(String eavqaLname) {
        this.eavqaLname = eavqaLname;
    }

    public String getEavqaUserLoginId() {
        return eavqaUserLoginId;
    }

    public void setEavqaUserLoginId(String eavqaUserLoginId) {
        this.eavqaUserLoginId = eavqaUserLoginId;
    }

    public String getEavqaCreator() {
        return eavqaCreator;
    }

    public void setEavqaCreator(String eavqaCreator) {
        this.eavqaCreator = eavqaCreator;
    }

    public String getEavqaEventId() {
        return eavqaEventId;
    }

    public void setEavqaEventId(String eavqaEventId) {
        this.eavqaEventId = eavqaEventId;
    }

    public String getEavqaMapPlaceId() {
        return eavqaMapPlaceId;
    }

    public void setEavqaMapPlaceId(String eavqaMapPlaceId) {
        this.eavqaMapPlaceId = eavqaMapPlaceId;
    }

    public String getEavqaTime() {
        return eavqaTime;
    }

    public void setEavqaTime(String eavqaTime) {
        this.eavqaTime = eavqaTime;
    }

    public String getEavqaDate() {
        return eavqaDate;
    }

    public void setEavqaDate(String eavqaDate) {
        this.eavqaDate = eavqaDate;
    }

    public String getEavqaAbout() {
        return eavqaAbout;
    }

    public void setEavqaAbout(String eavqaAbout) {
        this.eavqaAbout = eavqaAbout;
    }

    public String getEavqaActive() {
        return eavqaActive;
    }

    public void setEavqaActive(String eavqaActive) {
        this.eavqaActive = eavqaActive;
    }

}
