package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MiddleGame implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("middleType")
    String middleType;
    @SerializedName("middleSubType")
    String middleSubType;
    @SerializedName("date")
    String date;
    @SerializedName("score")
    float score;
    @SerializedName("win")
    int win;
    @SerializedName("draw")
    int draw;
    @SerializedName("loss")
    int loss;
    @SerializedName("totalWin")
    int totalWin;
    @SerializedName("totalDraw")
    int totalDraw;
    @SerializedName("totalLoss")
    int totalLoss;
    @SerializedName("good")
    int good;
    @SerializedName("equal")
    int equal;
    @SerializedName("bad")
    int bad;
    @SerializedName("totalGood")
    int totalGood;
    @SerializedName("totalEqual")
    int totalEqual;
    @SerializedName("totalBad")
    int totalBad;
    @SerializedName("total")
    int total;
    @SerializedName("result")
    int result;
    @SerializedName("totalResult")
    int totalResult;
    @SerializedName("frequency")
    float frequency;
    @SerializedName("efficiency")
    float efficiency;
    @SerializedName("occurance")
    int occurance;
    @SerializedName("occuranceAllMiddle")
    int occuranceAllMiddle;


    public MiddleGame() {
    }

    public MiddleGame(String userId, String middleType, String middleSubType, String date, float score
            , int win, int draw, int loss, int totalWin, int totalDraw, int totalLoss, int good
            , int equal, int bad, int totalGood, int totalEqual, int totalBad, int total
            , int result, int totalResult, float frequency, float efficiency, int occurance,int occuranceAllMiddle) {
        this.userId = userId;
        this.middleType = middleType;
        this.middleSubType = middleSubType;
        this.date = date;
        this.score = score;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.totalWin = totalWin;
        this.totalDraw = totalDraw;
        this.totalLoss = totalLoss;
        this.good = good;
        this.equal = equal;
        this.bad = bad;
        this.totalGood = totalGood;
        this.totalEqual = totalEqual;
        this.totalBad = totalBad;
        this.total = total;
        this.result = result;
        this.totalResult = totalResult;
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

    public String getMiddleSubType() {
        return middleSubType;
    }

    public void setMiddleSubType(String middleSubType) {
        this.middleSubType = middleSubType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
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

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getEqual() {
        return equal;
    }

    public void setEqual(int equal) {
        this.equal = equal;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
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
