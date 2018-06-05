package com.esgi.ridergoster.tutafeh.services;

import io.socket.client.Socket;

public class TutafehEvents {

    public static final String ON_CONNECT = Socket.EVENT_CONNECT;
    public static final String ON_DISCONNECT = Socket.EVENT_DISCONNECT;

    public static final String CREATE_USER = "CREATE_USER";
    public static final String USER_CREATED = "USER_CREATED";

    public static final String GET_ROOMS = "GET_ROOMS";

    public static final String JOIN_ROOM = "JOIN_ROOM";
    public static final String LEAVE_ROOM = "LEAVE_ROOM";
    public static final String ROOMS_UPDATE = "ROOMS_UPDATE";

    public static final String USER_JOINED = "USER_JOINED";
    public static final String USER_LEFT = "USER_LEFT";

    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String MESSAGE_RECEIVED = "MESSAGE_RECEIVED";

    public static final String TYPING = "TYPING";
    public static final String STOP_TYPING = "STOP_TYPING";
}
