package com.akisute.yourconsole.app.dagger;

import android.content.Context;

import com.akisute.yourconsole.app.ConsoleViewerFragment;
import com.akisute.yourconsole.app.LogcatRecordingService;
import com.akisute.yourconsole.app.SaveIntentService;
import com.akisute.yourconsole.app.model.LogcatRecordingManager;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.akisute.yourconsole.app.util.GlobalPreference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                ApplicationModule.class
        },
        injects = {
                // Activity, Fragment, Service (dynamically injected on create by Daggered classes using ObjectGraph.inject(), uses member injections)
                ConsoleViewerFragment.class,
                LogcatRecordingService.class,
                SaveIntentService.class,
                // Model (statically injected recursively when ObjectGraph.inject() is called, uses constructor injections and providers)
                LogcatRecordingManager.class
        }
)
public class ModelModule {

    @Provides
    @Singleton
    GlobalEventBus provideGlobalEventBus() {
        return new GlobalEventBus();
    }

    @Provides
    @Singleton
    GlobalPreference provideGlobalPreference(@ForApplication Context context) {
        return new GlobalPreference(context);
    }

    @Provides
    LogcatRecordingManager provideLogcatRecordingManager(GlobalEventBus globalEventBus, GlobalPreference globalPreference) {
        return new LogcatRecordingManager(globalEventBus, globalPreference);
    }
}
