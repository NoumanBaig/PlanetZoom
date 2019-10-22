package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubResults {
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("main_cat_id")
    @Expose
    private String mainCatId;
    @SerializedName("sub_cat_name")
    @Expose
    private String subCatName;
    @SerializedName("caa_img")
    @Expose
    private String caaImg;
    private boolean isSelected;

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getMainCatId() {
        return mainCatId;
    }

    public void setMainCatId(String mainCatId) {
        this.mainCatId = mainCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getCaaImg() {
        return caaImg;
    }

    public void setCaaImg(String caaImg) {
        this.caaImg = caaImg;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }



}
