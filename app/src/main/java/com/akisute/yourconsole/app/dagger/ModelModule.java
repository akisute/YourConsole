package com.akisute.yourconsole.app.dagger;

import dagger.Module;

/**
 * Provides instances that are completely independent to android contexts.
 * These instances should be inject statically on compile time rather than using runtime injection.
 */
@Module
public class ModelModule {
}
