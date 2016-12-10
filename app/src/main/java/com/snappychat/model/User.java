package com.snappychat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fabrizio on 2/12/2016.
 */

public class User implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("image.data")
    private String image;
    @SerializedName("nick_name")
    private String nickName;
    @SerializedName("location")
    private String location;
    @SerializedName("about_me")
    private String AboutMe;
    @SerializedName("interests")
    private String interests;
    @SerializedName("notification")
    private Boolean notification;
    @SerializedName("visibility")
    private String visibility;
    private String token;
    private String age;
    private String profession;
    private Boolean status;
    private String email;
    private List<String>friends;
    private transient boolean friend;
    private transient String message;
    private transient Boolean pending;
    private transient Boolean chatOwner;
    private transient String chatConversationId;
    private transient Bitmap image1;

    public User(){};

    public User(String id, String firstName, String lastName, String nickName, String image){
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setNickName(nickName);
        this.setImage(image);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof String)
        {
            sameSame = this.id.equals((String) object);
        }

        return sameSame;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Bitmap getImageIntoBitmap(){
        Bitmap decodedByte = null;
        try {
            byte[] decodedString = Base64.decode(image,Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }catch (Exception ex){
            Log.e("User", "Error converting image to bitmap",ex);
        }
        return decodedByte;
    }

    public String getAboutMe() {
        return AboutMe;
    }

    public void setAboutMe(String aboutMe) {
        AboutMe = aboutMe;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean getChatOwner() {
        return chatOwner;
    }

    public void setChatOwner(Boolean chatOwner) {
        this.chatOwner = chatOwner;
    }

    public String getChatConversationId() {
        return chatConversationId;
    }

    public void setChatConversationId(String chatId) {
        this.chatConversationId = chatId;
    }

    public Bitmap getImage1() {
        return image1;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }
}
