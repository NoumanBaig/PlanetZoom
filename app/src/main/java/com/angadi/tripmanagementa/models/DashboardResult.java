package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardResult {
    @SerializedName("qcaa_cat_id")
    @Expose
    private String qcaaCatId;
    @SerializedName("qcaa_cat_name")
    @Expose
    private String qcaaCatName;
    @SerializedName("qr_code_unique_id")
    @Expose
    private List<QrCodeUniqueId> qrCodeUniqueId = null;

    public String getQcaaCatId() {
        return qcaaCatId;
    }

    public void setQcaaCatId(String qcaaCatId) {
        this.qcaaCatId = qcaaCatId;
    }

    public String getQcaaCatName() {
        return qcaaCatName;
    }

    public void setQcaaCatName(String qcaaCatName) {
        this.qcaaCatName = qcaaCatName;
    }

    public List<QrCodeUniqueId> getQrCodeUniqueId() {
        return qrCodeUniqueId;
    }

    public void setQrCodeUniqueId(List<QrCodeUniqueId> qrCodeUniqueId) {
        this.qrCodeUniqueId = qrCodeUniqueId;
    }

}
