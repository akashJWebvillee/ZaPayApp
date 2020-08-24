package com.org.zapayapp.chat;

public class ChatMessageModel {
    public static final int RECEIVED_MSG = 0;
    public static final int SEND_MSG = 1;


    private String message;
    private int msgType;

    public ChatMessageModel(String message, int msgType) {
        this.message = message;
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public int getMsgType() {
        return msgType;
    }
}
