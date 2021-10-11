package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OpeningGame implements Serializable {
    @Keep
    @SerializedName("userId")
    String userId;
    @SerializedName("userUid")
    String userUid;
    @SerializedName("gameId")
    String gameId;
    @SerializedName("openingGameId")
    String openingGameId;
    @SerializedName("openingName")
    String openingName;
    @SerializedName("color")
    String color;
    @SerializedName("firstMove")
    String firstMove;
    @SerializedName("date")
    String date;
    @SerializedName("score")
    float score;
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
    @SerializedName("win")
    int win;
    @SerializedName("draw")
    float draw;
    @SerializedName("loss")
    int loss;
    @SerializedName("total")
    int total;
    @SerializedName("totalTG")
    int totalTG;
    @SerializedName("totalFAG")
    int totalFAG;
    @SerializedName("result")
    int result;
    @SerializedName("totalResult")
    int totalResult;
    @SerializedName("totalRTG")
    int totalRTG;
    @SerializedName("totalRAG")
    int totalRAG;
    @SerializedName("frequency")
    float frequency;
    @SerializedName("efficiency")
    float efficiency;
    @SerializedName("occurance")
    float occurance;
    @SerializedName("sumGoodTG")
    private int sumGoodTG;
    @SerializedName("sumEqualTG")
    private int sumEqualTG;
    @SerializedName("sumBadTG")
    private int sumBadTG;
    @SerializedName("sumWinTG")
    private int sumWinTG;
    @SerializedName("sumDrawTG")
    private int sumDrawTG;
    @SerializedName("sumLostTG")
    private int sumLostTG;


    public OpeningGame() {
    }

    public OpeningGame(String userId, String userUid, String gameId, String openingGameId
            , String openingName, String color, String firstMove, String date, float score
            , int totalWin, int totalDraw, int totalLoss, int good, int equal, int bad, int win
            , float draw, int loss, int total, int totalTG, int totalFAG, int result, int totalResult
            , int totalRTG, int totalRAG, float frequency, float efficiency, float occurance, int sumGoodTG
            , int sumEqualTG, int sumBadTG, int sumWinTG, int sumDrawTG, int sumLostTG) {
        this.userId = userId;
        this.userUid = userUid;
        this.gameId = gameId;
        this.openingGameId = openingGameId;
        this.openingName = openingName;
        this.color = color;
        this.firstMove = firstMove;
        this.date = date;
        this.score = score;
        this.totalWin = totalWin;
        this.totalDraw = totalDraw;
        this.totalLoss = totalLoss;
        this.good = good;
        this.equal = equal;
        this.bad = bad;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.total = total;
        this.totalTG = totalTG;
        this.totalFAG = totalFAG;
        this.result = result;
        this.totalResult = totalResult;
        this.totalRTG = totalRTG;
        this.totalRAG = totalRAG;
        this.frequency = frequency;
        this.efficiency = efficiency;
        this.occurance = occurance;
        this.sumGoodTG = sumGoodTG;
        this.sumEqualTG = sumEqualTG;
        this.sumBadTG = sumBadTG;
        this.sumWinTG = sumWinTG;
        this.sumDrawTG = sumDrawTG;
        this.sumLostTG = sumLostTG;
    }

    public int getSumGoodTG() {
        return sumGoodTG;
    }

    public void setSumGoodTG(int sumGoodTG) {
        this.sumGoodTG = sumGoodTG;
    }

    public int getSumEqualTG() {
        return sumEqualTG;
    }

    public void setSumEqualTG(int sumEqualTG) {
        this.sumEqualTG = sumEqualTG;
    }

    public int getSumBadTG() {
        return sumBadTG;
    }

    public void setSumBadTG(int sumBadTG) {
        this.sumBadTG = sumBadTG;
    }

    public int getSumWinTG() {
        return sumWinTG;
    }

    public void setSumWinTG(int sumWinTG) {
        this.sumWinTG = sumWinTG;
    }

    public int getSumDrawTG() {
        return sumDrawTG;
    }

    public void setSumDrawTG(int sumDrawTG) {
        this.sumDrawTG = sumDrawTG;
    }

    public int getSumLostTG() {
        return sumLostTG;
    }

    public void setSumLostTG(int sumLostTG) {
        this.sumLostTG = sumLostTG;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getTotalRTG() {
        return totalRTG;
    }

    public void setTotalRTG(int totalRTG) {
        this.totalRTG = totalRTG;
    }

    public int getTotalRAG() {
        return totalRAG;
    }

    public void setTotalRAG(int totalRAG) {
        this.totalRAG = totalRAG;
    }

    public int getTotalTG() {
        return totalTG;
    }

    public void setTotalTG(int totalTG) {
        this.totalTG = totalTG;
    }

    public int getTotalFAG() {
        return totalFAG;
    }

    public void setTotalFAG(int totalFAG) {
        this.totalFAG = totalFAG;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getOpeningGameId() {
        return openingGameId;
    }

    public void setOpeningGameId(String openingGameId) {
        this.openingGameId = openingGameId;
    }

    public String getOpeningName() {
        return openingName;
    }

    public void setOpeningName(String openingName) {
        this.openingName = openingName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFirstMove() {
        return firstMove;
    }

    public void setFirstMove(String firstMove) {
        this.firstMove = firstMove;
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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

    public float getOccurance() {
        return occurance;
    }

    public void setOccurance(float occurance) {
        this.occurance = occurance;
    }
}
