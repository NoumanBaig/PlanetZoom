package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeaGallery {
    @SerializedName("eiaa_id")
    @Expose
    private String eiaaId;
    @SerializedName("eiaa_image")
    @Expose
    private String eiaaImage;
    @SerializedName("eiaa_desc")
    @Expose
    private String eiaaDesc;

    public String getEiaaId() {
        return eiaaId;
    }

    public void setEiaaId(String eiaaId) {
        this.eiaaId = eiaaId;
    }

    public String getEiaaImage() {
        return eiaaImage;
    }

    public void setEiaaImage(String eiaaImage) {
        this.eiaaImage = eiaaImage;
    }

    public String getEiaaDesc() {
        return eiaaDesc;
    }

    public void setEiaaDesc(String eiaaDesc) {
        this.eiaaDesc = eiaaDesc;
    }
}
