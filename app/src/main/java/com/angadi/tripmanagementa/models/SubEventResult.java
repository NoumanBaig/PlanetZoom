package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubEventResult {
    @SerializedName("esaa_id")
    @Expose
    private String esaaId;
    @SerializedName("esaa_sub_title")
    @Expose
    private String esaaSubTitle;
    @SerializedName("esaa_desc")
    @Expose
    private String esaaDesc;
    @SerializedName("esaa_time")
    @Expose
    private String esaaTime;
    @SerializedName("esaa_date")
    @Expose
    private String esaaDate;

    public String getEsaaId() {
        return esaaId;
    }

    public void setEsaaId(String esaaId) {
        this.esaaId = esaaId;
    }

    public String getEsaaSubTitle() {
        return esaaSubTitle;
    }

    public void setEsaaSubTitle(String esaaSubTitle) {
        this.esaaSubTitle = esaaSubTitle;
    }

    public String getEsaaDesc() {
        return esaaDesc;
    }

    public void setEsaaDesc(String esaaDesc) {
        this.esaaDesc = esaaDesc;
    }

    public String getEsaaTime() {
        return esaaTime;
    }

    public void setEsaaTime(String esaaTime) {
        this.esaaTime = esaaTime;
    }

    public String getEsaaDate() {
        return esaaDate;
    }

    public void setEsaaDate(String esaaDate) {
        this.esaaDate = esaaDate;
    }

}
