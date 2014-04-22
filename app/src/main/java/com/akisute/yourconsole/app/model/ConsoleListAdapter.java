package com.akisute.yourconsole.app.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConsoleListAdapter extends BaseAdapter {

    private class ViewHolder {
        TextView textView;
    }

    private final LayoutInflater mLayoutInflater;
    private List<String> mLineList = new ArrayList<String>();

    public ConsoleListAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setLineList(List<String> lineList) {
        for (String line : lineList) {
            mLineList.add(line);
        }
        notifyDataSetChanged();
    }

    public void addLine(String line){
        mLineList.add(line);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLineList.size();
    }

    @Override
    public String getItem(int position) {
        return mLineList.get(position);
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

        String line = getItem(position);
        viewHolder.textView.setText(line);

        return convertView;
    }
}
