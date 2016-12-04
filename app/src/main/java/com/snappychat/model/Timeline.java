package com.snappychat.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fabrizio on 3/12/2016.
 */

public class Timeline implements Serializable {

    private String id;
    private String comment;
    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
