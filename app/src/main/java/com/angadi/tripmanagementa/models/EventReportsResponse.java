package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventReportsResponse {
    @SerializedName("counts")
    @Expose
    private String counts;
    @SerializedName("results")
    @Expose
    private List<ReportsResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public List<ReportsResult> getResults() {
        return results;
    }

    public void setResults(List<ReportsResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}