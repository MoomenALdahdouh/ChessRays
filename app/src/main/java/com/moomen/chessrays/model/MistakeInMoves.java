package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MistakeInMoves implements Serializable {
    @Keep
    @SerializedName("numberOfMove")
    int numberOfMove;
    @SerializedName("numberOfMistake")
    int numberOfMistake;
    @SerializedName("userId")
    String userId;
    @SerializedName("accountType")
    String accountType;

    public MistakeInMoves() {
    }

    public MistakeInMoves(int numberOfMove, int numberOfMistake, String userId,String accountType) {
        this.numberOfMove = numberOfMove;
        this.numberOfMistake = numberOfMistake;
        this.userId = userId;
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNumberOfMove() {
        return numberOfMove;
    }

    public void setNumberOfMove(int numberOfMove) {
        this.numberOfMove = numberOfMove;
    }

    public int getNumberOfMistake() {
        return numberOfMistake;
    }

    public void setNumberOfMistake(int numberOfMistake) {
        this.numberOfMistake = numberOfMistake;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
