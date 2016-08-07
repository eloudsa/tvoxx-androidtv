package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eloudsa on 07/08/16.
 */
public class TalkModel implements Parcelable {

    private String talkId;
    private String title;
    private String thumbnailUrl;


    public TalkModel() {
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.talkId);
        dest.writeString(this.title);
        dest.writeString(this.thumbnailUrl);
    }

    protected TalkModel(Parcel in) {
        this.talkId = in.readString();
        this.title = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<TalkModel> CREATOR = new Parcelable.Creator<TalkModel>() {
        public TalkModel createFromParcel(Parcel source) {
            return new TalkModel(source);
        }

        public TalkModel[] newArray(int size) {
            return new TalkModel[size];
        }
    };
}
