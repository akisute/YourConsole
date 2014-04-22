package com.akisute.yourconsole.app.model;

import android.util.Log;

import com.akisute.yourconsole.app.helper.LogcatHelper;
import com.akisute.yourconsole.app.reader.LogcatReader;
import com.akisute.yourconsole.app.reader.SingleLogcatReader;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.akisute.yourconsole.app.util.GlobalPreference;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class LogcatRecordingManager {

    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final GlobalEventBus mGlobalEventBus;
    private final GlobalPreference mGlobalPreference;

    private String mLastReadLine;

    @Inject
    public LogcatRecordingManager(GlobalEventBus globalEventBus, GlobalPreference globalPreference) {
        mGlobalEventBus = globalEventBus;
        mGlobalPreference = globalPreference;
    }

    public void start() {
        mLastReadLine = mGlobalPreference.getLastReadLine();
        mExecutorService.scheduleWithFixedDelay(new WorkerThread(), 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        mExecutorService.shutdownNow();
        mGlobalPreference.setLastReadLine(mLastReadLine);
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            LogcatReader logcatReader = null;
            try {
                logcatReader = new SingleLogcatReader(LogcatHelper.BUFFER_MAIN, mLastReadLine);
                while (!isInterrupted() && !logcatReader.isReadyToReadNewLines()) {
                    // Keep skipping lines until ready to read new lines of logcat
                    logcatReader.skipLine();
                }

                String line;
                while (!isInterrupted() && (line = logcatReader.readLine()) != null) {
                    LogcatLine logcatLine = LogcatLine.newLogLine(line, true);
                    mGlobalEventBus.postInMainThread(new OnNewLogcatLineEvent(logcatLine));
                    mLastReadLine = line;
                }
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "unexpected exception", e);
            } finally {
                try {
                    if (logcatReader != null) {
                        logcatReader.close();
                    }
                } catch (IOException e) {
                    // Do nothing
                }
            }
        }
    }

    public static class OnNewLogcatLineEvent {

        private final LogcatLine mLogcatLine;

        public OnNewLogcatLineEvent(LogcatLine logcatLine) {
            mLogcatLine = logcatLine;
        }

        public LogcatLine getLogcatLine() {
            return mLogcatLine;
        }
    }
}
