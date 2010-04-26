package com.android.gtalkstatus;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import com.android.gtalkstatus.GTalkStatusUpdater;

public class GTalkStatusActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);

        Log.e("GTalkStatusActivity", "STARTING!!!");
        Log.e("GTalkStatusActivity", "STARTING!!!");
        Log.e("GTalkStatusActivity", "STARTING!!!");
        Log.e("GTalkStatusActivity", "STARTING!!!");
        Log.e("GTalkStatusActivity", "STARTING!!!");
        Log.e("GTalkStatusActivity", "STARTING!!!");
        startService(new Intent(GTalkStatusActivity.this, GTalkStatusUpdater.class));
        Log.e("GTalkStatusActivity", "STARTED!!!");
        Log.e("GTalkStatusActivity", "STARTED!!!");
        Log.e("GTalkStatusActivity", "STARTED!!!");
        Log.e("GTalkStatusActivity", "STARTED!!!");
        Log.e("GTalkStatusActivity", "STARTED!!!");
        Log.e("GTalkStatusActivity", "STARTED!!!");
        Log.e("GTalkStatusActivity", "STARTED!!!");
    }
}
