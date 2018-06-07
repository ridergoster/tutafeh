package com.esgi.ridergoster.tutafeh.models;

public class Room {

    private String sRoomId;
    private String sLang;
    private int iUsers;

    public Room() {}

    public Room(String sRoomId, String sLang, int iUsers) {
        this.sRoomId = sRoomId;
        this.sLang = sLang;
        this.iUsers = iUsers;
    }

    public String getsRoomId() {
        return sRoomId;
    }

    public void setsRoomId(String sRoomId) {
        this.sRoomId = sRoomId;
    }

    public String getsLang() {
        return sLang;
    }

    public void setsLang(String sLang) {
        this.sLang = sLang;
    }

    public int getiUsers() {
        return iUsers;
    }

    public void setiUsers(int iUsers) {
        this.iUsers = iUsers;
    }
}
