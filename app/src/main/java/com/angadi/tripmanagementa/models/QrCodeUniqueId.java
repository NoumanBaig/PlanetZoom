package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeUniqueId {
    @SerializedName("qcaa_id")
    @Expose
    private String qcaaId;

    public String getQcaaId() {
        return qcaaId;
    }

    public void setQcaaId(String qcaaId) {
        this.qcaaId = qcaaId;
    }
}
