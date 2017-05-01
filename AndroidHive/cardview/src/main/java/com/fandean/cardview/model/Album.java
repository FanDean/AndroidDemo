package com.fandean.cardview.model;

/**
 * Created by fan on 17-5-1.
 */

public class Album {
    private String mName;
    private int mNumOfSongs;
    private int mThumbnail;

    public Album() {
    }

    public Album(String name, int numOfSongs, int thumbnail) {
        mName = name;
        mNumOfSongs = numOfSongs;
        mThumbnail = thumbnail;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumOfSongs() {
        return mNumOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        mNumOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        mThumbnail = thumbnail;
    }
}
