package com.snappychat.model;

import com.google.gson.annotations.SerializedName;

import java.util.Random;

/**
 * Created by Jelson on 11/27/16.
 */


public class ChatMessage {

    @SerializedName("chat_id")
    private String chatId;
    public String body;
    private String sender;
    private String receiver;
    public String senderName;
    public String Date, Time;
    public String msgid;
    public boolean isMine;// Did I send the message.
    public String type;

    public ChatMessage(String Sender, String Receiver, String messageString,
                       String ID, boolean isMINE, String Type) {
        body = messageString;
        isMine = isMINE;
        setSender(Sender);
        msgid = ID;
        setReceiver(Receiver);
        senderName = getSender();
        setType(Type);
        type = getType();
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



