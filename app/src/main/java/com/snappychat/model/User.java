package com.snappychat.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
    private String email;
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
}
