package com.fandean.androidhive.model;

import java.io.Serializable;

/**
 * Created by fan on 17-4-26.
 */

public class Image implements Serializable {
    private String name;
    private String small, medium, large;
    private String timestamp;

    public Image() {
    }

    public Image(String large, String medium, String name, String small, String timestamp) {
        this.large = large;
        this.medium = medium;
        this.name = name;
        this.small = small;
        this.timestamp = timestamp;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
