package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Speaker extends RealmObject implements Parcelable {
	@PrimaryKey
	private String uuid;
	private String firstName;
	private String lastName;
	private String lang;
	private String company;
	private String avatarUrl;
	private String bio;
	private RealmList<Talk> talks;


	public Speaker() {
	}

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

	public RealmList<Talk> getTalks() {
		return talks;
	}

	public void setTalks(RealmList<Talk> talks) {
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

	protected Speaker(Parcel in) {
		this.uuid = in.readString();
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.lang = in.readString();
		this.company = in.readString();
		this.avatarUrl= in.readString();
		this.bio= in.readString();
		in.readTypedList(talks, Talk.CREATOR);
	}

	public static final Parcelable.Creator<Speaker> CREATOR = new Parcelable.Creator<Speaker>() {
		public Speaker createFromParcel(Parcel source) {
			return new Speaker(source);
		}

		public Speaker[] newArray(int size) {
			return new Speaker[size];
		}
	};
}
