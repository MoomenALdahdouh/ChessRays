package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class InstallNewGame implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("gameType")
    String gameType;
    @SerializedName("rate")
    float rate;
    @SerializedName("date")
    String date;
    @SerializedName("opponentName")
    String opponentName;
    @SerializedName("opponentRate")
    float opponentRate;
    @SerializedName("index")
    int index;

    public InstallNewGame() {
    }

    public InstallNewGame(String userId, String gameType, float rate, String date, String opponentName, float opponentRate, int index) {
        this.userId = userId;
        this.gameType = gameType;
        this.rate = rate;
        this.date = date;
        this.opponentName = opponentName;
        this.opponentRate = opponentRate;
        this.index = index;
    }

    public float getOpponentRate() {
        return opponentRate;
    }

    public void setOpponentRate(float opponentRate) {
        this.opponentRate = opponentRate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
