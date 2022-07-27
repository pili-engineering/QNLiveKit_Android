package com.qlive.uiwidghtbeauty.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

public class SharedPreferencesUtils {

    public static int resVersion = 0;

    public static boolean resourceReady(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean resourceReady = preferences.getBoolean("resource", false);
        int preVersioncode = preferences.getInt("versionCode", 0);
        return resourceReady && getVersionCode(context) == preVersioncode;
    }

    public static void setResourceReady(Context context, boolean isReady) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("resource", isReady);
        editor.putInt("versionCode", getVersionCode(context));
        editor.commit();
    }

    private static int getVersionCode(Context context) {
        return resVersion;
    }
}
