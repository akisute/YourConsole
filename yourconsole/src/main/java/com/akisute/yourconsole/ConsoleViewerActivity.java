package com.akisute.yourconsole;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.akisute.yourconsole.model.ConsoleBuffer;
import com.akisute.yourconsole.model.ConsoleBufferLogcatLoader;
import com.akisute.yourconsole.model.StringLine;

import java.io.IOException;
import java.util.List;

public class ConsoleViewerActivity extends Activity {

    class ViewHolder {
        ScrollView scrollView;
        TextView textView;
    }

    private ViewHolder mViewHolder;
    private ConsoleBuffer mConsoleBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_viewer);

        mConsoleBuffer = new ConsoleBuffer();

        mViewHolder = new ViewHolder();
        mViewHolder.scrollView = (ScrollView) findViewById(R.id.scrollView);
        mViewHolder.textView = (TextView) findViewById(R.id.textView);
        mViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConsoleBuffer.addStringLine("God Fuck");
                updateText();
            }
        });

        loadInitialConsoleBufferFromDB();
        //startLoadingConsoleBufferFromLogcat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateText();
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
                updateText();
                return true;
            case R.id.action_settings:
                Log.d(this.getClass().toString(), "Settings");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadInitialConsoleBufferFromDB() {
        List<StringLine> lines = StringLine.getAll();
        for (StringLine line : lines) {
            mConsoleBuffer.addStringLine(line.toString());
        }
        updateText();
    }

    private void updateText() {
        // TODO: can use following code to update backing buffer of textView. Very useful when implementing line count restrictions.
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
        } else {
            mViewHolder.textView.setText("");
            mViewHolder.scrollView.post(new Runnable() {
                @Override
                public void run() {
                    mViewHolder.scrollView.fullScroll(View.FOCUS_UP);
                }
            });
        }
    }

    private void startLoadingConsoleBufferFromLogcat() {
        // TODO: better asynchronous logcat reading like using a Service to make lifetime controllable. For now this is good enough.
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConsoleBufferLogcatLoader logcatLoader = null;
                try {
                    logcatLoader = new ConsoleBufferLogcatLoader(mConsoleBuffer);
                    logcatLoader.load();
                } catch (IOException e) {
                    Log.e(this.getClass().toString(), "unexpected exception", e);
                } finally {
                    if (logcatLoader != null) {
                        try {
                            logcatLoader.close();
                        } catch (IOException e) {
                            Log.e(this.getClass().toString(), "unexpected exception", e);
                        }
                    }
                }
            }
        }).start();
    }
}
