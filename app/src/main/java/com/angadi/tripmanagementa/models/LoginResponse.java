package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("UID")
    @Expose
    private Integer uID;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status_login")
    @Expose
    private String statusLogin;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rand")
    @Expose
    private String rand;

    public Integer getUID() {
        return uID;
    }

    public void setUID(Integer uID) {
        this.uID = uID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusLogin() {
        return statusLogin;
    }

    public void setStatusLogin(String statusLogin) {
        this.statusLogin = statusLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getRand() {
        return rand;
    }

    public void setRand(String rand) {
        this.rand = rand;
    }

}

