package com.example.mymessenger.message;

public class MessengerItem {
    private String name;
    private String message;

    public MessengerItem() {
    }

    MessengerItem(String message, String name) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
