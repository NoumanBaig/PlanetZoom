package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeUniqueId {
    @SerializedName("qcaa_id")
    @Expose
    private String qcaaId;
    @SerializedName("qcaa_code_id_secure")
    @Expose
    private String qcaaCodeIdSecure;
    @SerializedName("qcaa_code_id_secure_link")
    @Expose
    private String qcaaCodeIdSecureLink;

    public String getQcaaId() {
        return qcaaId;
    }

    public void setQcaaId(String qcaaId) {
        this.qcaaId = qcaaId;
    }

    public String getQcaaCodeIdSecure() {
        return qcaaCodeIdSecure;
    }

    public void setQcaaCodeIdSecure(String qcaaCodeIdSecure) {
        this.qcaaCodeIdSecure = qcaaCodeIdSecure;
    }

    public String getQcaaCodeIdSecureLink() {
        return qcaaCodeIdSecureLink;
    }

    public void setQcaaCodeIdSecureLink(String qcaaCodeIdSecureLink) {
        this.qcaaCodeIdSecureLink = qcaaCodeIdSecureLink;
    }
}
