package com.android.gtalkstatus;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.music.IMediaPlaybackService;
import com.android.gtalkstatus.XMPPTransfer;

public class MediaPlaybackServiceConnection implements ServiceConnection {

    public static final String LOG_NAME = "MediaPlaybackServiceConnection";

    public String mCurrentArtist;
    public String mCurrentTrack;
    public IMediaPlaybackService mService;

    public void onServiceConnected(ComponentName aName, IBinder aService) {

        Log.i(LOG_NAME, "Connected! Name: " + aName.getClassName());

        mService = IMediaPlaybackService.Stub.asInterface(aService);

        getUpdate();
    }

    public void onServiceDisconnected(ComponentName aName) {
        Log.i(LOG_NAME, "Service disconnected");
    }

    public void getUpdate() {

        try {
            String trackName = mService.getTrackName();

            if (trackName != mCurrentTrack) {
                mCurrentTrack = trackName;
                mCurrentArtist = mService.getArtistName();

                if (mService.isPlaying()) {
                    String statusMessage = "Listening to: " + mCurrentArtist + " - " + mCurrentTrack;

                    XMPPTransfer gtalkConnector = new XMPPTransfer("username", "password");
                    gtalkConnector.setStatus(statusMessage);
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
