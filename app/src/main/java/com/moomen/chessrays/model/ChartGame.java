package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChartGame  implements Serializable {
    @Keep
    @SerializedName("accountType")
    String accountType;
    @SerializedName("date")
    String date;
    @SerializedName("userId")
    String userId;
    @SerializedName("installNewGameArrayList")
    ArrayList<InstallNewGame> installNewGameArrayList;

    public ChartGame() {
    }

    public ChartGame(String accountType,String date, String userId, ArrayList<InstallNewGame> installNewGameArrayList) {
        this.accountType = accountType;
        this.date = date;
        this.userId = userId;
        this.installNewGameArrayList = installNewGameArrayList;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public ArrayList<InstallNewGame> getInstallNewGameArrayList() {
        return installNewGameArrayList;
    }

    public void setInstallNewGameArrayList(ArrayList<InstallNewGame> installNewGameArrayList) {
        this.installNewGameArrayList = installNewGameArrayList;
    }
}
