package com.android.gtalkstatus;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.music.IMediaPlaybackService;
import com.android.gtalkstatus.XMPPTransfer;

public class MediaPlaybackServiceConnection implements ServiceConnection {

    public static final String LOG_NAME = "MediaPlaybackServiceConnection";

    private String mCurrentArtist;
    private String mCurrentTrack;
    private IMediaPlaybackService mService;

    public void onServiceConnected(ComponentName aName, IBinder aService) {

        Log.i(LOG_NAME, "Connected! Name: " + aName.getClassName());

        mService = IMediaPlaybackService.Stub.asInterface(aService);

        getUpdate();
    }

    public void onServiceDisconnected(ComponentName aName) {
        Log.i(LOG_NAME, "Service disconnected");
    }

    public void getUpdate() {

        if (GTalkStatusApplication.getInstance().getConnector() == null) {
            Log.i(LOG_NAME, "Updating connection!");
            GTalkStatusApplication.getInstance().updateConnection();
        }

        XMPPTransfer gTalkConnector = GTalkStatusApplication.getInstance().getConnector();

        try {
            String trackName = mService.getTrackName();

            if (trackName != mCurrentTrack) {
                mCurrentTrack = trackName;
                mCurrentArtist = mService.getArtistName();

                if (mService.isPlaying()) {

                    String statusMessage = "\u266B " + mCurrentArtist + " - " + mCurrentTrack;

                    gTalkConnector.setStatus(statusMessage);
                } else {
                    // No need to report anything, music is stopped
                    gTalkConnector.setStatus("", 0);
                }
            }

            Log.i(LOG_NAME, "Playing track: " + mCurrentTrack);
            Log.i(LOG_NAME, "Playing artist: " + mCurrentArtist);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
