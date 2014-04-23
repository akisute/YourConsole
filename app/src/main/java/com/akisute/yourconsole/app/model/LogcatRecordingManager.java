package com.akisute.yourconsole.app.model;

import android.util.Log;

import com.akisute.yourconsole.app.helper.LogcatHelper;
import com.akisute.yourconsole.app.reader.LogcatReader;
import com.akisute.yourconsole.app.reader.SingleLogcatReader;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.akisute.yourconsole.app.util.GlobalPreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

public class LogcatRecordingManager {

    private static final int BUFFER_INITIAL_CAPACITY = 128;

    private final ScheduledExecutorService mWorkerExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService mFlusherExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ReentrantLock mBufferLock = new ReentrantLock();
    private final GlobalEventBus mGlobalEventBus;
    private final GlobalPreference mGlobalPreference;

    private String mLastReadLine;       // R/W from Worker thread only, No lock required
    private List<LogcatLine> mBuffer;   // R/W from both Worker and Flusher, must be locked

    @Inject
    public LogcatRecordingManager(GlobalEventBus globalEventBus, GlobalPreference globalPreference) {
        mGlobalEventBus = globalEventBus;
        mGlobalPreference = globalPreference;
    }

    public void start() {
        mLastReadLine = mGlobalPreference.getLastReadLine();
        mBuffer = new ArrayList<LogcatLine>(BUFFER_INITIAL_CAPACITY);
        mWorkerExecutorService.scheduleWithFixedDelay(new WorkerThread(), 0, 500, TimeUnit.MILLISECONDS);
        mFlusherExecutorService.scheduleWithFixedDelay(new FlusherThread(), 501, 501, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        mFlusherExecutorService.shutdown();     // Runs until last scheduled delivery
        mWorkerExecutorService.shutdownNow();   // Immediately shuts down
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
                    mLastReadLine = line;
                    LogcatLine logcatLine = LogcatLine.newLogLine(line, true);
                    mBufferLock.lock();
                    mBuffer.add(logcatLine);
                    mBufferLock.unlock();
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

    private class FlusherThread extends Thread {
        @Override
        public void run() {
            try {
                if (mBufferLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                    if (mBuffer.size() > 0) {
                        List<LogcatLine> logcatLineList = mBuffer;
                        mGlobalEventBus.postInMainThread(new OnNewLogcatLineEvent(logcatLineList));
                        mBuffer = new ArrayList<LogcatLine>(BUFFER_INITIAL_CAPACITY);
                    }
                    mBufferLock.unlock();
                }
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
    }

    public static class OnNewLogcatLineEvent {

        private final List<LogcatLine> mLogcatLineList;

        public OnNewLogcatLineEvent(List<LogcatLine> logcatLineList) {
            mLogcatLineList = logcatLineList;
        }

        public List<LogcatLine> getLogcatLineList() {
            return mLogcatLineList;
        }
    }
}
