package com.example.peter.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Steps {

    @SerializedName(value = "id", alternate = "mId")
    private int mId;

    @SerializedName(value = "shortDescription", alternate = "mShortDescription")
    private String mShortDescription;

    @SerializedName(value = "description", alternate = "mDescription")
    private String mDescription;

    @SerializedName(value = "videoURL", alternate = "mVideoUrl")
    private String mVideoUrl;

    @SerializedName(value = "thumbnailURL", alternate = "mThumbnailUrl")
    private String mThumbnailUrl;

    public Steps(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl){
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }
}
