package com.android.gtalkstatus;

import android.preference.DialogPreference;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;
import android.view.View;

public class GTalkEditCredentials extends DialogPreference {

    private EditText mUsername;
    private EditText mPassword;

    public GTalkEditCredentials(Context aContext, AttributeSet aAttributes) {

        super(aContext, aAttributes);
        setDialogLayoutResource(R.layout.credentials);
    }

    @Override
    protected void onBindDialogView(View aView) {
        mUsername = (EditText) aView.findViewById(R.id.txtUsername);
        mPassword = (EditText) aView.findViewById(R.id.txtPassword);
    }

    @Override
    protected void onDialogClosed(boolean aPositiveResult) {

        super.onDialogClosed(aPositiveResult);

        if (aPositiveResult) {
            SharedPreferences prefs = getContext().getSharedPreferences("GTalkStatusPrefs", 0);
            Editor prefsEditor = prefs.edit();

            prefsEditor.putString("USERNAME", mUsername.getText().toString());
            prefsEditor.putString("PASSWORD", mPassword.getText().toString());
            prefsEditor.commit();

            GTalkStatusApplication.getInstance().updateConnection();
        }
    }
}
