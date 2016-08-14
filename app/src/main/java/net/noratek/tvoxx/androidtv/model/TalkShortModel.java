package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eloudsa on 07/08/16.
 */
public class TalkShortModel implements Parcelable {

    private String talkId;
    private String title;
    private String thumbnailUrl;


    public TalkShortModel() {
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

    protected TalkShortModel(Parcel in) {
        this.talkId = in.readString();
        this.title = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<TalkShortModel> CREATOR = new Parcelable.Creator<TalkShortModel>() {
        public TalkShortModel createFromParcel(Parcel source) {
            return new TalkShortModel(source);
        }

        public TalkShortModel[] newArray(int size) {
            return new TalkShortModel[size];
        }
    };
}
