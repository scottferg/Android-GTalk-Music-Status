package com.android.gtalkstatus;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.music.IMediaPlaybackService;
import com.android.gtalkstatus.XMPPTransfer;

import com.android.gtalkstatus.GTalkStatusGlobal;

public class MediaPlaybackServiceConnection implements ServiceConnection {

    public static final String LOG_NAME = "MediaPlaybackServiceConnection";

    private String mCurrentArtist;
    private String mCurrentTrack;
    private String mUsername;
    private String mPassword;
    private IMediaPlaybackService mService;
    private XMPPTransfer mGTalkConnector;

    public MediaPlaybackServiceConnection(String aUsername, String aPassword) {

        mUsername = aUsername;
        mPassword = aPassword;
    }

    public void onServiceConnected(ComponentName aName, IBinder aService) {

        Log.i(LOG_NAME, "Connected! Name: " + aName.getClassName());

        mService = IMediaPlaybackService.Stub.asInterface(aService);
        // Cache this connection
        mGTalkConnector = new XMPPTransfer(mUsername, mPassword);

        getUpdate();
    }

    public void onServiceDisconnected(ComponentName aName) {
        Log.i(LOG_NAME, "Service disconnected");
        // Set a blank status message
        mGTalkConnector.setStatus("", 0);
    }

    public void getUpdate() {

        try {
            String trackName = mService.getTrackName();

            if (trackName != mCurrentTrack) {
                mCurrentTrack = trackName;
                mCurrentArtist = mService.getArtistName();

                if (mService.isPlaying()) {

                    String statusMessage = "\u266B " + mCurrentArtist + " - " + mCurrentTrack;

                    mGTalkConnector.setStatus(statusMessage);
                } else {
                    // No need to report anything, music is stopped
                    mGTalkConnector.setStatus("", 0);
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
