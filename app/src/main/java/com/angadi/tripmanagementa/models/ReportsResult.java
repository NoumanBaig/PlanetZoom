package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportsResult {
    @SerializedName("track_data")
    @Expose
    private List<ReportsTrackData> trackData = null;

    public List<ReportsTrackData> getTrackData() {
        return trackData;
    }

    public void setTrackData(List<ReportsTrackData> trackData) {
        this.trackData = trackData;
    }
}
