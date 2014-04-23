package com.akisute.yourconsole.app.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akisute.yourconsole.app.dagger.ForInjecting;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ConsoleListAdapter extends BaseAdapter {

    private class ViewHolder {
        TextView textView;
    }

    private final LayoutInflater mLayoutInflater;
    private final List<MText> mTextList = new ArrayList<MText>();

    @Inject
    public ConsoleListAdapter(@ForInjecting Context context) {
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
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MText textModel = getItem(position);
        viewHolder.textView.setText(textModel.getText());

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
}
