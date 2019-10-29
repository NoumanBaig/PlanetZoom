package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowMembersResponse {
    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("results")
    @Expose
    private List<ShowMembersResult> results = null;
    @SerializedName("check_response")
    @Expose
    private String checkResponse;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<ShowMembersResult> getResults() {
        return results;
    }

    public void setResults(List<ShowMembersResult> results) {
        this.results = results;
    }

    public String getCheckResponse() {
        return checkResponse;
    }

    public void setCheckResponse(String checkResponse) {
        this.checkResponse = checkResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
