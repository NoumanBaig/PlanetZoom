package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateQrResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("qr_code_id")
    @Expose
    private String qrCodeId;
    @SerializedName("qr_code_id_secure")
    @Expose
    private String qrCodeIdSecure;
    @SerializedName("qr_code_id_secure_link")
    @Expose
    private String qrCodeIdSecureLink;
    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getQrCodeIdSecure() {
        return qrCodeIdSecure;
    }

    public void setQrCodeIdSecure(String qrCodeIdSecure) {
        this.qrCodeIdSecure = qrCodeIdSecure;
    }

    public String getQrCodeIdSecureLink() {
        return qrCodeIdSecureLink;
    }

    public void setQrCodeIdSecureLink(String qrCodeIdSecureLink) {
        this.qrCodeIdSecureLink = qrCodeIdSecureLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
