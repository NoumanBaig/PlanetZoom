package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileGallery {
    @SerializedName("piaa_id")
    @Expose
    private String piaaId;
    @SerializedName("piaa_image")
    @Expose
    private String piaaImage;

    public String getPiaaId() {
        return piaaId;
    }

    public void setPiaaId(String piaaId) {
        this.piaaId = piaaId;
    }

    public String getPiaaImage() {
        return piaaImage;
    }

    public void setPiaaImage(String piaaImage) {
        this.piaaImage = piaaImage;
    }
}
