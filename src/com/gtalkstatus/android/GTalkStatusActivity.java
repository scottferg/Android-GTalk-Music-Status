package com.gtalkstatus.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

public class GTalkStatusActivity extends Activity {
    private static final String TAG = "GTalkStatus";

    private GTalkStatusCurrentModel sModel = GTalkStatusCurrentModel.getInstance();

    private BroadcastReceiver mSongResponseReceiver;
    private Button mActionButton;
    private MenuItem mSettingsMenu;
    private SharedPreferences mPrefs;
    private TextView mCurrentStatus;
    private TextView mLoginStatus;
    private View mActionView;

    @Override
    public void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);

        mPrefs = getSharedPreferences("GTalkStatusPrefs", MODE_PRIVATE);

        mSongResponseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context aContext, Intent aIntent) {
                setCurrentTrack(aIntent.getStringExtra("artist"), aIntent.getStringExtra("track"));
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.main);

        mCurrentStatus = (TextView) findViewById(R.id.current_status);
        mLoginStatus = (TextView) findViewById(R.id.account_status);

        this.registerReceiver(this.mSongResponseReceiver, 
                new IntentFilter(GTalkStatusUpdater.SONG_RESPONSE_INTENT));

        if (mPrefs.getBoolean("LOGGEDIN", false)) {
            mLoginStatus.setText("Logged in");
            mActionView = ((ViewStub) findViewById(R.id.current_playing_track_view)).inflate();

            mActionButton = (Button) mActionView.findViewById(R.id.share_button);
            mActionButton.setOnClickListener(onShareClick);
        } else {
            mLoginStatus.setText(getResources().getString(R.string.login_error));
            mCurrentStatus.setText(getResources().getString(R.string.no_music));

            mActionView = ((ViewStub) findViewById(R.id.no_current_track_view)).inflate();

            mActionButton = (Button) mActionView.findViewById(R.id.login_button);
            mActionButton.setOnClickListener(onLoginClick);
        }

        requestCurrentTrack();
    }

    @Override
    public void onPause() {
        super.onPause();

        sModel.setArtist(null);
        sModel.setTrack(null);

        unregisterReceiver(mSongResponseReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, aMenu);

        mSettingsMenu = aMenu.findItem(R.id.settings);

        mSettingsMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem mItem) {
                startActivity(new Intent().setClass(GTalkStatusActivity.this,
                    GTalkStatusSettingsActivity.class));
                return true;
            }
        });

        return true;
    }

    private View.OnClickListener onLoginClick = new View.OnClickListener() {
        public void onClick(View aView) {
            startActivity(new Intent().setClass(GTalkStatusActivity.this,
                GTalkStatusSettingsActivity.class));
        }
    };

    private View.OnClickListener onShareClick = new View.OnClickListener() {
        public void onClick(View aView) {
            shareCurrentTrack();
        }
    };

    private void setCurrentTrack(String aArtist, String aTrack) {
        sModel.setArtist(aArtist);
        sModel.setTrack(aTrack);

        mCurrentStatus.setText(aArtist + " - " + aTrack);
    }

    private void requestCurrentTrack() {
        sendBroadcast(new Intent().setAction(GTalkStatusUpdater.REQUEST_SONG_INTENT));
    }

    private void shareCurrentTrack() {
        if (sModel.getTrack() == null || sModel.getArtist() == null) {
            return;
        }

        Intent shareIntent = new Intent();
        String currentTrackMessage = "Now listening to: " + sModel.getTrack() + " by " + sModel.getArtist();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentTrackMessage);

        startActivity(Intent.createChooser(shareIntent, null));
    }
}
