package com.example.ratifyBackend.POMicroService.DTO;

import lombok.Getter;
import lombok.Setter;

public class POItemNumber {

    private String poitemnumber;

    public POItemNumber() {}

    public String getPoitemnumber() {
        return poitemnumber;
    }

    public void setPoitemnumber(String poitemnumber) {
        this.poitemnumber = poitemnumber;
    }
}

