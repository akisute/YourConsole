package com.akisute.yourconsole.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.akisute.yourconsole.R;
import com.akisute.yourconsole.app.dagger.DaggeredActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_start_logcat_recording:
                LogcatRecordingService.startLogcatRecording(this);
                return true;
            case R.id.action_stop_logcat_recording:
                LogcatRecordingService.stopLogcatRecording(this);
                return true;
            case R.id.action_settings:
                SaveIntentService.startActionSave(this, "Settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
