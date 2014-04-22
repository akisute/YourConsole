package com.akisute.yourconsole.app.dagger;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class DaggeredFragment extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        com.akisute.yourconsole.app.Application application = (com.akisute.yourconsole.app.Application) activity.getApplication();
        application.inject(this);
    }
}
