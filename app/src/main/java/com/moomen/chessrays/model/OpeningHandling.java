package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OpeningHandling implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("openingId")
    String openingId;
    @SerializedName("openingGameId")
    String openingGameId;
    @SerializedName("openingColor")
    String openingColor;
    @SerializedName("openingFirstMove")
    String openingFirstMove;
    @SerializedName("challengeId")
    String challengeId;
    @SerializedName("challengeName")
    String challengeName;
    @SerializedName("firstPlayerName")
    String firstPlayerName;
    @SerializedName("secondPlayerName")
    String secondPlayerName;
    @SerializedName("date")
    String date;
    @SerializedName("good")
    int good;
    @SerializedName("equal")
    int equal;
    @SerializedName("bad")
    int bad;
    @SerializedName("win")
    int win;
    @SerializedName("draw")
    float draw;
    @SerializedName("loss")
    int loss;
    @SerializedName("result")
    int result;
    @SerializedName("total")
    int total;
    @SerializedName("frequency")
    float frequency;
    @SerializedName("efficiency")
    float efficiency;
    @SerializedName("occurance")
    int occurance;


    public OpeningHandling() {
    }

    public OpeningHandling(String userId, String openingId, String openingGameId, String openingColor
            , String openingFirstMove, String challengeId, String challengeName, String firstPlayerName
            , String secondPlayerName, String date, int good, int equal, int bad, int win, float draw
            , int loss, int result, int total, float frequency, float efficiency, int occurance) {
        this.userId = userId;
        this.openingId = openingId;
        this.openingGameId = openingGameId;
        this.openingColor = openingColor;
        this.openingFirstMove = openingFirstMove;
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
        this.date = date;
        this.good = good;
        this.equal = equal;
        this.bad = bad;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.result = result;
        this.total = total;
        this.frequency = frequency;
        this.efficiency = efficiency;
        this.occurance = occurance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpeningId() {
        return openingId;
    }

    public void setOpeningId(String openingId) {
        this.openingId = openingId;
    }

    public String getOpeningGameId() {
        return openingGameId;
    }

    public void setOpeningGameId(String openingGameId) {
        this.openingGameId = openingGameId;
    }

    public String getOpeningColor() {
        return openingColor;
    }

    public void setOpeningColor(String openingColor) {
        this.openingColor = openingColor;
    }

    public String getOpeningFirstMove() {
        return openingFirstMove;
    }

    public void setOpeningFirstMove(String openingFirstMove) {
        this.openingFirstMove = openingFirstMove;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public float getDraw() {
        return draw;
    }

    public void setDraw(float draw) {
        this.draw = draw;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getOccurance() {
        return occurance;
    }

    public void setOccurance(int occurance) {
        this.occurance = occurance;
    }
}
