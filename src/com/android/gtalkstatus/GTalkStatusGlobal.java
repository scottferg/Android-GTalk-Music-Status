package com.android.gtalkstatus;

import android.app.Application;

public class GTalkStatusGlobal extends Application {

    private String mUsername;
    private String mPassword;

    public String getUsername() {

        return mUsername;
    }

    public void setUsername(String aUsername) {

        mUsername = aUsername;
    }

    public String getPassword() {

        return mPassword;
    }
    
    public void setPassword(String aPassword) {

        mPassword = aPassword;
    }
}
