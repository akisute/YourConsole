package com.akisute.yourconsole.model;

import com.akisute.yourconsole.intent.Intents;

import java.util.List;

public class ConsoleBufferLoader {

    private ConsoleBuffer mConsoleBuffer;

    public ConsoleBufferLoader(ConsoleBuffer consoleBuffer) {
        if (consoleBuffer == null) {
            throw new IllegalArgumentException();
        }
        mConsoleBuffer = consoleBuffer;
    }

    public void load() {
        mConsoleBuffer.clear();
        List<MText> models = MText.getAll();
        for (MText model : models) {
            if (Intents.MIME_TYPE_PLAINTEXT.equals(model.getMimeType())) {
                String text = model.getText();
                if (text != null) {
                    String[] lines = text.split("\n");
                    for (String line : lines) {
                        LogcatLine l = LogcatLine.newLogLine(line, true);
                        mConsoleBuffer.addLogcatLine(l);
                    }
                }
            } else if (Intents.MIME_TYPE_LOGCAT.equals(model.getMimeType())) {
                String s = model.getText();
                mConsoleBuffer.addString(s);
            }
        }
    }

}
