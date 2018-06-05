package com.esgi.ridergoster.tutafeh.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.esgi.ridergoster.tutafeh.R;
import com.esgi.ridergoster.tutafeh.services.SocketInstance;
import com.esgi.ridergoster.tutafeh.services.TutafehEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class RoomsActivity extends AppCompatActivity {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketInstance.get().disconnect();

        SocketInstance.get().off(TutafehEvents.ROOMS_UPDATE);
    }

    private Emitter.Listener onRoomsUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONArray rooms = (JSONArray) args[0];
                for (int i = 0; i < rooms.length(); i++) {
                    JSONObject room = rooms.getJSONObject(i);
                    Log.d("roomId", room.getString("roomId"));
                    Log.d("lang", room.getString("lang"));
                    Log.d("users", Integer.toString(room.getInt("users")));
                }
            } catch (JSONException error) {
                error.printStackTrace();
            }
        }
    };
}
