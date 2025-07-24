package com.example.POMicroservice.DTO;

public class POItemNumber {

    private String poitemnumber;

    public POItemNumber(String poItemNumber) {this.poitemnumber = poItemNumber;}


    public String getPoitemnumber() {
        return poitemnumber;
    }

    public void setPoitemnumber(String poitemnumber) {
        this.poitemnumber = poitemnumber;
    }
}

