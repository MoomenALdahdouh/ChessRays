package com.moomen.chessrays.model;

public class CustomDataEntry {
    String date;
    String value;
    String value1;
    String value2;
    String value3;

    public CustomDataEntry() {
    }

    public CustomDataEntry(String date, String value, String value1, String value2, String value3) {
        this.date = date;
        this.value = value;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
