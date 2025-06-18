package com.example.POMicroservice.POMicroservice.DTO;


import lombok.Getter;
import lombok.Setter;


public class GetUserRequest {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
