package com.akisute.yourconsole.app;

import android.content.Context;
import android.content.Intent;

import com.activeandroid.ActiveAndroid;
import com.akisute.yourconsole.app.dagger.DaggeredIntentService;
import com.akisute.yourconsole.app.intent.Intents;
import com.akisute.yourconsole.app.model.LogcatLine;
import com.akisute.yourconsole.app.model.MText;
import com.akisute.yourconsole.app.util.GlobalEventBus;

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
                final String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                handleSave(senderPackageName, mimeType, text);
            }
        }
    }

    private void handleSave(String senderPackageName, String mimeType, String text) {
        try {
            ActiveAndroid.beginTransaction();
            MText model = MText.newInstance(senderPackageName, mimeType, text);
            if (model != null) {
                model.save();
                mGlobalEventBus.postInMainThread(new OnSaveEvent(model));
                ActiveAndroid.setTransactionSuccessful();
            }
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static class OnSaveEvent {

        private final MText mSavedTextModel;

        public OnSaveEvent(MText savedTextModel) {
            mSavedTextModel = savedTextModel;
        }

        public MText getSavedTextModel() {
            return mSavedTextModel;
        }
    }
}
