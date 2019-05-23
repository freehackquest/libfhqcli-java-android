package com.freehackquest.sample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.freehackquest.libfhqcli.FHQClient;


public class SampleApp extends Application {
    private static final String TAG = SampleApp.class.getSimpleName();

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "onCreate");
        FHQClient.startService(getApplicationContext()); // init instance
    }
}
