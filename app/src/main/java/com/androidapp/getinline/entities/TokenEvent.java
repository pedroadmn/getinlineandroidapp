package com.androidapp.getinline.entities;

public class TokenEvent {
    private String token;

    public TokenEvent(String token ){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}