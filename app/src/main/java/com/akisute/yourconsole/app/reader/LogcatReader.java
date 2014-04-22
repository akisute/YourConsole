package com.akisute.yourconsole.app.reader;

import java.io.IOException;
import java.io.Reader;

public abstract class LogcatReader extends Reader {

    public abstract String readLine() throws IOException;

    public abstract void skipLine() throws IOException;

    public abstract boolean isReadyToReadNewLines();

}
