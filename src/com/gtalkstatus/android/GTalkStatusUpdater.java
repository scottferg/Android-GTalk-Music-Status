/***************************************************************************
 *   Copyright 2010 Scott Ferguson                                         *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.         *
 ***************************************************************************/
package com.gtalkstatus.android;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ServiceConnection;
import android.widget.Toast;
import android.app.Service;
import android.os.IBinder;
import android.os.RemoteException;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.app.PendingIntent;

import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.NullPointerException;

import java.util.*;

import org.jivesoftware.smack.XMPPException; 

import com.android.music.IMediaPlaybackService;

public class GTalkStatusUpdater extends Service {
    
    public static final String TAG = "GTalkStatusUpdater";
    public static final String REQUEST_SONG_INTENT = "com.gtalkstatus.android.REQUEST_SONG";
    public static final String SONG_RESPONSE_INTENT = "com.gtalkstatus.android.SONG_RESULT";

    private BroadcastReceiver mSongRequestReceiver;
    private String mArtist;
    private String mTrack;

    @Override
    public void onCreate() {
        super.onCreate();

        mSongRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context aContext, Intent aIntent) {
                sendSongInfoIntent();
            }
        };

        this.registerReceiver(this.mSongRequestReceiver, 
                new IntentFilter(REQUEST_SONG_INTENT));
    }

    public int onStartCommand(Intent aIntent, int aFlags, int aStartId) {
        onStart(aIntent, aStartId);

        return 2;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSongRequestReceiver);

        mArtist = null;
        mTrack = null;

        sendSongInfoIntent();
    }

    @Override
    public void onStart(final Intent aIntent, int aStartId) {

		Log.d(TAG, aIntent.getAction());

        if (aIntent.getAction().equals("com.android.music.playbackcomplete") ||
                aIntent.getAction().equals("com.doubleTwist.androidPlayer.playbackcomplete")) {
            stopSelf();
        } else if (aIntent.getAction().equals("com.doubleTwist.androidPlayer.playstatechanged")
            || aIntent.getAction().equals("com.doubleTwist.androidPlayer.metachanged")) {
            Log.i(TAG, "DoubleTwist changed!");

            try {
                mArtist = aIntent.getStringExtra("artist");
                mTrack = aIntent.getStringExtra("track");

                setStatus(aIntent.getBooleanExtra("playing", false));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                throw new RuntimeException(e);
            }

        } else if (aIntent.getAction().equals("com.android.music.playstatechanged") 
                || aIntent.getAction().equals("com.android.music.metachanged")
                || aIntent.getAction().equals("com.android.music.queuechanged")) {

            bindService(new Intent().setClassName("com.android.music", "com.android.music.MediaPlaybackService"), new ServiceConnection() {
                public void onServiceConnected(ComponentName aName, IBinder aService) {
                    IMediaPlaybackService service = IMediaPlaybackService.Stub.asInterface(aService);

                    try {
                        mArtist = service.getArtistName();
                        mTrack = service.getTrackName();

                        setStatus(service.isPlaying());

                        unbindService(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        throw new RuntimeException(e);
                    }
                }

                public void onServiceDisconnected(ComponentName aName) {
                    GTalkStatusApplication.getInstance().getConnector().setStatus("", 0);
                }
            }, 0);
        } else if ((aIntent.getAction().equals("com.htc.music.playstatechanged") && aIntent.getIntExtra("id", -1) != -1)
                || aIntent.getAction().equals("com.htc.music.metachanged")) {
            /** EXPERIMENTAL HTC MUSIC PLAYER SUPPORT **/
            bindService(new Intent().setClassName("com.htc.music", "com.htc.music.MediaPlaybackService"), new ServiceConnection() {
                public void onServiceConnected(ComponentName aName, IBinder aService) {
                    com.htc.music.IMediaPlaybackService service = com.htc.music.IMediaPlaybackService.Stub.asInterface(aService);

                    try {
                        mArtist = service.getArtistName();
                        mTrack = service.getTrackName();

                        setStatus(service.isPlaying());

                        unbindService(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        throw new RuntimeException(e);
                    }
                }

                public void onServiceDisconnected(ComponentName aName) {
                    GTalkStatusApplication.getInstance().getConnector().setStatus("", 0);
                }
            }, 0);
        } else if (aIntent.getAction().equals("com.gtalkstatus.android.statusupdate")) {
            Log.d(TAG, "Found Generic Intent");
            Bundle extras = aIntent.getExtras();

            try {
                mArtist = extras.getString("artist");
                mTrack = extras.getString("track");

                setStatus(true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                throw new RuntimeException(e);
            }
        }
    }

    public IBinder onBind(Intent aIntent) {
        return null;
    }

    private void setStatus(boolean aPlaying) {
        try {
            // We disconnect from XMPP if we don't need to keep the connection alive.
            // Reconnect if necessary.
            if (! GTalkStatusApplication.getInstance().getConnector().isConnected()) {
                GTalkStatusApplication.getInstance().updateConnection();
            }

            if (aPlaying) {
                String statusMessage = "\u266B " + mArtist + " - " + mTrack;

                GTalkStatusApplication.getInstance().getConnector().setStatus(statusMessage);
                sendSongInfoIntent();
            } else {
                GTalkStatusApplication.getInstance().getConnector().disconnect();
                stopSelf();
            }
        } catch (IllegalStateException e) { 
            GTalkStatusNotifier.notify(this, GTalkStatusNotifier.ERROR);
            stopSelf();
        } catch (NullPointerException e) {
            Log.w(TAG, "Service was never connected!");
            GTalkStatusNotifier.notify(this, GTalkStatusNotifier.ERROR);
            stopSelf();
        } catch (XMPPException e) {
            Log.w(TAG, "XMPPException");
            GTalkStatusNotifier.notify(this, GTalkStatusNotifier.ERROR);
            stopSelf();
        }
    }

    private void sendSongInfoIntent() {
        Intent result = new Intent();
        result.setAction(SONG_RESPONSE_INTENT);
        result.putExtra("artist", mArtist);
        result.putExtra("track", mTrack);

        sendBroadcast(result);
    }
}
