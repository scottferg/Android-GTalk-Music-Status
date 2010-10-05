/***************************************************************************
 *   Copyright 2010 Scott Ferguson                                         *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.         *
 ***************************************************************************/
package com.gtalkstatus.android;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.util.Log;
import android.content.Context;
import android.app.NotificationManager;

public class GTalkStatusSettingsActivity extends PreferenceActivity
{
    private GTalkEditCredentials mCredentialsPreference;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle mSavedInstanceState)
    {
        super.onCreate(mSavedInstanceState);

        addPreferencesFromResource(R.layout.settings);

        mCredentialsPreference = (GTalkEditCredentials) findPreference("edit_credentials");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
