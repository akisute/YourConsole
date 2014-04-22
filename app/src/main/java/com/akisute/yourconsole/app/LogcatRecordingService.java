package com.akisute.yourconsole.app;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.akisute.yourconsole.app.dagger.DaggeredService;
import com.akisute.yourconsole.app.intent.Intents;
import com.akisute.yourconsole.app.model.LogcatRecordingManager;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class LogcatRecordingService extends DaggeredService {

    @Inject
    GlobalEventBus mGlobalEventBus;
    @Inject
    LogcatRecordingManager mLogcatRecordingManager;

    public static void startLogcatRecording(Context context) {
        Intent intent = new Intent(context, LogcatRecordingService.class);
        intent.setAction(Intents.ACTION_START_LOGCAT_RECORDING);
        context.startService(intent);
    }

    public static void stopLogcatRecording(Context context) {
        Intent intent = new Intent(context, LogcatRecordingService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleStopLogcatRecording();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Binding to this service is not yet supported.");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        throw new UnsupportedOperationException("Binding to this service is not yet supported.");
    }

    @Override
    public void onRebind(Intent intent) {
        throw new UnsupportedOperationException("Binding to this service is not yet supported.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Intents.ACTION_START_LOGCAT_RECORDING.equals(action)) {
                handleStartLogcatRecording();
                return START_STICKY;
            }
        }

        stopSelfResult(startId);
        return START_NOT_STICKY;
    }

    private void handleStartLogcatRecording() {
        com.akisute.yourconsole.app.Application application = (Application) getApplication();
        mLogcatRecordingManager.start();
        mGlobalEventBus.register(this);
    }

    private void handleStopLogcatRecording() {
        mGlobalEventBus.unregister(this);
        mLogcatRecordingManager.stop();
    }

    @Subscribe
    public void onNewLogcatLineEvent(LogcatRecordingManager.OnNewLogcatLineEvent event) {
        SaveIntentService.startActionSave(this, event.getLogcatLine());
    }
}
