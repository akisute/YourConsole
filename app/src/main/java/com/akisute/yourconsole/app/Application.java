package com.akisute.yourconsole.app;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.akisute.yourconsole.app.dagger.ApplicationModule;
import com.akisute.yourconsole.app.dagger.ModelModule;
import com.akisute.yourconsole.app.model.LogcatRecordingManager;

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

    public void validateObjectGraph() {
        LogcatRecordingManager abesi = mObjectGraph.get(LogcatRecordingManager.class);
        Log.d("abesi", abesi.toString());
    }

    private Object[] getModules() {
        return new Object[]{
                new ApplicationModule(this),
                new ModelModule()
        };
    }
}
