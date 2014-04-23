package com.akisute.yourconsole.app;

import android.content.Context;
import android.content.Intent;

import com.activeandroid.ActiveAndroid;
import com.akisute.yourconsole.app.dagger.DaggeredIntentService;
import com.akisute.yourconsole.app.intent.Intents;
import com.akisute.yourconsole.app.model.LogcatLine;
import com.akisute.yourconsole.app.model.MText;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class SaveIntentService extends DaggeredIntentService {

    @Inject
    GlobalEventBus mGlobalEventBus;

    public static void startActionSave(Context context, String text) {
        Intent intent = new Intent(context, SaveIntentService.class);
        intent.setAction(Intents.ACTION_SAVE);
        intent.setType(Intents.MIME_TYPE_PLAINTEXT);
        intent.putExtra(Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startService(intent);
    }

    public static void startActionSave(Context context, LogcatLine logcatLine) {
        Intent intent = new Intent(context, SaveIntentService.class);
        intent.setAction(Intents.ACTION_SAVE);
        intent.setType(Intents.MIME_TYPE_LOGCAT);
        intent.putExtra(Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(Intent.EXTRA_TEXT, logcatLine.getOriginalLine());
        context.startService(intent);
    }

    public static void startActionSave(Context context, List<LogcatLine> logcatLineList) {
        // Use Google guava (dependent by dagger) here for transform!
        Iterable<CharSequence> iterator = Iterables.transform(logcatLineList, new Function<LogcatLine, CharSequence>() {
            @Override
            public CharSequence apply(LogcatLine input) {
                return input.getOriginalLine();
            }
        });
        CharSequence[] textLines = Iterables.toArray(iterator, CharSequence.class);
        Intent intent = new Intent(context, SaveIntentService.class);
        intent.setAction(Intents.ACTION_SAVE);
        intent.setType(Intents.MIME_TYPE_LOGCAT);
        intent.putExtra(Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(Intents.EXTRA_TEXT_LINES, textLines);
        context.startService(intent);
    }

    public SaveIntentService() {
        super("SaveIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Intents.ACTION_SAVE.equals(action)) {
                final String mimeType = intent.getType();
                final String senderPackageName = intent.getStringExtra(Intents.EXTRA_PACKAGE_NAME);
                final CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
                final CharSequence[] textLines = intent.getCharSequenceArrayExtra(Intents.EXTRA_TEXT_LINES);
                if (textLines != null) {
                    handleSave(senderPackageName, mimeType, textLines);
                } else if (text != null) {
                    handleSave(senderPackageName, mimeType, text);
                }
            }
        }
    }

    private void handleSave(String senderPackageName, String mimeType, CharSequence text) {
        try {
            ActiveAndroid.beginTransaction();
            MText model = MText.newInstance(senderPackageName, mimeType, text.toString());
            if (model != null) {
                model.save();
                mGlobalEventBus.postInMainThread(new OnSaveEvent(Arrays.asList(model)));
                ActiveAndroid.setTransactionSuccessful();
            }
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private void handleSave(String senderPackageName, String mimeType, CharSequence[] textLines) {
        try {
            ActiveAndroid.beginTransaction();
            List<MText> savedTextModelList = new ArrayList<MText>(textLines.length);
            for (CharSequence text : textLines) {
                MText model = MText.newInstance(senderPackageName, mimeType, text.toString());
                if (model != null) {
                    model.save();
                    savedTextModelList.add(model);
                }
            }
            mGlobalEventBus.postInMainThread(new OnSaveEvent(savedTextModelList));
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static class OnSaveEvent {

        private final List<MText> mSavedTextModelList;

        public OnSaveEvent(List<MText> savedTextModelList) {
            mSavedTextModelList = savedTextModelList;
        }

        public List<MText> getSavedTextModelList() {
            return mSavedTextModelList;
        }
    }
}
