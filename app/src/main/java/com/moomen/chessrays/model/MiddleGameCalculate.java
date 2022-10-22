package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MiddleGameCalculate implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("middleType")
    String middleType;
    @SerializedName("totalWin")
    int totalWin;
    @SerializedName("totalDraw")
    int totalDraw;
    @SerializedName("totalLoss")
    int totalLoss;
    @SerializedName("totalGood")
    int totalGood;
    @SerializedName("totalEqual")
    int totalEqual;
    @SerializedName("totalBad")
    int totalBad;
    @SerializedName("score")
    float score;
    @SerializedName("frequency")
    float frequency;
    @SerializedName("efficiency")
    float efficiency;
    @SerializedName("occurance")
    int occurance;
    @SerializedName("occuranceAllMiddle")
    int occuranceAllMiddle;


    public MiddleGameCalculate() {
    }

    public MiddleGameCalculate(String userId, String middleType, int totalWin, int totalDraw, int totalLoss
            , int totalGood, int totalEqual, int totalBad, float score, float frequency, float efficiency
            , int occurance, int occuranceAllMiddle) {
        this.userId = userId;
        this.middleType = middleType;
        this.totalWin = totalWin;
        this.totalDraw = totalDraw;
        this.totalLoss = totalLoss;
        this.totalGood = totalGood;
        this.totalEqual = totalEqual;
        this.totalBad = totalBad;
        this.score = score;
        this.frequency = frequency;
        this.efficiency = efficiency;
        this.occurance = occurance;
        this.occuranceAllMiddle = occuranceAllMiddle;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMiddleType() {
        return middleType;
    }

    public void setMiddleType(String middleType) {
        this.middleType = middleType;
    }

    public int getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }

    public int getTotalDraw() {
        return totalDraw;
    }

    public void setTotalDraw(int totalDraw) {
        this.totalDraw = totalDraw;
    }

    public int getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(int totalLoss) {
        this.totalLoss = totalLoss;
    }

    public int getTotalGood() {
        return totalGood;
    }

    public void setTotalGood(int totalGood) {
        this.totalGood = totalGood;
    }

    public int getTotalEqual() {
        return totalEqual;
    }

    public void setTotalEqual(int totalEqual) {
        this.totalEqual = totalEqual;
    }

    public int getTotalBad() {
        return totalBad;
    }

    public void setTotalBad(int totalBad) {
        this.totalBad = totalBad;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public int getOccurance() {
        return occurance;
    }

    public void setOccurance(int occurance) {
        this.occurance = occurance;
    }

    public int getOccuranceAllMiddle() {
        return occuranceAllMiddle;
    }

    public void setOccuranceAllMiddle(int occuranceAllMiddle) {
        this.occuranceAllMiddle = occuranceAllMiddle;
    }
}
