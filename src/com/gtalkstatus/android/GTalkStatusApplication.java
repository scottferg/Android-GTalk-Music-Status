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

import android.app.Application;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;

import org.jivesoftware.smack.XMPPException; 

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

        try {
            updateConnection();
        } catch (Exception e) {
            // Do nothing here.  If there is an error when attempting to connect, we'll want
            // to catch it when the user tries playing music.
        }
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

    public void updateConnection() throws XMPPException {

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
