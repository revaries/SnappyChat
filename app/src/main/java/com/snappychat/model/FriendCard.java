package com.snappychat.model;

/**
 * Created by Jelson on 12/4/2016.
 */

public class FriendCard {
    private String mName;
    private String mLast;
    private String mEmail;
    private String mDescription;
    private String mVisibility;
    private Boolean mFriends;
    private Boolean mPending;
    private Boolean mRequested;

    public FriendCard(String name, String last, String email, String description, String visibility,
                      boolean friends, boolean pending, boolean requested){
        this.mName = name;
        this.mLast = last;
        this.mEmail = email;
        this.mDescription = description;
        this.mVisibility = visibility;
        this.mFriends = friends;
        this.mPending = pending;
        this.mRequested = requested;
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

    public String getVisibility() {
        return mVisibility;
    }

    public void setVisibility(String visibility) {
        this.mVisibility = visibility;
    }

    public Boolean getFriends() {
        return mFriends;
    }

    public void setFriends(Boolean friends) {
        this.mFriends = friends;
    }

    public Boolean getPending() {
        return mPending;
    }

    public void setPending(Boolean pending) {
        this.mPending = pending;
    }

    public Boolean getRequested() {
        return mRequested;
    }

    public void setRequested(Boolean requested) {
        this.mRequested = requested;
    }

}
