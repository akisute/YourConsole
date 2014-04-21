package com.akisute.yourconsole.app.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogcatHelper {
    public static final String BUFFER_MAIN = "main";
    public static final String BUFFER_EVENTS = "events";
    public static final String BUFFER_RADIO = "radio";
    public static final int LOG_WTF = 100; // arbitrary int to signify 'wtf' log level

    public static Process getLogcatProcess(String logBufferName) throws IOException {
        List<String> args = getLogcatArgs(logBufferName);
        return RuntimeHelper.exec(args);
    }

    private static List<String> getLogcatArgs(String buffer) {
        List<String> args = new ArrayList<String>(Arrays.asList("logcat", "-v", "time"));

        // for some reason, adding -b main excludes log output from AndroidRuntime runtime exceptions,
        // whereas just leaving it blank keeps them in.  So do not specify the buffer if it is "main"
        if (!buffer.equals(BUFFER_MAIN)) {
            args.add("-b");
            args.add(buffer);
        }

        return args;
    }
}
