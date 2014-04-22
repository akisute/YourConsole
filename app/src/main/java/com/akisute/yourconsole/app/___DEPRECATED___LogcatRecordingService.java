package com.akisute.yourconsole.app;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.akisute.yourconsole.app.helper.LogcatHelper;
import com.akisute.yourconsole.app.helper.PreferenceHelper;
import com.akisute.yourconsole.app.intent.Intents;
import com.akisute.yourconsole.app.model.LogcatLine;
import com.akisute.yourconsole.app.reader.LogcatReader;
import com.akisute.yourconsole.app.reader.SingleLogcatReader;

import java.io.IOException;


public class ___DEPRECATED___LogcatRecordingService extends IntentService {

    private LogcatReader mLogcatReader;
    private boolean mKilled;
    private final Object mKillLock = new Object();
    private final BroadcastReceiver mStopRecordingIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            killProcess();
            context.stopService(new Intent(context, ___DEPRECATED___LogcatRecordingService.class));
        }
    };

    public static void startLogcatRecording(Context context) {
        Intent intent = new Intent(context, ___DEPRECATED___LogcatRecordingService.class);
        intent.setAction(Intents.ACTION_START_LOGCAT_RECORDING);
        context.startService(intent);
    }

    public static void stopLogcatRecording(Context context) {
        Intent intent = new Intent(context, ___DEPRECATED___LogcatRecordingService.class);
        intent.setAction(Intents.ACTION_STOP_LOGCAT_RECORDING);
        context.startService(intent);
    }

    public ___DEPRECATED___LogcatRecordingService() {
        super("___DEPRECATED___LogcatRecordingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter(Intents.ACTION_STOP_LOGCAT_RECORDING);
        registerReceiver(mStopRecordingIntentReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killProcess();
        unregisterReceiver(mStopRecordingIntentReceiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Intents.ACTION_START_LOGCAT_RECORDING.equals(action)) {
                handleStartLogcatRecording();
            }
        }
    }

    private void handleStartLogcatRecording() {
        try {
            initializeLogcatReader();
            String line;
            while (!mKilled && (line = mLogcatReader.readLine()) != null) {
                LogcatLine logcatLine = LogcatLine.newLogLine(line, true);
                SaveIntentService.startActionSave(this, logcatLine);
                PreferenceHelper.setLastReadLine(this, line);
            }
        } catch (IOException e) {
            Log.e(this.getClass().toString(), "unexpected exception", e);
        } finally {
            killProcess();
        }
    }

    private void initializeLogcatReader() throws IOException {
        String lastReadLine = PreferenceHelper.getLastReadLine(this);
        mLogcatReader = new SingleLogcatReader(LogcatHelper.BUFFER_MAIN, lastReadLine);
        while (!mKilled && !mLogcatReader.isReadyToRead()) {
            mLogcatReader.skipLine();
            // keep skipping lines until we find one that is past the last log line, i.e.
            // it's ready to record
        }
    }

    private void killProcess() {
        if (!mKilled) {
            synchronized (mKillLock) {
                if (!mKilled && mLogcatReader != null) {
                    try {
                        mLogcatReader.close();
                    } catch (IOException e) {
                        // Do nothing
                    }
                    mKilled = true;
                }
            }
        }
    }
}
