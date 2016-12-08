package com.snappychat.model;

/**
 * Created by Fabrizio on 6/12/2016.
 */

public class MessageEvent {

    private ChatMessage chatMessage;

    public MessageEvent(ChatMessage chatMessage){
        this.setChatMessage(chatMessage);
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
