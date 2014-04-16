package com.akisute.yourconsole.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by JP11688 on 2014/04/16.
 */
public class PreferenceHelper {

    public static String getLastReadLine(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString("LastReadLine", null);
    }

    public static void setLastReadLine(Context context, String line) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("LastReadLine", line);
        editor.commit();
    }
}
