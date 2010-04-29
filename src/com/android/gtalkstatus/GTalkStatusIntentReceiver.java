package com.android.gtalkstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GTalkStatusIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context aContext, Intent aIntent) {

        GTalkStatusApplication.getInstance().startService();
    }
}
