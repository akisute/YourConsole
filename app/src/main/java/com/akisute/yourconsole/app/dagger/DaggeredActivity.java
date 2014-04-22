package com.akisute.yourconsole.app.dagger;


import android.app.Activity;
import android.os.Bundle;

public class DaggeredActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.akisute.yourconsole.app.Application application = (com.akisute.yourconsole.app.Application) getApplication();
        application.inject(this);
    }
}
