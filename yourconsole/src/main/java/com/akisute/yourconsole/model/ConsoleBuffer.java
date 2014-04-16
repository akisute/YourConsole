package com.akisute.yourconsole.model;

import com.akisute.yourconsole.intent.Intents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsoleBuffer {

    private List<String> mBuffer;
    private int mLastReadLineNumber;
    private final Object mLock = new Object();

    public ConsoleBuffer() {
        clear();
    }

    public void addString(String s) {
        String[] lines = s.split("\n");
        synchronized (mLock) {
            Collections.addAll(mBuffer, lines);
        }
    }

    public void addStringLine(String s) {
        String line = s.replace("\n", " ");
        synchronized (mLock) {
            mBuffer.add(line);
        }
    }

    public void addLogcatLine(LogcatLine l) {
        synchronized (mLock) {
            mBuffer.add(l.getOriginalLine().toString());
        }
    }

    public void clear() {
        synchronized (mLock) {
            mBuffer = new ArrayList<String>();
            mLastReadLineNumber = 0;
        }
    }

    public String getText() {
        StringBuilder stringBuilder;
        synchronized (mLock) {
            stringBuilder = new StringBuilder(mBuffer.size());
            for (String line : mBuffer) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public String getTextFrom(int from) {
        StringBuilder stringBuilder;
        List<String> subList;
        synchronized (mLock) {
            if (from >= mBuffer.size()) {
                return null;
            }
            subList = mBuffer.subList(from, mBuffer.size());
            stringBuilder = new StringBuilder(subList.size());
            for (String line : subList) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public String getTextFromLastReadLine() {
        StringBuilder stringBuilder;
        List<String> subList;
        synchronized (mLock) {
            if (mLastReadLineNumber >= mBuffer.size()) {
                return null;
            }
            subList = mBuffer.subList(mLastReadLineNumber, mBuffer.size());
            stringBuilder = new StringBuilder(subList.size());
            for (String line : subList) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            mLastReadLineNumber = mBuffer.size();
        }
        return stringBuilder.toString();
    }

    public int getNumberOfLines() {
        synchronized (mLock) {
            return mBuffer.size();
        }
    }
}
