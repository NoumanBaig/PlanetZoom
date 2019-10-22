package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("caa_id")
    @Expose
    private String caaId;
    @SerializedName("caa_name")
    @Expose
    private String caaName;
    @SerializedName("caa_img")
    @Expose
    private String caaImg;

    public String getCaaId() {
        return caaId;
    }

    public void setCaaId(String caaId) {
        this.caaId = caaId;
    }

    public String getCaaName() {
        return caaName;
    }

    public void setCaaName(String caaName) {
        this.caaName = caaName;
    }

    public String getCaaImg() {
        return caaImg;
    }

    public void setCaaImg(String caaImg) {
        this.caaImg = caaImg;
    }

}
