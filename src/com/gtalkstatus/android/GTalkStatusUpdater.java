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
import android.content.ServiceConnection;
import android.content.Context;
import android.widget.Toast;
import android.app.Service;
import android.os.IBinder;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.app.PendingIntent;

import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.NullPointerException;

import org.jivesoftware.smack.XMPPException; 

import com.android.music.IMediaPlaybackService;

public class GTalkStatusUpdater extends Service {
    
    public static final String LOG_NAME = "GTalkStatusUpdater";

    @Override
    public void onCreate() {
        super.onCreate();
        
    }

    public int onStartCommand(Intent aIntent, int aFlags, int aStartId) {
        
        onStart(aIntent, aStartId);

        return 2;
    }

    @Override
    public void onStart(Intent aIntent, int aStartId) {

		Log.d(LOG_NAME, aIntent.getAction());

        if (aIntent.getAction().equals("com.android.music.playbackcomplete")) {
            // The song has ended, stop the service
            stopSelf();
        } else if (aIntent.getAction().equals("com.android.music.playstatechanged") 
                || aIntent.getAction().equals("com.android.music.metachanged")
                || aIntent.getAction().equals("com.android.music.queuechanged")) {

            bindService(new Intent().setClassName("com.android.music", "com.android.music.MediaPlaybackService"), new ServiceConnection() {
        
                public void onServiceConnected(ComponentName aName, IBinder aService) {
                    IMediaPlaybackService service = IMediaPlaybackService.Stub.asInterface(aService);

                    try {
                        // We disconnect from XMPP if we don't need to keep the connection alive.
                        // Reconnect if necessary.
                        if (! GTalkStatusApplication.getInstance().getConnector().isConnected()) {
                            GTalkStatusApplication.getInstance().updateConnection();
                        }

                        String currentTrack = service.getTrackName();
                        String currentArtist = service.getArtistName();

                        if (service.isPlaying()) {
                            String statusMessage = "\u266B " + currentArtist + " - " + currentTrack;

                            GTalkStatusApplication.getInstance().getConnector().setStatus(statusMessage);
                        } else {
                            GTalkStatusApplication.getInstance().getConnector().disconnect();
                            stopSelf();
                        }
                    } catch (IllegalStateException e) { 
						GTalkStatusNotifier.notify(GTalkStatusApplication.getInstance(), GTalkStatusNotifier.ERROR);
                        stopSelf();
                    } catch (NullPointerException e) {
                        Log.w(LOG_NAME, "Service was never connected!");
                        GTalkStatusNotifier.notify(GTalkStatusApplication.getInstance(), GTalkStatusNotifier.ERROR);
                        stopSelf();
                    } catch (XMPPException e) {
                        Log.w(LOG_NAME, "No connection to XMPP server!");
                        GTalkStatusNotifier.notify(GTalkStatusApplication.getInstance(), GTalkStatusNotifier.ERROR);
                        stopSelf();
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
        } else if ((aIntent.getAction().equals("com.htc.music.playstatechanged") && aIntent.getIntExtra("id", -1) != -1)
                || aIntent.getAction().equals("com.htc.music.metachanged")) {

            /** EXPERIMENTAL HTC MUSIC PLAYER SUPPORT **/

            bindService(new Intent().setClassName("com.htc.music", "com.htc.music.MediaPlaybackService"), new ServiceConnection() {

                public void onServiceConnected(ComponentName aName, IBinder aService) {

                    com.htc.music.IMediaPlaybackService s = com.htc.music.IMediaPlaybackService.Stub.asInterface(aService);

                    IMediaPlaybackService service = IMediaPlaybackService.Stub.asInterface(aService);

                    try {
                        // We disconnect from XMPP if we don't need to keep the connection alive.
                        // Reconnect if necessary.
                        if (! GTalkStatusApplication.getInstance().getConnector().isConnected()) {
                            GTalkStatusApplication.getInstance().updateConnection();
                        }

                        String currentTrack = service.getTrackName();
                        String currentArtist = service.getArtistName();

                        if (service.isPlaying()) {
                            String statusMessage = "\u266B " + currentArtist + " - " + currentTrack;

                            GTalkStatusApplication.getInstance().getConnector().setStatus(statusMessage);
                        } else {
                            GTalkStatusApplication.getInstance().getConnector().disconnect();
                            stopSelf();
                        }
                    } catch (IllegalStateException e) { 
                        GTalkStatusNotifier.notify(GTalkStatusApplication.getInstance(), GTalkStatusNotifier.ERROR);
                        stopSelf();
                    } catch (NullPointerException e) {
                        Log.w(LOG_NAME, "Service was never connected!");
                        GTalkStatusNotifier.notify(GTalkStatusApplication.getInstance(), GTalkStatusNotifier.ERROR);
                        stopSelf();
                    } catch (XMPPException e) {
                        Log.w(LOG_NAME, "No connection to XMPP server!");
                        GTalkStatusNotifier.notify(GTalkStatusApplication.getInstance(), GTalkStatusNotifier.ERROR);
                        stopSelf();
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
        } else if (aIntent.getAction().equals("com.gtalkstatus.android.statusupdate")) {

            Log.d(LOG_NAME, "Found Generic Intent");
            Bundle extras = aIntent.getExtras();

            try {
                // We disconnect from XMPP if we don't need to keep the connection alive.
                // Reconnect if necessary.
                if (! GTalkStatusApplication.getInstance().getConnector().isConnected()) {
                    GTalkStatusApplication.getInstance().updateConnection();
                }

                String currentTrack = extras.getString("track");
                String currentArtist = extras.getString("artist");


                Log.d(LOG_NAME, extras.getString("state"));

                if (extras.getString("state").equals("is_playing")) {
                    String statusMessage = "\u266B " + currentArtist + " - " + currentTrack;

                    GTalkStatusApplication.getInstance().getConnector().setStatus(statusMessage);
                } else {
                    GTalkStatusApplication.getInstance().getConnector().disconnect();
                    stopSelf();
                }
            } catch (IllegalStateException e) { 
                GTalkStatusNotifier.notify(this, GTalkStatusNotifier.ERROR);
                stopSelf();
            } catch (NullPointerException e) {
                Log.w(LOG_NAME, "Service was never connected!");
                GTalkStatusNotifier.notify(this, GTalkStatusNotifier.ERROR);
                stopSelf();
            } catch (XMPPException e) {
                Log.w(LOG_NAME, "XMPPException");
                GTalkStatusNotifier.notify(this, GTalkStatusNotifier.ERROR);
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_NAME, e.toString());
                throw new RuntimeException(e);
            }
        }
    }
               

    public IBinder onBind(Intent aIntent) {

        return null;
    }
}
