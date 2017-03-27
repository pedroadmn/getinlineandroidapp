package com.androidapp.getinline.service;

import com.androidapp.getinline.entities.TokenEvent;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.greenrobot.eventbus.EventBus;

public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        TokenEvent tokenEvent = new TokenEvent(token);
        EventBus.getDefault().post(tokenEvent);
    }
}