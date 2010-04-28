package com.android.gtalkstatus;

import android.preference.DialogPreference;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;
import android.view.View;

import com.android.gtalkstatus.GTalkStatusGlobal;
import com.android.gtalkstatus.GTalkStatusUpdater;

public class GTalkEditCredentials extends DialogPreference {

    private String mUsername;
    private String mPassword;

    public GTalkEditCredentials(Context aContext, AttributeSet aAttributes) {

        super(aContext, aAttributes);
        setDialogLayoutResource(R.layout.credentials);
    }

    @Override
    protected void onBindDialogView(View aView) {
        mUsername = ((EditText) aView.findViewById(R.id.txtUsername)).toString();
        mPassword = ((EditText) aView.findViewById(R.id.txtPassword)).toString();
    }

    @Override
    protected void onDialogClosed(boolean aPositiveResult) {

        super.onDialogClosed(aPositiveResult);

        if (aPositiveResult) {
            SharedPreferences prefs = getContext().getSharedPreferences("StatusPrefs", 0);
            Editor prefsEditor = prefs.edit();

            prefsEditor.putString("USERNAME", mUsername);
            prefsEditor.putString("PASSWORD", mPassword);
            prefsEditor.commit();
                
            /*
            getContext().startService(new Intent(GTalkEditCredentials.this, GTalkStatusUpdater.class));
            */
        }
    }
}
