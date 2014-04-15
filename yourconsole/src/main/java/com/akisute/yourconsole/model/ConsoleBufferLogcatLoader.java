package com.akisute.yourconsole.model;

import android.util.Log;

import com.akisute.yourconsole.helper.LogcatHelper;
import com.akisute.yourconsole.reader.LogcatReader;
import com.akisute.yourconsole.reader.SingleLogcatReader;

import java.io.Closeable;
import java.io.IOException;

public class ConsoleBufferLogcatLoader implements Closeable {

    private ConsoleBuffer mConsoleBuffer;
    private LogcatReader mLogcatReader;
    private boolean mClosed;
    private final Object mLock = new Object();

    public ConsoleBufferLogcatLoader(ConsoleBuffer consoleBuffer) throws IOException {
        if (consoleBuffer == null) {
            throw new IllegalArgumentException();
        }
        mConsoleBuffer = consoleBuffer;
        mLogcatReader = new SingleLogcatReader(LogcatHelper.BUFFER_MAIN, null);
    }

    public void load() throws IOException {
        if (mClosed) {
            throw new IllegalStateException();
        }
        String line;
        try {
            while ((line = mLogcatReader.readLine()) != null && !mClosed) {
                mConsoleBuffer.addStringLine(line);
            }
        } catch (IOException e) {
            Log.e(this.getClass().toString(), "unexpected exception", e);
            throw e;
        } finally {
            close();
        }
    }

    @Override
    public void close() throws IOException {
        if (!mClosed) {
            synchronized (mLock) {
                if (!mClosed && mLogcatReader != null) {
                    mLogcatReader.close();
                    mClosed = true;
                }
            }
        }
    }
}
