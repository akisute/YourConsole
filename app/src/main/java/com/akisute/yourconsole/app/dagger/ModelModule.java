package com.akisute.yourconsole.app.dagger;

import com.akisute.yourconsole.app.ConsoleViewerFragment;
import com.akisute.yourconsole.app.LogcatRecordingService;
import com.akisute.yourconsole.app.SaveIntentService;
import com.akisute.yourconsole.app.model.ConsoleListAdapter;
import com.akisute.yourconsole.app.model.LogcatRecordingManager;
import com.akisute.yourconsole.app.reader.LogcatReader;
import com.akisute.yourconsole.app.reader.SingleLogcatReader;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.akisute.yourconsole.app.util.GlobalPreference;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances that are completely independent to android contexts.
 * These instances should be inject statically on compile time rather than using runtime injection.
 */
@Module (
        injects = {
                // Activity and Fragment
                ConsoleViewerFragment.class,
                // Service
                LogcatRecordingService.class,
                SaveIntentService.class,
                // Model
                ConsoleListAdapter.class,
                LogcatRecordingManager.class,
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
    GlobalPreference provideGlobalPreference() {
        return new GlobalPreference();
    }

}
