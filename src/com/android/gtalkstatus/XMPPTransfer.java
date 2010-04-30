package com.android.gtalkstatus;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.XMPPException; 

public class XMPPTransfer {

    private XMPPConnection mConnection;

    public XMPPTransfer(String aUsername, String aPassword) {

        ConnectionConfiguration connectionConfig = 
            new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");

        mConnection = new XMPPConnection(connectionConfig);

        try {
            mConnection.connect();
            mConnection.login(aUsername, aPassword);

        } catch (XMPPException e) {
            // Probably should handle this better
            e.printStackTrace();
        }
    }

    public void disconnect() {
        mConnection.disconnect();
    }

    public boolean isConnected() {
        return mConnection.isConnected();        
    }

    public void setStatus(String aStatusMessage) {
        setStatus(aStatusMessage, 25);
    }

    public void setStatus(String aStatusMessage, int aPriority) {

        Presence presence = new Presence(Presence.Type.available);

        presence.setStatus(aStatusMessage);
        presence.setPriority(aPriority);
        presence.setMode(Presence.Mode.available);

        mConnection.sendPacket(presence);
    }
}
