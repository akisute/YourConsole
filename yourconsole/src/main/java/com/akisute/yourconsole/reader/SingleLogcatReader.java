package com.akisute.yourconsole.reader;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.akisute.yourconsole.helper.LogcatHelper;
import com.akisute.yourconsole.helper.RuntimeHelper;

public class SingleLogcatReader extends AbsLogcatReader {

    private Process mLogcatProcess;
    private BufferedReader mBufferedReader;
    private String mLogBufferName;
    private String mLastLine;

    public SingleLogcatReader(String logBufferName, String lastLine) throws IOException {
        super();
        mLogBufferName = logBufferName;
        mLastLine = lastLine;
        mLogcatProcess = LogcatHelper.getLogcatProcess(mLogBufferName);
        mBufferedReader = new BufferedReader(new InputStreamReader(mLogcatProcess.getInputStream()), 8192);
    }

    @Override
    public boolean isReadyToRead() {
        return (mLastLine == null);
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
    public void close() throws IOException {
        if (mLogcatProcess != null) {
            RuntimeHelper.destroy(mLogcatProcess);
        }
        if (mBufferedReader != null) {
            mBufferedReader.close();
        }
    }

    public void setLastLine(String lastLine) {
        mLastLine = lastLine;
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
