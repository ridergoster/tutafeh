package com.esgi.ridergoster.tutafeh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.esgi.ridergoster.tutafeh.R;
import com.esgi.ridergoster.tutafeh.services.SocketInstance;
import com.esgi.ridergoster.tutafeh.services.TutafehEvents;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.input_username)
    EditText inputUsername;
    @BindView(R.id.input_username_btn)
    Button inputUsernameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketInstance.get().on(TutafehEvents.ON_CONNECT, onConnect);
        SocketInstance.get().on(TutafehEvents.ON_DISCONNECT, onDisconnect);
        SocketInstance.get().on(TutafehEvents.USER_CREATED, onUserCreated);

        SocketInstance.get().connect();

        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SocketInstance.get().disconnect();
    }

    @OnClick(R.id.input_username_btn)
    public void login () {
        String username = inputUsername.getText().toString();
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SocketInstance.get().emit(TutafehEvents.CREATE_USER, obj);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("socket", "connected");
            Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content),
                    "online !", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }
    };

    private Emitter.Listener onUserCreated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject data = (JSONObject) args[0];
                String username = data.getString("username");
                Log.d("socket", "connected");
                Intent myIntent = new Intent(MainActivity.this, RoomsActivity.class);
                myIntent.putExtra("username", username);
                MainActivity.this.startActivity(myIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content),
                    "offline...", Snackbar.LENGTH_SHORT);
            mySnackbar.show();

            SocketInstance.get().connect();
        }
    };
}
