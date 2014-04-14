package com.akisute.yourconsole.com.akisute.yourconsole.model;

public class ConsoleBuffer {

    private StringBuilder mBuffer;
    private int mNumberOfLines;

    public ConsoleBuffer() {
        mBuffer = new StringBuilder();
    }

    public void addString(String s) {
        String[] lines = s.split("\n");
        for (String line : lines) {
            mBuffer.append(line);
            mBuffer.append("\n");
            mNumberOfLines++;
        }
    }

    public void addStringLine(String s) {
        String[] lines = s.split("\n");
        for (String line : lines) {
            mBuffer.append(line);
            mBuffer.append(" ");
        }
        mBuffer.append("\n");
        mNumberOfLines++;
    }

    public String getText() {
        return mBuffer.toString();
    }

    public int getNumberOfLines() {
        return mNumberOfLines;
    }
}
