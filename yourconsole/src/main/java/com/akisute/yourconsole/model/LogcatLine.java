package com.akisute.yourconsole.model;

import android.text.TextUtils;
import android.util.Log;

import com.akisute.yourconsole.helper.LogcatHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogcatLine {

    public static final String LOGCAT_DATE_FORMAT = "MM-dd HH:mm:ss.SSS";

    private static final int TIMESTAMP_LENGTH = 19;

    private static final Pattern LOG_PATTERN = Pattern.compile(
            // log level
            "(\\w)" +
                    "/" +
                    // tag
                    "([^(]+)" +
                    "\\(\\s*" +
                    // pid
                    "(\\d+)" +
                    // optional weird number that only occurs on ZTE blade
                    "(?:\\*\\s*\\d+)?" +
                    "\\): "
    );

    private int mLogLevel;
    private String mTag;
    private String mLogOutput;
    private int mProcessId = -1;
    private String mTimestamp;
    private boolean mExpanded = false;
    private boolean mHighlighted = false;

    public int getLogLevel() {
        return mLogLevel;
    }

    public void setLogLevel(int mLogLevel) {
        this.mLogLevel = mLogLevel;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public String getLogOutput() {
        return mLogOutput;
    }

    public void setLogOutput(String mLogOutput) {
        this.mLogOutput = mLogOutput;
    }

    public int getProcessId() {
        return mProcessId;
    }

    public void setProcessId(int mProcessId) {
        this.mProcessId = mProcessId;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public boolean ismExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean mExpanded) {
        this.mExpanded = mExpanded;
    }

    public boolean ismHighlighted() {
        return mHighlighted;
    }

    public void setHighlighted(boolean mHighlighted) {
        this.mHighlighted = mHighlighted;
    }

    public static LogcatLine newLogLine(String originalLine, boolean expanded) {

        LogcatLine logcatLine = new LogcatLine();
        logcatLine.setExpanded(expanded);

        int startIdx = 0;

        // if the first char is a digit, then this starts out with a timestamp
        // otherwise, it's a legacy log or the beginning of the log output or something
        if (!TextUtils.isEmpty(originalLine)
                && Character.isDigit(originalLine.charAt(0))
                && originalLine.length() >= TIMESTAMP_LENGTH) {
            String timestamp = originalLine.substring(0, TIMESTAMP_LENGTH - 1);
            logcatLine.setTimestamp(timestamp);
            startIdx = TIMESTAMP_LENGTH; // cut off timestamp
        }

        Matcher matcher = LOG_PATTERN.matcher(originalLine);

        if (matcher.find(startIdx)) {
            char logLevelChar = matcher.group(1).charAt(0);

            logcatLine.setLogLevel(convertCharToLogLevel(logLevelChar));
            logcatLine.setTag(matcher.group(2));
            logcatLine.setProcessId(Integer.parseInt(matcher.group(3)));

            logcatLine.setLogOutput(originalLine.substring(matcher.end()));

        } else {
            logcatLine.setLogOutput(originalLine);
            logcatLine.setLogLevel(-1);
        }

        return logcatLine;
    }

    public static int convertCharToLogLevel(char logLevelChar) {

        switch (logLevelChar) {
            case 'D':
                return Log.DEBUG;
            case 'E':
                return Log.ERROR;
            case 'I':
                return Log.INFO;
            case 'V':
                return Log.VERBOSE;
            case 'W':
                return Log.WARN;
            case 'F':
                return LogcatHelper.LOG_WTF; // 'F' actually stands for 'WTF', which is a real Android log level in 2.2
        }
        return -1;
    }

    public static char convertLogLevelToChar(int logLevel) {

        switch (logLevel) {
            case Log.DEBUG:
                return 'D';
            case Log.ERROR:
                return 'E';
            case Log.INFO:
                return 'I';
            case Log.VERBOSE:
                return 'V';
            case Log.WARN:
                return 'W';
            case LogcatHelper.LOG_WTF:
                return 'F';
        }
        return ' ';
    }

    public CharSequence getOriginalLine() {

        if (mLogLevel == -1) { // starter line like "begin of log etc. etc."
            return mLogOutput;
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (mTimestamp != null) {
            stringBuilder.append(mTimestamp).append(' ');
        }

        stringBuilder.append(convertLogLevelToChar(mLogLevel))
                .append('/')
                .append(mTag)
                .append('(')
                .append(mProcessId)
                .append("): ")
                .append(mLogOutput);

        return stringBuilder;
    }
}
