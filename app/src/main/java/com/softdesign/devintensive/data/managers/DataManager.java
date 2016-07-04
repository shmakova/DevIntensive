package com.softdesign.devintensive.data.managers;

/**
 * Created by shmakova on 28.06.16.
 */
public class DataManager {
    private static DataManager INSTANCE = null;

    private PreferenceManager mPreferenceManager;

    public DataManager() {
        this.mPreferenceManager = new PreferenceManager();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }

        return INSTANCE;
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }
}
