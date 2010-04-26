package com.android.gtalkstatus;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ServiceConnection;

import com.android.gtalkstatus.MediaPlaybackServiceConnection;
import com.android.music.IMediaPlaybackService;

public class GTalkStatusActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);

        Intent serviceIntent = new Intent();
        serviceIntent.setClassName("com.android.music", "com.android.music.MediaPlaybackService");
        ServiceConnection connection = new MediaPlaybackServiceConnection();

        this.bindService(serviceIntent, connection, 0);
    }
}
