package com.freehackquest.sample;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

class LogViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    TextView textView;
    LogViewHolder(RelativeLayout v) {
        super(v);
        textView = v.findViewById(R.id.log_text);
    }
}
