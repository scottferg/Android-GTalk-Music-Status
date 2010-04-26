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
    private XMPPTransfer mGTalkConnector;

    public void onServiceConnected(ComponentName aName, IBinder aService) {

        Log.i(LOG_NAME, "Connected! Name: " + aName.getClassName());

        mService = IMediaPlaybackService.Stub.asInterface(aService);
        // Cache this connection
        mGTalkConnector = new XMPPTransfer("username", "password");

        getUpdate();
    }

    public void onServiceDisconnected(ComponentName aName) {
        Log.i(LOG_NAME, "Service disconnected");
        // Set a blank status message
        mGTalkConnector.setStatus("");
    }

    public void getUpdate() {

        try {
            String trackName = mService.getTrackName();

            if (trackName != mCurrentTrack) {
                mCurrentTrack = trackName;
                mCurrentArtist = mService.getArtistName();

                if (mService.isPlaying()) {

                    String statusMessage = "Listening to: " + mCurrentArtist + " - " + mCurrentTrack;

                    // TODO: Cache username and password
                    mGTalkConnector.setStatus(statusMessage);
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
