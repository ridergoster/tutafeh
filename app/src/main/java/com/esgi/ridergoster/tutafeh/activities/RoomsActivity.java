package com.esgi.ridergoster.tutafeh.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.esgi.ridergoster.tutafeh.R;
import com.esgi.ridergoster.tutafeh.adapters.MyAdapter;
import com.esgi.ridergoster.tutafeh.models.Room;
import com.esgi.ridergoster.tutafeh.services.SocketInstance;
import com.esgi.ridergoster.tutafeh.services.TutafehEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class RoomsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;

    private ArrayList<Room> aRooms = new ArrayList<>();
    private Emitter.Listener onRoomsUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                aRooms.clear();

                JSONArray rooms = (JSONArray) args[0];

                for (int i = 0; i < rooms.length(); i++) {
                    JSONObject oJsonRoom = rooms.getJSONObject(i);
                    Room room = new Room();

                    room.setsRoomId(oJsonRoom.getString("roomId"));
                    room.setsLang(oJsonRoom.getString("lang"));
                    room.setiUsers(oJsonRoom.getInt("users"));

                    aRooms.add(room);

                    Log.d("roomId", oJsonRoom.getString("roomId"));
                    Log.d("lang", oJsonRoom.getString("lang"));
                    Log.d("users", Integer.toString(oJsonRoom.getInt("users")));
                }

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

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
