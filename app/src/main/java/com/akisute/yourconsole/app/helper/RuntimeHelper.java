package com.akisute.yourconsole.app.helper;

import java.io.IOException;
import java.util.List;

public class RuntimeHelper {

    public static Process exec(List<String> args) throws IOException {
        return Runtime.getRuntime().exec(args.toArray(new String[args.size()]));
    }

    public static void destroy(Process process) {
        process.destroy();
    }
}
