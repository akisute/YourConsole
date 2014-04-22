package com.akisute.yourconsole.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.akisute.yourconsole.app.dagger.ForApplication;

import javax.inject.Inject;

public class GlobalPreference {
    @Inject
    @ForApplication
    Context mContext;

    public String getLastReadLine() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPrefs.getString("LastReadLine", null);
    }

    public void setLastReadLine(String line) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("LastReadLine", line);
        editor.commit();
    }
}
