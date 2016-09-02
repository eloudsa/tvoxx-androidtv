package net.noratek.tvoxx.androidtv.model;

import net.noratek.tvoxx.androidtv.model.converter.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.SpeakerRealmProxy;
import io.realm.annotations.PrimaryKey;


@Parcel(implementations = { SpeakerRealmProxy.class },
		value = Parcel.Serialization.FIELD,
		analyze = { Speaker.class })
public class Speaker extends RealmObject {
	@PrimaryKey
	public String uuid;
	public String firstName;
	public String lastName;
	public String lang;
	public String company;
	public String avatarUrl;
	public String bio;

	@ParcelPropertyConverter(RealmListParcelConverter.class)
	public RealmList<Talk> talks;


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

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public RealmList<Talk> getTalks() {
		return talks;
	}

	public void setTalks(RealmList<Talk> talks) {
		this.talks = talks;
	}
}
