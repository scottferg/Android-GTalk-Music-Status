package com.gtalkstatus.android;

import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

public class GTalkStatusNotifier {

	static final int WARNING = 0;
	static final int ERROR = 1;
	static final int SCROBBLE = 2;
	static final int CUSTOM = 3;

    static final void notify(Context app, int type) { 

        NotificationManager mNotificationManager = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = 0;
        String sType = "";

        switch(type) {
            case ERROR:
                icon = android.R.drawable.stat_notify_error;
                sType = " - ERROR";
                break;
            default:
                icon = android.R.drawable.stat_notify_error;

        }

        // Occurs if the connection was never initialized
        CharSequence notificationText = "GTalk Status";


        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, notificationText, when);

        CharSequence title = "Music Status" + sType;
        CharSequence text = "Music Status was unable to connect to the Google Talk server.  Did you enter your username and password correctly?";

        Intent notificationIntent = new Intent(app.getApplicationContext(), GTalkStatusActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(app.getApplicationContext(), 0, notificationIntent, 0);

        notification.setLatestEventInfo(app.getApplicationContext(), title, text, contentIntent);

        mNotificationManager.notify(1, notification);
    }
}
