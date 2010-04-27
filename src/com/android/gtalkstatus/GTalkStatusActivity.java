package com.android.gtalkstatus;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.util.Log;

import com.android.gtalkstatus.GTalkStatusGlobal;
import com.android.gtalkstatus.GTalkStatusUpdater;

public class GTalkStatusActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle mSavedInstanceState)
    {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.main);

        Button loginButton = (Button) findViewById(R.id.btnSubmit);
        final EditText usernameText = (EditText) findViewById(R.id.txtUsername);
        final EditText passwordText = (EditText) findViewById(R.id.txtPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View aView) {
                
                // TODO: Store this in a shared preferences file and only display UI
                //       if necessary.
                GTalkStatusGlobal appState = ((GTalkStatusGlobal) getApplicationContext());
                appState.setUsername(usernameText.getText().toString());
                appState.setPassword(passwordText.getText().toString());
        
                startService(new Intent(GTalkStatusActivity.this, GTalkStatusUpdater.class));
            }
        });
    }
}
