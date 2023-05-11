package com.dnlab.tack_together.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class StompClientSingleton {

    private static final String TAG = "[StompService]";

    private static final String STOMP_URL = "ws://192.168.64.51:8082/match?token=";
    private static final String TOKEN_TEMP = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJzdG9tcCIsImlhdCI6MTY4MzcxODMzMywiZXhwIjoxNjg0OTI3OTMzLCJhdXRoIjoiUk9MRV9NRU1CRVIifQ._jFzZkAAigA0cSr6euukw21meCWeiFbYvVtK70cAjPVUsYA9Pswam4KpjjvHZ5UO";

    private static StompClientSingleton instance = null;
    private static StompClient stompClient;

    private static List<StompHeader> headerList;

    public static StompClientSingleton getInstance(Context context){
        if(instance == null){
            instance = new StompClientSingleton();
            initStompClient(context);
        }
        return instance;
    }

    public static StompClient getStompClient(){
        return stompClient;
    }

    private static void initStompClient(Context context){
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, STOMP_URL+TOKEN_TEMP);
        Disposable dispLifecycle = stompClient.lifecycle().subscribe(lifecycleEvent -> {
            boolean isUnexpectedClosed = false;
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;
                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    if(lifecycleEvent.getException().getMessage().contains("EOF")){
                        isUnexpectedClosed=true;
                    }
                    break;
                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    if(isUnexpectedClosed){
                        /**
                         * EOF Error
                         */
                        initStompClient(context);
                        isUnexpectedClosed=false;
                    }
                    break;
            }
        });
        // add Header
        headerList=new ArrayList<>();
        headerList.add(new StompHeader("Authorization", "Bearer " + TOKEN_TEMP));
        stompClient.connect(headerList);
    }


}
