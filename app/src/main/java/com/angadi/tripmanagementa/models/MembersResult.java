package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MembersResult {
    @SerializedName("eulaa_id")
    @Expose
    private String eulaaId;
    @SerializedName("eulaa_name")
    @Expose
    private String eulaaName;
    private boolean isSelected;

    public String getEulaaId() {
        return eulaaId;
    }

    public void setEulaaId(String eulaaId) {
        this.eulaaId = eulaaId;
    }

    public String getEulaaName() {
        return eulaaName;
    }

    public void setEulaaName(String eulaaName) {
        this.eulaaName = eulaaName;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
