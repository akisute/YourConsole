package com.akisute.yourconsole.app.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akisute.yourconsole.R;
import com.akisute.yourconsole.app.dagger.ForInjecting;
import com.akisute.yourconsole.app.helper.LogcatHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConsoleListAdapter extends BaseAdapter {

    class ViewHolder {
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.tag)
        TextView tag;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<MText> mTextList = new ArrayList<MText>();

    @Inject
    public ConsoleListAdapter(@ForInjecting Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mTextList.size();
    }

    @Override
    public MText getItem(int position) {
        return mTextList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_console_item, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MText textModel = getItem(position);
        LogcatLine logcatLine = textModel.getLogcatLine();
        if (logcatLine != null) {
            viewHolder.tag.setText(logcatLine.getTag());
            viewHolder.tag.setTextColor(getColorForLogcatLine(logcatLine));
            viewHolder.text.setText(logcatLine.getLogOutput());
            viewHolder.text.setTextColor(getColorForLogcatLine(logcatLine));
        } else {
            viewHolder.tag.setText(textModel.getSenderPackageName());
            viewHolder.tag.setTextColor(mContext.getResources().getColor(R.color.font_text));
            viewHolder.text.setText(textModel.getText());
            viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.font_text));
        }

        return convertView;
    }

    public void clear() {
        mTextList.clear();
        notifyDataSetChanged();
    }

    public void load() {
        clear();
        List<MText> textList = MText.getAll();
        appendTexts(textList);
    }

    public void appendTexts(List<MText> textList) {
        mTextList.addAll(textList);
        notifyDataSetChanged();
    }

    private int getColorForLogcatLine(LogcatLine logcatLine) {
        switch (logcatLine.getLogLevel()) {
            case Log.DEBUG:
                return mContext.getResources().getColor(R.color.font_logcat_debug);
            case Log.ERROR:
                return mContext.getResources().getColor(R.color.font_logcat_error);
            case Log.INFO:
                return mContext.getResources().getColor(R.color.font_logcat_info);
            case Log.VERBOSE:
                return mContext.getResources().getColor(R.color.font_logcat_verbose);
            case Log.WARN:
                return mContext.getResources().getColor(R.color.font_logcat_warn);
            case LogcatHelper.LOG_WTF:
                return mContext.getResources().getColor(R.color.font_logcat_wtf);
        }
        return mContext.getResources().getColor(R.color.font_text);
    }
}
