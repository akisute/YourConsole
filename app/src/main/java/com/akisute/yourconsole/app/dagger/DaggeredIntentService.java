package com.akisute.yourconsole.app.dagger;


import android.app.IntentService;

public abstract class DaggeredIntentService extends IntentService {

    public DaggeredIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        com.akisute.yourconsole.app.Application application = (com.akisute.yourconsole.app.Application) getApplication();
        application.inject(this);
    }
}
