package com.akisute.yourconsole.app.dagger;

import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.akisute.yourconsole.app.util.GlobalPreference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances that are completely independent to android contexts.
 * These instances should be inject statically on compile time rather than using runtime injection.
 */
@Module
public class ModelModule {

    @Provides
    @Singleton
    GlobalEventBus provideGlobalEventBus() {
        return new GlobalEventBus();
    }

    @Provides
    @Singleton
    GlobalPreference provideGlobalPreference() {
        return new GlobalPreference();
    }
}
