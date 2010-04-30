package com.android.gtalkstatus;

import android.content.Intent;
import android.content.ServiceConnection;
import android.app.Service;
import android.os.IBinder;
import android.util.Log;
import android.content.ComponentName;
import android.os.IBinder;

import java.util.TimerTask;
import java.util.Timer;

import com.android.music.IMediaPlaybackService;

public class GTalkStatusUpdater extends Service {

    public static final String LOG_NAME = "GTalkStatusUpdaterService";
    private static final int UPDATE_INTERVAL = 30000;

    private Timer mTimer = new Timer();
    private ServiceConnection mConnection;
    
    @Override
    public void onCreate() {

        super.onCreate();
    
        Log.i(LOG_NAME, "Service created");
    }

    @Override
    public int onStartCommand(Intent aIntent, int aFlags, int aStartId) {
        
        onStart(aIntent, aStartId);

        return START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent aIntent, int aStartId) {

        Log.i(LOG_NAME, aIntent.getAction());

        if (aIntent.getAction().equals("com.android.music.playbackcomplete")
                || aIntent.getAction().equals("com.htc.music.playbackcomplete")) {
            // The song has ended, stop the service
            Log.i(LOG_NAME, "Playback has been completed, stopping the service");
            stopSelf();
        } else if (aIntent.getAction().equals("com.android.music.playstatechanged") 
                || aIntent.getAction().equals("com.android.music.metachanged")
                || aIntent.getAction().equals("com.android.music.queuechanged")) {

            bindService(new Intent().setClassName("com.android.music", "com.android.music.MediaPlaybackService"), new ServiceConnection() {
        
                public void onServiceConnected(ComponentName aName, IBinder aService) {
                    IMediaPlaybackService service = IMediaPlaybackService.Stub.asInterface(aService);

                    try {
                        String currentTrack = service.getTrackName();
                        String currentArtist = service.getArtistName();

                        if (service.isPlaying()) {

                            Log.i(LOG_NAME, "Music playing");
                            String statusMessage = "\u266B " + currentArtist + " - " + currentTrack;

                            GTalkStatusApplication.getInstance().getConnector().setStatus(statusMessage);
                        } else {
                            Log.i(LOG_NAME, "Music is not playing");

                            GTalkStatusApplication.getInstance().getConnector().disconnect();
                            stopSelf();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                    unbindService(this);
                }

                public void onServiceDisconnected(ComponentName aName) {
                    Log.i(LOG_NAME, "Service disconnected");
                    GTalkStatusApplication.getInstance().getConnector().setStatus("", 0);
                }
            }, 0);
        }
    }

    public IBinder onBind(Intent aIntent) {

        return null;
    }
}
