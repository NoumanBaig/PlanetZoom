package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlacesResult {

    @SerializedName("etlaa_id")
    @Expose
    private String etlaaId;
    @SerializedName("etlaa_uid")
    @Expose
    private String etlaaUid;
    @SerializedName("etlaa_event_id")
    @Expose
    private String etlaaEventId;
    @SerializedName("etlaa_places")
    @Expose
    private String etlaaPlaces;
    private boolean isSelected;

    public String getEtlaaId() {
        return etlaaId;
    }

    public void setEtlaaId(String etlaaId) {
        this.etlaaId = etlaaId;
    }

    public String getEtlaaUid() {
        return etlaaUid;
    }

    public void setEtlaaUid(String etlaaUid) {
        this.etlaaUid = etlaaUid;
    }

    public String getEtlaaEventId() {
        return etlaaEventId;
    }

    public void setEtlaaEventId(String etlaaEventId) {
        this.etlaaEventId = etlaaEventId;
    }

    public String getEtlaaPlaces() {
        return etlaaPlaces;
    }

    public void setEtlaaPlaces(String etlaaPlaces) {
        this.etlaaPlaces = etlaaPlaces;
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
