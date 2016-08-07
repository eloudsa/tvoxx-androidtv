package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eloudsa on 07/08/16.
 */
public class SpeakerFullModel implements Parcelable {

    private String uuid;
    private String firstName;
    private String lastName;
    private String lang;
    private String company;
    private String avatarUrl;
    private String bio;
    private List<TalkModel> talks;


    public SpeakerFullModel() {
    }

    public SpeakerFullModel(String uuid, String lastName, String firstName) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
    }


    public SpeakerFullModel(String uuid, String lastName, String firstName, String lang, String company, String avatarUrl, String bio, List<TalkModel> talks) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.lang = lang;
        this.company = company;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.talks = talks;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<TalkModel> getTalks() {
        return talks;
    }

    public void setTalks(List<TalkModel> talks) {
        this.talks = talks;
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
        dest.writeString(this.lang);
        dest.writeString(this.company);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.bio);
        dest.writeTypedList(this.talks);
    }

    protected SpeakerFullModel(Parcel in) {
        this.uuid = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.lang = in.readString();
        this.company = in.readString();
        this.avatarUrl= in.readString();
        this.bio= in.readString();
        this.talks = new ArrayList<TalkModel>();
        in.readTypedList(talks, TalkModel.CREATOR);
    }

    public static final Parcelable.Creator<SpeakerFullModel> CREATOR = new Parcelable.Creator<SpeakerFullModel>() {
        public SpeakerFullModel createFromParcel(Parcel source) {
            return new SpeakerFullModel(source);
        }

        public SpeakerFullModel[] newArray(int size) {
            return new SpeakerFullModel[size];
        }
    };

}
