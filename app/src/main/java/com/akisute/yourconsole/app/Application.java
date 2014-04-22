package com.akisute.yourconsole.app;

import com.activeandroid.ActiveAndroid;
import com.akisute.yourconsole.app.dagger.ApplicationModule;

import dagger.ObjectGraph;

public class Application extends android.app.Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(getModules());
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
        mObjectGraph = null;
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    private Object[] getModules() {
        return new Object[]{
                new ApplicationModule(this)
        };
    }
}
