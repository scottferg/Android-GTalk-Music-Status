package com.android.gtalkstatus;

import android.content.Intent;
import android.content.ServiceConnection;
import android.app.Service;
import android.os.IBinder;
import android.util.Log;
import java.util.TimerTask;
import java.util.Timer;

import com.android.gtalkstatus.MediaPlaybackServiceConnection;

public class GTalkStatusUpdater extends Service {

    public static final String LOG_NAME = "GTalkStatusUpdaterService";
    private static final int UPDATE_INTERVAL = 10000;

    private Timer mTimer = new Timer();
    private ServiceConnection mConnection;
    
    @Override
    public void onCreate() {

        super.onCreate();
    
        Log.i(LOG_NAME, "Service created");

        startService();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        stopService();
    }

    public IBinder onBind(Intent aIntent) {

        return null;
    }

    private void startService() {

        Intent serviceIntent = new Intent();

        serviceIntent.setClassName("com.android.music", "com.android.music.MediaPlaybackService");
        mConnection = new MediaPlaybackServiceConnection();

        Log.i(LOG_NAME, "Connection created");

        this.bindService(serviceIntent, mConnection, 0);

        mTimer.scheduleAtFixedRate(
            new TimerTask() {
                public void run() {
                    getMediaPlayerStatusUpdate();
                }
            },
            0,
            UPDATE_INTERVAL);
    }

    private void stopService() {

        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void getMediaPlayerStatusUpdate() {

        ((MediaPlaybackServiceConnection) mConnection).getUpdate();
    }
}
