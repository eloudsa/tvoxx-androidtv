package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by eloudsa on 22/08/16.
 */
public class Etag extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id;
    private String eTag;
    private String url;

    public Etag() {
    }


    public Etag(String id, String eTag, String url) {
        this.id = id;
        this.eTag = eTag;
        this.url = url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return eTag;
    }

    public void setEtag(String eTag) {
        this.eTag = eTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eTag);
        dest.writeString(this.url);
    }

    protected Etag(Parcel in) {
        this.eTag = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Etag> CREATOR = new Parcelable.Creator<Etag>() {
        public Etag createFromParcel(Parcel source) {
            return new Etag(source);
        }

        public Etag[] newArray(int size) {
            return new Etag[size];
        }
    };


}
