package com.freehackquest.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
    private ArrayList<String> mDataset = new ArrayList<>();

    LogAdapter() {

    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_view_holder, parent, false);
        // ...
        LogViewHolder vh = new LogViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItem(String log) {
        mDataset.add(0, log);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mDataset.clear();
        notifyDataSetChanged();
    }
}
