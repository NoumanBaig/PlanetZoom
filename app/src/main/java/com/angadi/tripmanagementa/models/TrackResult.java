package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackResult {
    @SerializedName("track_data")
    @Expose
    private List<TrackData> trackData = null;

    public List<TrackData> getTrackData() {
        return trackData;
    }

    public void setTrackData(List<TrackData> trackData) {
        this.trackData = trackData;
    }
}
