package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QRHisResult {
    @SerializedName("scan_data")
    @Expose
    private List<QRHisData> scanData = null;

    public List<QRHisData> getScanData() {
        return scanData;
    }

    public void setScanData(List<QRHisData> scanData) {
        this.scanData = scanData;
    }
}
