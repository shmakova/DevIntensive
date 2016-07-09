package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shmakova on 28.06.16.
 */
public class DevintensiveApplication extends Application {


    public static SharedPreferences sSharedPreferences;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }
}
