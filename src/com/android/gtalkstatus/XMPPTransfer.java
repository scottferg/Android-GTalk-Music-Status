package com.android.gtalkstatus;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.XMPPException; 

public class XMPPTransfer {

    private String mUsername;
    private String mPassword;
    private XMPPConnection mConnection;

    public XMPPTransfer(String aUsername, String aPassword) {

        mUsername = aUsername;
        mPassword = aPassword;

        ConnectionConfiguration connectionConfig = 
            new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");

        mConnection = new XMPPConnection(connectionConfig);

        try {
            mConnection.connect();
            mConnection.login(mUsername, mPassword);

        } catch (XMPPException e) {
            // Probably should handle this better
            e.printStackTrace();
        }
    }

    public void setStatus(String aStatusMessage) {

        Presence presence = new Presence(Presence.Type.available);

        presence.setStatus(aStatusMessage);
        presence.setPriority(24);
        presence.setMode(Presence.Mode.available);

        mConnection.sendPacket(presence);
    }
}
