package com.esgi.ridergoster.tutafeh.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.esgi.ridergoster.tutafeh.R;
import com.esgi.ridergoster.tutafeh.adapters.MessageAdapter;
import com.esgi.ridergoster.tutafeh.models.Message;
import com.esgi.ridergoster.tutafeh.models.Occurencies;
import com.esgi.ridergoster.tutafeh.services.SocketInstance;
import com.esgi.ridergoster.tutafeh.services.TutafehEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    EditText mInputMessage;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Message> aMessages = new ArrayList<>();
    private String sUsername;
    private String sRoomId;
    Button mFirst;
    Button mSecond;
    Button mThird;
    private String sLang;
    private String sInput;
    private String[] aRecommendations = new String[3];
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject message = (JSONObject) args[0];

            runOnUiThread(
                    new Runnable() {
                        public void run() {
                            try {
                                addMessage(message.getString("username"), message.getString("message"));

                                Log.d("roomId", message.getString("roomId"));
                                Log.d("username", message.getString("username"));
                                Log.d("message", message.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sUsername = getIntent().getStringExtra("username");
        sRoomId = getIntent().getStringExtra("roomId");
        sLang = getIntent().getStringExtra("lang");

        Objects.requireNonNull(getSupportActionBar()).setTitle(
                sRoomId + " (" +
                        getIntent().getStringExtra("users") +
                        " users connected)"
        );

        mRecyclerView = findViewById(R.id.messages);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(this, aMessages);
        mRecyclerView.setAdapter(mAdapter);

        mInputMessage = findViewById(R.id.messageText);

        mFirst = findViewById(R.id.firstProposal);
        mSecond = findViewById(R.id.secondProposal);
        mThird = findViewById(R.id.thirdProposal);

        mFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMessage.setText(mInputMessage.getText().append(mFirst.getText()).append(' '));
                setRecommendations(
                        mInputMessage.getText().toString()
                                .replace('.', ' ')
                                .replace(',', ' ')
                                .replace(';', ' ')
                                .replace("\"", " ")
                                .replace('\'', ' ')
                                .replace('?', ' ')
                                .replace('!', ' ')
                                .replaceAll("\\s+", " ")
                                .toLowerCase().trim().split(" ")
                );
            }
        });

        mSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMessage.setText(mInputMessage.getText().append(mSecond.getText()).append(' '));
                setRecommendations(
                        mInputMessage.getText().toString()
                                .replace('.', ' ')
                                .replace(',', ' ')
                                .replace(';', ' ')
                                .replace("\"", " ")
                                .replace('\'', ' ')
                                .replace('?', ' ')
                                .replace('!', ' ')
                                .replaceAll("\\s+", " ")
                                .toLowerCase().trim().split(" ")
                );
            }
        });

        mThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMessage.setText(mInputMessage.getText().append(mThird.getText()).append(' '));
                setRecommendations(
                        mInputMessage.getText().toString()
                                .replace('.', ' ')
                                .replace(',', ' ')
                                .replace(';', ' ')
                                .replace("\"", " ")
                                .replace('\'', ' ')
                                .replace('?', ' ')
                                .replace('!', ' ')
                                .replaceAll("\\s+", " ")
                                .toLowerCase().trim().split(" ")
                );
            }
        });

        mInputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    char lastChar = s.charAt(s.length() - 1);

                    if (lastChar == ' ') {
                        setRecommendations(
                                s.toString()
                                        .replace('.', ' ')
                                        .replace(',', ' ')
                                        .replace(';', ' ')
                                        .replace("\"", " ")
                                        .replace('\'', ' ')
                                        .replace('?', ' ')
                                        .replace('!', ' ')
                                        .replaceAll("\\s+", " ")
                                        .toLowerCase().trim().split(" ")
                        );
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button sendBtn = findViewById(R.id.sendButton);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        SocketInstance.get().on(TutafehEvents.MESSAGE_RECEIVED, onMessageReceived);
    }

    private void setRecommendations(String[] split) {
        StringBuilder sb = new StringBuilder();
        String separator = "";
        List<Occurencies> aTotals = new ArrayList<>();

        for (int i = split.length - 4; i < split.length; i++) {
            if (i < 0) {
                continue;
            }
            sb.append(separator).append(split[i]);
            separator = ",";

            RealmResults<Occurencies> aResults = Realm.getDefaultInstance()
                    .where(Occurencies.class)
                    .equalTo("words", sb.toString())
                    .and()
                    .equalTo("language", sLang)
                    .sort("occurencies", Sort.DESCENDING)
                    .findAll();

            if (aResults.size() > 0 && aResults.first() != null && aResults.first().getOccurencies() > 3) {
                aTotals = aResults;
            }
        }

        if (aTotals.size() > 0) {
            mFirst.setText(aTotals.get(0).getResult());

            if (aTotals.get(1) != null) {
                mSecond.setText(aTotals.get(1).getResult());
            }

            if (aTotals.get(2) != null) {
                mThird.setText(aTotals.get(2).getResult());
            }
        }

        mInputMessage.requestFocus();
        mInputMessage.setSelection(mInputMessage.length() - 1);
    }

    private void attemptSend() {
        if (null == sUsername) return;

        String message = mInputMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessage.requestFocus();
            return;
        }

        mInputMessage.setText("");

        JSONObject obj = new JSONObject();
        try {
            obj.put("roomId", sRoomId);
            obj.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SocketInstance.get().emit(TutafehEvents.SEND_MESSAGE, obj);
    }

    private void addMessage(String username, String message) {
        aMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(aMessages.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketInstance.get().disconnect();

        SocketInstance.get().off(TutafehEvents.MESSAGE_RECEIVED);
    }
}
