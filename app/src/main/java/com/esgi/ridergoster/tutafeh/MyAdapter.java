package com.esgi.ridergoster.tutafeh;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<String> mDataset;

    public MyAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_selectable_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;

        ViewHolder(TextView v) {
            super(v);
            mTextView = v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Snackbar mySnackbar = Snackbar.make(v, "clicked!", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }
    }
}
