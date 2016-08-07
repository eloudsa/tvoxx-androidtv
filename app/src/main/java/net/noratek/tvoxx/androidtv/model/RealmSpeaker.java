package net.noratek.tvoxx.androidtv.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmSpeaker extends RealmObject {
	@PrimaryKey
	private String uuid;
	private String firstName;
	private String lastName;
	private String lang;
	private String company;
	private String avatarUrl;
	private String bio;
	private RealmList<RealmTalk> talks;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
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

	public RealmList<RealmTalk> getTalks() {
		return talks;
	}

	public void setTalks(RealmList<RealmTalk> talks) {
		this.talks = talks;
	}

	public static class Contract {
		public static final String UUID = "uuid";
	}
}
