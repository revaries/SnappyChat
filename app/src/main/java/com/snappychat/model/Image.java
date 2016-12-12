package com.snappychat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fabrizio on 12/12/2016.
 */

public class Image {

    @SerializedName("_id")
    private String id;
    private String data;
    private String type;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
