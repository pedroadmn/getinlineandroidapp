package com.androidapp.getinline.entities;

import android.content.Intent;

public class NotificationEvent {
    private Intent content;

    public NotificationEvent(Intent content ){
        this.content = content;
    }

    public Intent getContent(){
        return content;
    }
}