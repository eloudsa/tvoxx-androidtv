package net.noratek.tvoxx.androidtv.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmFullTalk extends RealmObject {
	@PrimaryKey
	private String talkId;
	private String talkType;
	private String trackTitle;
	private String title;
	private String conferenceLabel;
	private String summary;
	private String lang;
	private String youtubeVideoId;
	private String thumbnailUrl;
	private float averageRating;
	private int numberOfRatings;
	private int durationInSeconds;

	private RealmList<RealmSpeaker> speakers;


	public RealmFullTalk() {
	}

	public RealmFullTalk(String talkId, String talkType, String trackTitle, String title, String conferenceLabel, String summary, String lang, String youtubeVideoId, String thumbnailUrl, float averageRating, int numberOfRatings, int durationInSeconds, RealmList<RealmSpeaker> speakers) {
		this.talkId = talkId;
		this.talkType = talkType;
		this.trackTitle = trackTitle;
		this.title = title;
		this.conferenceLabel = conferenceLabel;
		this.summary = summary;
		this.lang = lang;
		this.youtubeVideoId = youtubeVideoId;
		this.thumbnailUrl = thumbnailUrl;
		this.averageRating = averageRating;
		this.numberOfRatings = numberOfRatings;
		this.durationInSeconds = durationInSeconds;
		this.speakers = speakers;
	}

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

	public RealmList<RealmSpeaker> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(RealmList<RealmSpeaker> speakers) {
		this.speakers = speakers;
	}}
