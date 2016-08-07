package net.noratek.tvoxx.androidtv.model;

import io.realm.RealmObject;

public class RealmTalk extends RealmObject {
	private String talkId;
	private String title;
	private String thumbnailUrl;

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
}
