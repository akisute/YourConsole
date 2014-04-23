package com.akisute.yourconsole.app;

import com.activeandroid.ActiveAndroid;
import com.akisute.yourconsole.app.dagger.DaggeredApplication;

public class Application extends DaggeredApplication {

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

    @Override
    protected Object[] getModules() {
        return new Object[]{
                new AppModule()
        };
    }
}
