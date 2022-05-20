package com.lyentech.npdemo;

import android.app.Application;
import android.provider.Settings;

import com.lyentech.np.NpServer;

public class NpApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String uniqueId = Settings.Secure.getString(getContentResolver(), "android_id");
        NpServer.initSdk(getApplicationContext(), uniqueId, MainActivity.class);
    }
}
