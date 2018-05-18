package com.example.peter.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Steps implements Parcelable{


    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

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

    public Steps(int id, String shortDescription, String description, String videoUrl,
                 String thumbnailUrl){
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }

    protected Steps(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getStepId() { return mId; }
    public String getShortDescription() { return mShortDescription; }
    public String getDescription() { return mDescription; }
    public String getThumbnailUrl() { return mThumbnailUrl; }
    public String getVideoUrl() { return mVideoUrl; }
}
