package com.android.gtalkstatus;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.util.Log;

public class GTalkStatusActivity extends PreferenceActivity
{
    private GTalkEditCredentials mCredentialsPreference;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle mSavedInstanceState)
    {
        super.onCreate(mSavedInstanceState);

        addPreferencesFromResource(R.layout.main);

        mCredentialsPreference = (GTalkEditCredentials) findPreference("edit_credentials");

        mCredentialsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference aPreference, Object aValue) {
                Log.i("GTalkStatusActivity", aPreference.toString());
                Log.i("GTalkStatusActivity", aValue.toString());

                return true;
            }
        });
    }
}
