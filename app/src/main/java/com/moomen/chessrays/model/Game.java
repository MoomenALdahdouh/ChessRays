package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("date")
    String date;
    @SerializedName("type")
    String type;
    @SerializedName("score")
    float score;
    @SerializedName("totalWin")
    int totalWin;
    @SerializedName("totalDraw")
    float totalDraw;
    @SerializedName("totalLoss")
    int totalLoss;


    public Game() {
    }

    public Game(String userId, String date, String type, float score, int totalWin, float totalDraw, int totalLoss) {
        this.userId = userId;
        this.date = date;
        this.type = type;
        this.score = score;
        this.totalWin = totalWin;
        this.totalDraw = totalDraw;
        this.totalLoss = totalLoss;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }

    public float getTotalDraw() {
        return totalDraw;
    }

    public void setTotalDraw(float totalDraw) {
        this.totalDraw = totalDraw;
    }

    public int getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(int totalLoss) {
        this.totalLoss = totalLoss;
    }
}
