package com.akisute.yourconsole.app.reader;

import java.io.Closeable;
import java.io.IOException;

public interface LogcatReader extends Closeable {
    public boolean isReadyToRead();
    public String readLine() throws IOException;
    public void skipLine() throws IOException;
}
