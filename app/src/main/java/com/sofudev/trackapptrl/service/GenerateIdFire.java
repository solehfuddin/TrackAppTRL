package com.sofudev.trackapptrl.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.LoginSession;
import com.sofudev.trackapptrl.Data.Data_token_fcm;

import java.util.HashMap;
import java.util.Map;

public class GenerateIdFire extends FirebaseInstanceIdService {
    private static final String TAG   = GenerateIdFire.class.getSimpleName();
    private static final String TOPIC = "trl";
    LoginSession session;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Your token : " + token);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        session = new LoginSession(getApplicationContext());
        session.UpdateTokenSession(token);
    }
}
