package com.akisute.yourconsole.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akisute.yourconsole.R;
import com.akisute.yourconsole.app.dagger.DaggeredFragment;

public class ConsoleViewerFragment extends DaggeredFragment {

    public ConsoleViewerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_console_viewer, container, false);
    }

}
