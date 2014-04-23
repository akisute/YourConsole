package com.akisute.yourconsole.app;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.akisute.yourconsole.R;
import com.akisute.yourconsole.app.dagger.DaggeredFragment;
import com.akisute.yourconsole.app.model.ConsoleListAdapter;
import com.akisute.yourconsole.app.model.MText;
import com.akisute.yourconsole.app.util.GlobalEventBus;
import com.squareup.otto.Subscribe;

import java.util.Arrays;

import javax.inject.Inject;

public class ConsoleViewerFragment extends DaggeredFragment {

    private class ViewHolder {
        ListView listView;
    }

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            getActivity().setTitle(String.format("%s (%d Lines)", getResources().getString(R.string.app_name), mAdapter.getCount()));
            if (mViewHolder != null) {
                final int position = mAdapter.getCount() - 1;
                mViewHolder.listView.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewHolder.listView.smoothScrollToPosition(position);
                    }
                });
            }
        }
    };

    @Inject
    GlobalEventBus mGlobalEventBus;
    @Inject
    ConsoleListAdapter mAdapter;

    private ViewHolder mViewHolder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter.registerDataSetObserver(mDataSetObserver);
        mAdapter.load();
        mGlobalEventBus.register(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGlobalEventBus.unregister(this);
        mAdapter.unregisterDataSetObserver(mDataSetObserver);
        mAdapter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LogcatRecordingService.startLogcatRecording(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogcatRecordingService.stopLogcatRecording(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_console_viewer, container, false);

        mViewHolder = new ViewHolder();
        mViewHolder.listView = (ListView) view.findViewById(android.R.id.list);
        mViewHolder.listView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.console_viewer_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear_all_log:
                MText.removeAll();
                mAdapter.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onSaveEvent(SaveIntentService.OnSaveEvent event) {
        mAdapter.appendTexts(Arrays.asList(event.getSavedTextModel()));
    }
}
