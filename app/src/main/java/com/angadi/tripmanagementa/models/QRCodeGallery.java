package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QRCodeGallery {
    @SerializedName("qiaa_id")
    @Expose
    private String qiaaId;
    @SerializedName("qiaa_image")
    @Expose
    private String qiaaImage;

    public String getQiaaId() {
        return qiaaId;
    }

    public void setQiaaId(String qiaaId) {
        this.qiaaId = qiaaId;
    }

    public String getQiaaImage() {
        return qiaaImage;
    }

    public void setQiaaImage(String qiaaImage) {
        this.qiaaImage = qiaaImage;
    }
}
