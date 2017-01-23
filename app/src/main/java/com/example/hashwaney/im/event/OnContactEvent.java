package com.example.hashwaney.im.event;

/**
 * Created by HashWaney on 2017/1/23.
 */

public class OnContactEvent {
    private String username;
    private boolean isAdded;


    public OnContactEvent(String username, boolean isAdded) {
        this.username = username;
        this.isAdded = isAdded;
    }
}
