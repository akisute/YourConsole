package com.akisute.yourconsole.app.dagger;

import android.app.Activity;
import android.app.Fragment;

public abstract class DaggeredFragment extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DaggeredApplication application = (DaggeredApplication) activity.getApplication();
        application.inject(this);
    }
}
