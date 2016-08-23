package com.trelokopoi.dentist.dbutil;

public class diaryObj {

    private int id;
    private int prodId;
    private int quantity;
    private String date;
    private String time;
    private int belongs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getBelongs() {
        return belongs;
    }

    public void setBelongs(int belongs) {
        this.belongs = belongs;
    }

}