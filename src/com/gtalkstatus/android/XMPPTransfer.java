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

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.XMPPException; 

public class XMPPTransfer {

    private XMPPConnection mConnection;

    public XMPPTransfer(String aUsername, String aPassword) 
        throws XMPPException {

        ConnectionConfiguration connectionConfig = 
            new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");

        mConnection = new XMPPConnection(connectionConfig);

        try {
            mConnection.connect();
            mConnection.login(aUsername, aPassword);
        } catch (XMPPException e) {
            throw new XMPPException();
        }
    }

    public void disconnect() {
        mConnection.disconnect();
    }

    public boolean isConnected() {
        return mConnection.isConnected();        
    }

    public void setStatus(String aStatusMessage) {
        // Turn it up to 11
        setStatus(aStatusMessage, 100);
    }

    public void setStatus(String aStatusMessage, int aPriority) {

        Presence presence = new Presence(Presence.Type.available);

        presence.setStatus(aStatusMessage);
        presence.setPriority(aPriority);
        presence.setMode(Presence.Mode.available);

        mConnection.sendPacket(presence);
    }
}
