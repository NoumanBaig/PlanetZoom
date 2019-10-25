package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateEventResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("new_event_id")
    @Expose
    private String newEventId;
    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNewEventId() {
        return newEventId;
    }

    public void setNewEventId(String newEventId) {
        this.newEventId = newEventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
