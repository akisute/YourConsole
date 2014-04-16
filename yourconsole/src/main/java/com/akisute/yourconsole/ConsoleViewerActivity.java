package com.akisute.yourconsole;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.akisute.yourconsole.model.ConsoleBuffer;
import com.akisute.yourconsole.model.ConsoleBufferLoader;

public class ConsoleViewerActivity extends Activity {

    class ViewHolder {
        ScrollView scrollView;
        TextView textView;
    }

    private ViewHolder mViewHolder;
    private ConsoleBuffer mConsoleBuffer;
    private ConsoleBufferLoader mConsoleBufferLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_viewer);

        mConsoleBuffer = new ConsoleBuffer();
        mConsoleBufferLoader = new ConsoleBufferLoader(mConsoleBuffer);

        mViewHolder = new ViewHolder();
        mViewHolder.scrollView = (ScrollView) findViewById(R.id.scrollView);
        mViewHolder.textView = (TextView) findViewById(R.id.textView);
        mViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveIntentService.startActionSave(view.getContext(), "Hello, SaveIntentService!");
                updateText();
            }
        });

        initializeText();
        mConsoleBufferLoader.startTailing();
        LogcatRecordingService.startLogcatRecording(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConsoleBufferLoader.stopTailing();
        LogcatRecordingService.stopLogcatRecording(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.console_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_reload:
                initializeText();
                return true;
            case R.id.action_settings:
                Log.d(this.getClass().toString(), "Settings");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeText() {
        mViewHolder.textView.setText("");
        mConsoleBufferLoader.load();
        updateText();
    }

    private void updateText() {
        // TODO: updateText() when ConsoleBufferLoader.OnTailEvent has been fired.
        // Note:
        // Following code can be used to update backing buffer of textView. Very useful when implementing line count restrictions.
        /*
            Editable editable = mViewHolder.textView.getEditableText();
            if (editable != null) {
                editable.delete(0, 100);
            }
         */
        setTitle(String.format("%s (%d lines)", getResources().getString(R.string.app_name), mConsoleBuffer.getNumberOfLines()));
        String text = mConsoleBuffer.getTextFromLastReadLine();
        if (text != null) {
            mViewHolder.textView.append(text);
            mViewHolder.scrollView.post(new Runnable() {
                @Override
                public void run() {
                    mViewHolder.scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }
}
