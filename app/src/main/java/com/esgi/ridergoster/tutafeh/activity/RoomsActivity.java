package com.esgi.ridergoster.tutafeh.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.esgi.ridergoster.tutafeh.MyAdapter;
import com.esgi.ridergoster.tutafeh.R;
import com.esgi.ridergoster.tutafeh.services.SocketInstance;
import com.esgi.ridergoster.tutafeh.services.TutafehEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import io.socket.emitter.Emitter;

public class RoomsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;

    private ArrayList<String> aRooms = new ArrayList<>();
    private Emitter.Listener onRoomsUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                aRooms.clear();

                JSONArray aJSONRooms = new JSONArray();
                JSONArray rooms = (JSONArray) args[0];

                for (int i = 0; i < rooms.length(); i++) {
                    JSONObject room = rooms.getJSONObject(i);

                    aJSONRooms.put(room);
                    aRooms.add(room.getString("roomId"));

                    Log.d("roomId", room.getString("roomId"));
                    Log.d("lang", room.getString("lang"));
                    Log.d("users", Integer.toString(room.getInt("users")));
                }

                Log.d("rooms", Arrays.toString(aRooms.toArray(new String[0])));

                runOnUiThread(
                        new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                );
            } catch (JSONException error) {
                error.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        SocketInstance.get().on(TutafehEvents.ROOMS_UPDATE, onRoomsUpdate);
        SocketInstance.get().emit(TutafehEvents.GET_ROOMS);

        String username = getIntent().getStringExtra("username");

        Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content),
                "user " + username + " connected !", Snackbar.LENGTH_LONG);
        mySnackbar.show();

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(aRooms);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketInstance.get().disconnect();

        SocketInstance.get().off(TutafehEvents.ROOMS_UPDATE);
    }
}
