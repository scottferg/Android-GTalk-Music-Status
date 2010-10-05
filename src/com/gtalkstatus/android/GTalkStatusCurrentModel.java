package com.gtalkstatus.android;

import android.database.Observable;

public class GTalkStatusCurrentModel extends Observable {
    private static GTalkStatusCurrentModel sInstance = null;
    private static String mArtist;
    private static String mTrack;

    private GTalkStatusCurrentModel() {}

    public static GTalkStatusCurrentModel getInstance() {
        if (sInstance == null) {
            sInstance = new GTalkStatusCurrentModel();
        }

        return sInstance;
    }

    public void setArtist(String aArtist) {
        mArtist = aArtist;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setTrack(String aTrack) {
        mTrack = aTrack;
    }

    public String getTrack() {
        return mTrack;
    }
}
