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

        // TODO: THIS DOES NOT WORK!!
        // Here's a reason: https://github.com/square/dagger/issues/367
        // So problem is LogcatRecordingManager is NEWed by Module, which means the instance is not created by ObjectGraph nor injected dynamically by using inject().
        // Thus I have to inject() manually or let constructor pass dependent instances. Kinda fuck.
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
