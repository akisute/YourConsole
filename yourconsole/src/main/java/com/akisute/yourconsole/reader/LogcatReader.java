package com.akisute.yourconsole.reader;

import java.io.Closeable;
import java.io.IOException;

public interface LogcatReader extends Closeable {
    public String readLine() throws IOException;
}
