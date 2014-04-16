package com.akisute.yourconsole;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.activeandroid.ActiveAndroid;
import com.akisute.yourconsole.model.LogcatLine;
import com.akisute.yourconsole.model.StringLine;


public class SaveIntentService extends IntentService {
    private static final String ACTION_SAVE = "com.akisute.yourconsole.action.SAVE";
    private static final String MIME_TYPE_PLAINTEXT = "text/plain";
    private static final String MIME_TYPE_LOGCAT = "application/vnd.com.akisute.yourconsole.logcat";

    public static void startActionSave(Context context, String text) {
        Intent intent = new Intent(context, SaveIntentService.class);
        intent.setAction(ACTION_SAVE);
        intent.setType(MIME_TYPE_PLAINTEXT);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startService(intent);
    }

    public static void startActionSave(Context context, StringLine stringLine) {
        startActionSave(context, stringLine.getText());
    }

    public static void startActionSave(Context context, LogcatLine logcatLine) {
        Intent intent = new Intent(context, SaveIntentService.class);
        intent.setAction(ACTION_SAVE);
        intent.setType(MIME_TYPE_LOGCAT);
        intent.putExtra(Intent.EXTRA_TEXT, logcatLine.getOriginalLine());
        context.startService(intent);
    }

    public SaveIntentService() {
        super("SaveIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE.equals(action)) {
                final String mimeType = intent.getType();
                final String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                handleSave(mimeType, text);
            }
        }
    }

    private void handleSave(String mimeType, String text) {
        if (text == null) {
            return;
        }

        try {
            ActiveAndroid.beginTransaction();
            String[] lines = text.split("\n");
            if (mimeType == null || MIME_TYPE_PLAINTEXT.equals(mimeType)) {
                // plain text lines
                for (String line : lines) {
                    StringLine stringLine = StringLine.newStringLine(line);
                    stringLine.save();
                }
            } else {
                // logcat lines
                for (String line : lines) {
                    LogcatLine logcatLine = LogcatLine.newLogLine(line, true);
                    logcatLine.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
