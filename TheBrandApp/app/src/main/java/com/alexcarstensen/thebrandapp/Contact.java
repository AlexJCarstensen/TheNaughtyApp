package com.alexcarstensen.thebrandapp;

/**
 * Created by Peter Ring on 07/10/2016.
 */

public class Contact {

    private String email;
    private String chat;
    private boolean isAdded;

public Contact()
{

}

    public Contact(String email, String chat) {
        this.email = email;
        this.chat = chat;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
