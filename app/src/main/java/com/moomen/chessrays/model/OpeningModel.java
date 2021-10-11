package com.moomen.chessrays.model;

public class OpeningModel {
    String openingId;
    String openingName;

    public OpeningModel() {
    }

    public OpeningModel(String openingId, String openingName) {
        this.openingId = openingId;
        this.openingName = openingName;
    }

    public String getOpeningId() {
        return openingId;
    }

    public void setOpeningId(String openingId) {
        this.openingId = openingId;
    }

    public String getOpeningName() {
        return openingName;
    }

    public void setOpeningName(String openingName) {
        this.openingName = openingName;
    }
}
