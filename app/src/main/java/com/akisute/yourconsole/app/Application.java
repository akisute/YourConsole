package com.akisute.yourconsole.app;

import com.activeandroid.ActiveAndroid;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
