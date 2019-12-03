package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventVisitorsResponse {
    @SerializedName("Pioneer")
    @Expose
    private String pioneer;
    @SerializedName("Attendee")
    @Expose
    private String attendee;
    @SerializedName("Influencer")
    @Expose
    private String influencer;
    @SerializedName("Partner")
    @Expose
    private String partner;
    @SerializedName("VIP")
    @Expose
    private String vIP;
    @SerializedName("CoreTeam")
    @Expose
    private String coreTeam;
    @SerializedName("OrganizingTeam")
    @Expose
    private String organizingTeam;
    @SerializedName("TotalTeams")
    @Expose
    private String totalTeams;
    @SerializedName("status")
    @Expose
    private String status;

    public String getPioneer() {
        return pioneer;
    }

    public void setPioneer(String pioneer) {
        this.pioneer = pioneer;
    }

    public String getAttendee() {
        return attendee;
    }

    public void setAttendee(String attendee) {
        this.attendee = attendee;
    }

    public String getInfluencer() {
        return influencer;
    }

    public void setInfluencer(String influencer) {
        this.influencer = influencer;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getVIP() {
        return vIP;
    }

    public void setVIP(String vIP) {
        this.vIP = vIP;
    }

    public String getCoreTeam() {
        return coreTeam;
    }

    public void setCoreTeam(String coreTeam) {
        this.coreTeam = coreTeam;
    }

    public String getOrganizingTeam() {
        return organizingTeam;
    }

    public void setOrganizingTeam(String organizingTeam) {
        this.organizingTeam = organizingTeam;
    }

    public String getTotalTeams() {
        return totalTeams;
    }

    public void setTotalTeams(String totalTeams) {
        this.totalTeams = totalTeams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
