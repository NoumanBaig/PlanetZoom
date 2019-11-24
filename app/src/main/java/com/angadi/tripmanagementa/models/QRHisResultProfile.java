package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QRHisResultProfile {
    @SerializedName("scan_data")
    @Expose
    private List<QRHisDataProfile> scanData = null;

    public List<QRHisDataProfile> getScanData() {
        return scanData;
    }

    public void setScanData(List<QRHisDataProfile> scanData) {
        this.scanData = scanData;
    }
}
