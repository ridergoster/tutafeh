package com.esgi.ridergoster.tutafeh.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.ridergoster.tutafeh.R;
import com.esgi.ridergoster.tutafeh.activities.ChatActivity;
import com.esgi.ridergoster.tutafeh.models.Room;
import com.esgi.ridergoster.tutafeh.services.SocketInstance;
import com.esgi.ridergoster.tutafeh.services.TutafehEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Room> mDataset;

    public MyAdapter(ArrayList<Room> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNameTextView.setText(mDataset.get(position).getsRoomId());
        holder.mLangTextView.setText(mDataset.get(position).getsLang());
        holder.mUsersTextView.setText(String.valueOf(mDataset.get(position).getiUsers()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTextView;
        TextView mLangTextView;
        TextView mUsersTextView;

        ViewHolder(View v) {
            super(v);

            mNameTextView = v.findViewById(R.id.roomId);
            mLangTextView = v.findViewById(R.id.roomLang);
            mUsersTextView = v.findViewById(R.id.roomUsers);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra("username", ((Activity) v.getContext()).getIntent().getStringExtra("username"));
            intent.putExtra("roomId", mNameTextView.getText());
            intent.putExtra("users", mUsersTextView.getText());
            intent.putExtra("lang", mLangTextView.getText());

            JSONObject data = new JSONObject();

            try {
                data.put("roomId", mNameTextView.getText());
                data.put("lang", mLangTextView.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SocketInstance.get().emit(TutafehEvents.JOIN_ROOM, data);
            v.getContext().startActivity(intent);
        }
    }
}
