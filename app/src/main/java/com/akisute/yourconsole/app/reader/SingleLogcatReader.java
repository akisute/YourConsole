package com.akisute.yourconsole.app.reader;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import com.akisute.yourconsole.app.helper.LogcatHelper;
import com.akisute.yourconsole.app.helper.RuntimeHelper;

public class SingleLogcatReader extends LogcatReader {

    private final Process mLogcatProcess;
    private final BufferedReader mBufferedReader;
    private final String mLogBufferName;
    private String mLastLine;

    public SingleLogcatReader(String logBufferName, String lastLine) throws IOException {
        super();
        mLogBufferName = logBufferName;
        mLastLine = lastLine;
        mLogcatProcess = LogcatHelper.getLogcatProcess(mLogBufferName);
        mBufferedReader = new BufferedReader(new InputStreamReader(mLogcatProcess.getInputStream()), 8192);
    }

    @Override
    public int read(char[] chars, int i, int i2) throws IOException {
        throw new UnsupportedOperationException("Must use readLine() instead");
    }

    @Override
    public void reset() throws IOException {
        // Do nothing, unsupported
    }

    @Override
    public boolean ready() throws IOException {
        return mBufferedReader.ready();
    }

    @Override
    public long skip(long charCount) throws IOException {
        throw new UnsupportedOperationException("Must use skipLine() instead");
    }

    @Override
    public void mark(int readLimit) throws IOException {
        // Do nothing, unsupported
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Must use readLine() instead");
    }

    @Override
    public String readLine() throws IOException {
        String line = mBufferedReader.readLine();

        if (mLastLine != null) { // still skipping past the 'last line'
            if (mLastLine.equals(line) || isAfterLastTime(line)) {
                mLastLine = null; // indicates we've passed the last line
            }
        }

        return line;
    }

    @Override
    public void skipLine() throws IOException {
        // Just a sugar syntax of readLine()
        readLine();
    }

    @Override
    public boolean isReadyToReadNewLines() {
        return (mLastLine == null);
    }

    @Override
    public void close() throws IOException {
        if (mLogcatProcess != null) {
            RuntimeHelper.destroy(mLogcatProcess);
        }
        if (mBufferedReader != null) {
            mBufferedReader.close();
        }
    }

    private boolean isAfterLastTime(String line) {
        // doing a string comparison is sufficient to determine whether this line is chronologically
        // after the last line, because the format they use is exactly the same and
        // lists larger time period before smaller ones
        return isDatedLogLine(mLastLine) && isDatedLogLine(line) && line.compareTo(mLastLine) > 0;

    }

    private boolean isDatedLogLine(String line) {
        // 18 is the size of the logcat timestamp
        return (!TextUtils.isEmpty(line) && line.length() >= 18 && Character.isDigit(line.charAt(0)));
    }
}
