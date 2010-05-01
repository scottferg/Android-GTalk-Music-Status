package com.gtalkstatus.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.music.IMediaPlaybackService;

public class GTalkStatusIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context aContext, Intent aIntent) {

        GTalkStatusApplication.getInstance().startService(aContext, aIntent);
    }
}
