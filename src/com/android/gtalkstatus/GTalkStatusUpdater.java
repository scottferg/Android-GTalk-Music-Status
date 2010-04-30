package com.android.gtalkstatus;

import android.content.Intent;
import android.content.ServiceConnection;
import android.app.Service;
import android.os.IBinder;
import android.content.ComponentName;
import android.os.IBinder;
import android.util.Log;

import com.android.music.IMediaPlaybackService;

public class GTalkStatusUpdater extends Service {
    
    public static final String LOG_NAME = "GTalkStatusUpdater";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent aIntent, int aFlags, int aStartId) {
        
        onStart(aIntent, aStartId);

        return START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent aIntent, int aStartId) {

        if (aIntent.getAction().equals("com.android.music.playbackcomplete")) {
            // The song has ended, stop the service
            stopSelf();
        } else if (aIntent.getAction().equals("com.android.music.playstatechanged") 
                || aIntent.getAction().equals("com.android.music.metachanged")
                || aIntent.getAction().equals("com.android.music.queuechanged")) {

            bindService(new Intent().setClassName("com.android.music", "com.android.music.MediaPlaybackService"), new ServiceConnection() {
        
                public void onServiceConnected(ComponentName aName, IBinder aService) {
                    IMediaPlaybackService service = IMediaPlaybackService.Stub.asInterface(aService);

                    // We disconnect from XMPP if we don't need to keep the connection alive.
                    // Reconnect if necessary.
                    if (! GTalkStatusApplication.getInstance().getConnector().isConnected()) {
                        GTalkStatusApplication.getInstance().updateConnection();
                    }

                    try {
                        String currentTrack = service.getTrackName();
                        String currentArtist = service.getArtistName();

                        if (service.isPlaying()) {
                            String statusMessage = "\u266B " + currentArtist + " - " + currentTrack;

                            GTalkStatusApplication.getInstance().getConnector().setStatus(statusMessage);
                        } else {
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
                    GTalkStatusApplication.getInstance().getConnector().setStatus("", 0);
                }
            }, 0);
        }
    }

    public IBinder onBind(Intent aIntent) {

        return null;
    }
}
