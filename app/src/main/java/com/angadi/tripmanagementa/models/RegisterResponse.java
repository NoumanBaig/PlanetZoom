package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("UID")
    @Expose
    private Integer uID;
    @SerializedName("rand")
    @Expose
    private Integer rand;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getUID() {
        return uID;
    }

    public void setUID(Integer uID) {
        this.uID = uID;
    }

    public Integer getRand() {
        return rand;
    }

    public void setRand(Integer rand) {
        this.rand = rand;
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
