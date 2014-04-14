package com.akisute.yourconsole;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.akisute.yourconsole.com.akisute.yourconsole.model.ConsoleBuffer;

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
        mConsoleBuffer.addString(getResources().getString(R.string.hello_world));

        mViewHolder = new ViewHolder();
        mViewHolder.scrollView = (ScrollView) findViewById(R.id.scrollView);
        mViewHolder.textView = (TextView) findViewById(R.id.textView);
        mViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConsoleBuffer.addStringLine("Abesi");
                updateText();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateText() {
        setTitle(String.format("%s (%d lines)", getResources().getString(R.string.app_name), mConsoleBuffer.getNumberOfLines()));
        mViewHolder.textView.setText(mConsoleBuffer.getText());
        mViewHolder.scrollView.post(new Runnable() {
            @Override
            public void run() {
                mViewHolder.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
