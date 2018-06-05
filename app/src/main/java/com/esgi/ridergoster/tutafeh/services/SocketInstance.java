package com.esgi.ridergoster.tutafeh.services;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Vincent on 24/07/2016.
 */
public class SocketInstance {

    static SocketInstance INSTANCE;
    private  Socket mSocket;
    private final static String CHAT_SERVER_URL = "https://tutafeh-server.herokuapp.com";

    public SocketInstance(){
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket get() {
        if(INSTANCE == null){
            INSTANCE = new SocketInstance();
        }
        return INSTANCE.mSocket;
    }
}
