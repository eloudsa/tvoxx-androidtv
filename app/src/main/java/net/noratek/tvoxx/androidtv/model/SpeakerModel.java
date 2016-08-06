package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpeakerModel implements Parcelable {

    private String uuid;
    private String lastName;
    private String firstName;
    private String lang;
    private String company;
    private String avatarUrl;


    public SpeakerModel() {
    }

    public SpeakerModel(String uuid, String lastName, String firstName) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
    }


    public SpeakerModel(String uuid, String lastName, String firstName, String lang, String company, String avatarUrl) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.lang = lang;
        this.company = company;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.company);
        dest.writeString(this.avatarUrl);
    }

    protected SpeakerModel(Parcel in) {
        this.uuid = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.company = in.readString();
        this.avatarUrl= in.readString();
    }

    public static final Parcelable.Creator<SpeakerModel> CREATOR = new Parcelable.Creator<SpeakerModel>() {
        public SpeakerModel createFromParcel(Parcel source) {
            return new SpeakerModel(source);
        }

        public SpeakerModel[] newArray(int size) {
            return new SpeakerModel[size];
        }
    };
}
