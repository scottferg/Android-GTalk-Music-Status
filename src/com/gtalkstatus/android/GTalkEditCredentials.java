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
