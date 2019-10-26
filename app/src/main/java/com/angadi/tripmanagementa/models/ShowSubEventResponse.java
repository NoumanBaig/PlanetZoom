package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShowSubEventResponse {
    @SerializedName("esaa_uid")
    @Expose
    private String esaaUid;
    @SerializedName("esaa_event_id")
    @Expose
    private String esaaEventId;
    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("results")
    @Expose
    private List<SubEventResult> results = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public String getEsaaUid() {
        return esaaUid;
    }

    public void setEsaaUid(String esaaUid) {
        this.esaaUid = esaaUid;
    }

    public String getEsaaEventId() {
        return esaaEventId;
    }

    public void setEsaaEventId(String esaaEventId) {
        this.esaaEventId = esaaEventId;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<SubEventResult> getResults() {
        return results;
    }

    public void setResults(List<SubEventResult> results) {
        this.results = results;
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
