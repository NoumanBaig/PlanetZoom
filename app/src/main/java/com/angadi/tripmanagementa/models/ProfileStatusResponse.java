package com.angadi.tripmanagementa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileStatusResponse {
    @SerializedName("fav")
    @Expose
    private String fav;
    @SerializedName("fav_cnt")
    @Expose
    private String favCnt;
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("like_cnt")
    @Expose
    private String likeCnt;
    @SerializedName("dislike")
    @Expose
    private String dislike;
    @SerializedName("dislike_cnt")
    @Expose
    private String dislikeCnt;
    @SerializedName("my_rate")
    @Expose
    private String myRate;
    @SerializedName("rate_cnt")
    @Expose
    private String rateCnt;
    @SerializedName("rate_avg")
    @Expose
    private String rateAvg;
    @SerializedName("status")
    @Expose
    private String status;

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getFavCnt() {
        return favCnt;
    }

    public void setFavCnt(String favCnt) {
        this.favCnt = favCnt;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(String likeCnt) {
        this.likeCnt = likeCnt;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getDislikeCnt() {
        return dislikeCnt;
    }

    public void setDislikeCnt(String dislikeCnt) {
        this.dislikeCnt = dislikeCnt;
    }

    public String getMyRate() {
        return myRate;
    }

    public void setMyRate(String myRate) {
        this.myRate = myRate;
    }

    public String getRateCnt() {
        return rateCnt;
    }

    public void setRateCnt(String rateCnt) {
        this.rateCnt = rateCnt;
    }

    public String getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(String rateAvg) {
        this.rateAvg = rateAvg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
