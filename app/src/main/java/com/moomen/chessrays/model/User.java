package com.moomen.chessrays.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @Keep
    @SerializedName("name")
    String name;
    @SerializedName("date")
    String date;
    @SerializedName("userID")
    String userID;

    public User() {
    }

    public User(String name, String date, String userID) {
        this.name = name;
        this.date = date;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
