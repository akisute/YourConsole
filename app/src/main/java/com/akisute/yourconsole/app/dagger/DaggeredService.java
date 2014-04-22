package com.akisute.yourconsole.app.dagger;

import android.app.Service;

public abstract class DaggeredService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        com.akisute.yourconsole.app.Application application = (com.akisute.yourconsole.app.Application) getApplication();
        application.inject(this);
    }
}
