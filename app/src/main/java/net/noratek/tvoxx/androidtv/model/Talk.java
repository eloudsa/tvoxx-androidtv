package net.noratek.tvoxx.androidtv.model;


import net.noratek.tvoxx.androidtv.model.converter.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.TalkRealmProxy;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = { TalkRealmProxy.class },
		value = Parcel.Serialization.FIELD,
		analyze = { Talk.class })
public class Talk extends RealmObject  {
	@PrimaryKey
	public String talkId;
	public String talkType;
	public String trackId;
	public String trackTitle;
	public String title;
	public String conferenceEventCode;
	public String conferenceLabel;
	public String summary;
	public String lang;
	public String youtubeVideoId;
	public String thumbnailUrl;
	public float averageRating;
	public int numberOfRatings;
	public int durationInSeconds;
	@Ignore
	private boolean watchlist;

	@ParcelPropertyConverter(RealmListParcelConverter.class)
	public RealmList<RealmString> speakerNames;

	@ParcelPropertyConverter(RealmListParcelConverter.class)
	public RealmList<Speaker> speakers;



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

	public RealmList<Speaker> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(RealmList<Speaker> speakers) {
		this.speakers = speakers;
	}

	public RealmList<RealmString> getSpeakerNames() {

		if ((speakerNames == null) && ((speakers) != null) && (speakers.size() > 0)) {
			// Retrieve the speakers name from the list of speakers

			RealmList<RealmString> speakersList = new RealmList<>();

			for (Speaker speaker : speakers) {
				RealmString fullName = new RealmString();
				fullName.value = speaker.getFirstName() + " " + speaker.getLastName();
				speakersList.add(fullName);
			}

			return speakersList;
		}

		return speakerNames;
	}

	public void setSpeakerNames(RealmList<RealmString> speakerNames) {
		this.speakerNames = speakerNames;
	}

	public boolean isWatchlist() {
		return watchlist;
	}

	public void setWatchlist(boolean watchlist) {
		this.watchlist = watchlist;
	}
}
