package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventTrackResponse {
    @SerializedName("results")
    @Expose
    private List<TrackResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<TrackResult> getResults() {
        return results;
    }

    public void setResults(List<TrackResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
