package com.akisute.yourconsole.app.dagger;

import android.app.Application;
import android.content.Context;

import com.akisute.yourconsole.app.util.GlobalPreference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Provides the application context and classes that depends on application contexts.
 */
@Module (
        injects = {
                GlobalPreference.class,
        }
)
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return mApplication;
    }
}
