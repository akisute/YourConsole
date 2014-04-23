package com.akisute.yourconsole.app.dagger;

import android.app.Service;

public abstract class DaggeredService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        DaggeredApplication application = (DaggeredApplication) getApplication();
        application.inject(this);
    }
}
