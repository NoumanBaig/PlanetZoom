package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackData {
    @SerializedName("mtaa_id")
    @Expose
    private String mtaaId;
    @SerializedName("mtaa_uid")
    @Expose
    private String mtaaUid;
    @SerializedName("mtaa_volunteer_name")
    @Expose
    private String mtaaVolunteerName;
    @SerializedName("mtaa_track_place")
    @Expose
    private String mtaaTrackPlace;
    @SerializedName("mtaa_volunteer_id")
    @Expose
    private String mtaaVolunteerId;
    @SerializedName("mtaa_event_id")
    @Expose
    private String mtaaEventId;
    @SerializedName("mtaa_place")
    @Expose
    private String mtaaPlace;
    @SerializedName("mtaa_time")
    @Expose
    private String mtaaTime;
    @SerializedName("mtaa_time_ago")
    @Expose
    private String mtaa_time_ago;
    @SerializedName("mtaa_date")
    @Expose
    private String mtaaDate;
    @SerializedName("mtaa_day")
    @Expose
    private String mtaaDay;
    @SerializedName("mtaa_month")
    @Expose
    private String mtaaMonth;
    @SerializedName("mtaa_year")
    @Expose
    private String mtaaYear;

    public String getMtaaId() {
        return mtaaId;
    }

    public void setMtaaId(String mtaaId) {
        this.mtaaId = mtaaId;
    }

    public String getMtaaUid() {
        return mtaaUid;
    }

    public void setMtaaUid(String mtaaUid) {
        this.mtaaUid = mtaaUid;
    }

    public String getMtaaVolunteerName() {
        return mtaaVolunteerName;
    }

    public void setMtaaVolunteerName(String mtaaVolunteerName) {
        this.mtaaVolunteerName = mtaaVolunteerName;
    }

    public String getMtaaTrackPlace() {
        return mtaaTrackPlace;
    }

    public void setMtaaTrackPlace(String mtaaTrackPlace) {
        this.mtaaTrackPlace = mtaaTrackPlace;
    }

    public String getMtaaVolunteerId() {
        return mtaaVolunteerId;
    }

    public void setMtaaVolunteerId(String mtaaVolunteerId) {
        this.mtaaVolunteerId = mtaaVolunteerId;
    }

    public String getMtaaEventId() {
        return mtaaEventId;
    }

    public void setMtaaEventId(String mtaaEventId) {
        this.mtaaEventId = mtaaEventId;
    }

    public String getMtaaPlace() {
        return mtaaPlace;
    }

    public void setMtaaPlace(String mtaaPlace) {
        this.mtaaPlace = mtaaPlace;
    }

    public String getMtaa_time_ago() {
        return mtaa_time_ago;
    }

    public void setMtaa_time_ago(String mtaa_time_ago) {
        this.mtaa_time_ago = mtaa_time_ago;
    }

    public String getMtaaTime() {
        return mtaaTime;
    }

    public void setMtaaTime(String mtaaTime) {
        this.mtaaTime = mtaaTime;
    }

    public String getMtaaDate() {
        return mtaaDate;
    }

    public void setMtaaDate(String mtaaDate) {
        this.mtaaDate = mtaaDate;
    }

    public String getMtaaDay() {
        return mtaaDay;
    }

    public void setMtaaDay(String mtaaDay) {
        this.mtaaDay = mtaaDay;
    }

    public String getMtaaMonth() {
        return mtaaMonth;
    }

    public void setMtaaMonth(String mtaaMonth) {
        this.mtaaMonth = mtaaMonth;
    }

    public String getMtaaYear() {
        return mtaaYear;
    }

    public void setMtaaYear(String mtaaYear) {
        this.mtaaYear = mtaaYear;
    }

}
