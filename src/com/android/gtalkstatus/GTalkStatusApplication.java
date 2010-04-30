package com.android.gtalkstatus;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.Context;

public class GTalkStatusApplication extends Application {

    private static XMPPTransfer mGTalkConnector;
    private final static String LOG_NAME = "GTalkStatusApplication";

    public static GTalkStatusApplication sInstance = null;

    public static GTalkStatusApplication getInstance() {
        
        if (sInstance != null) {
            return sInstance;
        } else {
            return new GTalkStatusApplication();
        }
    }

    @Override
    public void onCreate() {

        sInstance = this;

        updateConnection();
    }

    @Override
    public void onTerminate() {

        mGTalkConnector.disconnect();

        super.onTerminate();
    }

    public void startService(Context aContext, Intent aIntent) {

        Intent serviceIntent = new Intent(this, GTalkStatusUpdater.class);
        serviceIntent.setAction(aIntent.getAction());
        serviceIntent.putExtras(aIntent);

        aContext.startService(serviceIntent);
    }

    public void updateConnection() {

        if (mGTalkConnector != null) {
            mGTalkConnector.disconnect();
        }

        SharedPreferences settings = getSharedPreferences("GTalkStatusPrefs", 0);
        String username = settings.getString("USERNAME", "");
        String password = settings.getString("PASSWORD", "");

        mGTalkConnector = new XMPPTransfer(username, password);
    }

    public XMPPTransfer getConnector() {

        return mGTalkConnector;
    }
}
