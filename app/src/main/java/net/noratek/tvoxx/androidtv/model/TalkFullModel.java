package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by eloudsa on 07/08/16.
 */
public class TalkFullModel implements Parcelable {

    private String talkId;
    private String talkType;
    private String trackId;
    private String trackTitle;
    private String title;
    private String conferenceEventCode;
    private String conferenceLabel;
    private String summary;
    private String lang;
    private String youtubeVideoId;
    private String thumbnailUrl;
    private float averageRating;
    private int numberOfRatings;
    private int durationInSeconds;

    private List<String> speakerUuids;

    private List<String> speakerNames;

    private List<SpeakerModel> speakers;



    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public String getTalkType() {
        return talkType;
    }

    public void setTalkType(String talkType) {
        this.talkType = talkType;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConferenceEventCode() {
        return conferenceEventCode;
    }

    public void setConferenceEventCode(String conferenceEventCode) {
        this.conferenceEventCode = conferenceEventCode;
    }

    public String getConferenceLabel() {
        return conferenceLabel;
    }

    public void setConferenceLabel(String conferenceLabel) {
        this.conferenceLabel = conferenceLabel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public List<String> getSpeakerUuids() {
        return speakerUuids;
    }

    public void setSpeakerUuids(List<String> speakerUuids) {
        this.speakerUuids = speakerUuids;
    }

    public List<String> getSpeakerNames() {
        return speakerNames;
    }

    public void setSpeakerNames(List<String> speakerNames) {
        this.speakerNames = speakerNames;
    }

    public List<SpeakerModel> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<SpeakerModel> speakers) {
        this.speakers = speakers;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.talkId);
        dest.writeString(this.talkType);
        dest.writeString(this.trackId);
        dest.writeString(this.trackTitle);
        dest.writeString(this.title);
        dest.writeString(this.conferenceEventCode);
        dest.writeString(this.conferenceLabel);
        dest.writeString(this.summary);
        dest.writeString(this.lang);
        dest.writeString(this.youtubeVideoId);
        dest.writeString(this.thumbnailUrl);
        dest.writeFloat(this.averageRating);
        dest.writeInt(this.numberOfRatings);
        dest.writeInt(this.durationInSeconds);

        dest.writeStringList(this.speakerUuids);
        dest.writeStringList(this.speakerNames);

        dest.writeTypedList(this.speakers);
    }

    protected TalkFullModel(Parcel in) {
        this.talkId = in.readString();;
        this.talkType = in.readString();;
        this.trackId = in.readString();;
        this.trackTitle = in.readString();;
        this.title = in.readString();;
        this.conferenceEventCode = in.readString();;
        this.conferenceLabel = in.readString();;
        this.summary = in.readString();;
        this.lang = in.readString();;
        this.youtubeVideoId = in.readString();;
        this.thumbnailUrl = in.readString();;
        this.averageRating = in.readFloat();
        this.numberOfRatings = in.readInt();
        this.durationInSeconds = in.readInt();
        in.readStringList(speakerUuids);
        in.readStringList(speakerNames);
        in.readTypedList(speakers, SpeakerModel.CREATOR);
    }

    public static final Creator<TalkFullModel> CREATOR = new Creator<TalkFullModel>() {
        public TalkFullModel createFromParcel(Parcel source) {
            return new TalkFullModel(source);
        }

        public TalkFullModel[] newArray(int size) {
            return new TalkFullModel[size];
        }
    };

}
