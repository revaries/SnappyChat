package com.snappychat.friends;

/**
 * Created by Jelson on 12/4/2016.
 */

public class FriendCard {
    private String mName;
    private String mLast;
    private String mEmail;
    private String mDescription;

    public FriendCard(String name, String last, String email, String description){
        this.mName = name;
        this.mLast = last;
        this.mEmail = email;
        this.mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLast() {
        return mLast;
    }

    public void setLast(String last) {
        this.mLast = last;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

}
