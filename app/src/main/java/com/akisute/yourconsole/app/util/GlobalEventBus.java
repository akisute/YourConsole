package com.akisute.yourconsole.app.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class GlobalEventBus extends Bus {

    private static final GlobalEventBus INSTANCE = new GlobalEventBus();

    private Handler mMainThreadHandler;

    public GlobalEventBus() {
        super();
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static GlobalEventBus getInstance() {
        return INSTANCE;
    }

    public void postInMainThread(final Object event) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                post(event);
            }
        });
    }
}
