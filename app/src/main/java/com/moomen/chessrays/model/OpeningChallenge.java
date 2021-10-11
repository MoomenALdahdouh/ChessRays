package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OpeningChallenge implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("openingGameId")
    String openingGameId;
    @SerializedName("openingHandlingId")
    String openingHandlingId;
    @SerializedName("challengeName")
    String challengeName;
    @SerializedName("date")
    String date;

    public OpeningChallenge() {
    }

    public OpeningChallenge(String userId, String openingGameId, String openingHandlingId, String challengeName, String date) {
        this.userId = userId;
        this.openingGameId = openingGameId;
        this.openingHandlingId = openingHandlingId;
        this.challengeName = challengeName;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpeningGameId() {
        return openingGameId;
    }

    public void setOpeningGameId(String openingGameId) {
        this.openingGameId = openingGameId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpeningHandlingId() {
        return openingHandlingId;
    }

    public void setOpeningHandlingId(String openingHandlingId) {
        this.openingHandlingId = openingHandlingId;
    }
}
