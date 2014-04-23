package com.akisute.yourconsole.app;

import android.content.Context;

import com.akisute.yourconsole.app.dagger.DaggeredApplicationModule;
import com.akisute.yourconsole.app.dagger.ForApplication;
import com.akisute.yourconsole.app.dagger.ForInjecting;
import com.akisute.yourconsole.app.model.ConsoleListAdapter;
import com.akisute.yourconsole.app.model.LogcatRecordingManager;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.akisute.yourconsole.app.util.GlobalPreference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                DaggeredApplicationModule.class
        },
        injects = {
                // Activity, Fragment, Service (dynamically injected on create by Daggered classes using ObjectGraph.inject(), uses member injections)
                ConsoleViewerFragment.class,
                LogcatRecordingService.class,
                SaveIntentService.class

                // Model (statically injected recursively when ObjectGraph.inject() is called, uses constructor injections)
                // Usually models are not listed here
        }
)
public class AppModule {

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

    @Provides
    ConsoleListAdapter provideConsoleListAdapter(@ForInjecting Context context) {
        return new ConsoleListAdapter(context);
    }
}
