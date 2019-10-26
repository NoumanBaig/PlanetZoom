package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchVolunteerResponse {
    @SerializedName("ura_id")
    @Expose
    private String uraId;
    @SerializedName("ura_fname")
    @Expose
    private String uraFname;
    @SerializedName("ura_lname")
    @Expose
    private String uraLname;
    @SerializedName("ura_login_id")
    @Expose
    private String uraLoginId;
    @SerializedName("status")
    @Expose
    private String status;

    public String getUraId() {
        return uraId;
    }

    public void setUraId(String uraId) {
        this.uraId = uraId;
    }

    public String getUraFname() {
        return uraFname;
    }

    public void setUraFname(String uraFname) {
        this.uraFname = uraFname;
    }

    public String getUraLname() {
        return uraLname;
    }

    public void setUraLname(String uraLname) {
        this.uraLname = uraLname;
    }

    public String getUraLoginId() {
        return uraLoginId;
    }

    public void setUraLoginId(String uraLoginId) {
        this.uraLoginId = uraLoginId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
