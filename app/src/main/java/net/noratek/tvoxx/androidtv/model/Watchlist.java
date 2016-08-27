package net.noratek.tvoxx.androidtv.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Watchlist extends RealmObject implements Parcelable {
	@PrimaryKey
	private String talkId;

	public Watchlist() {
	}

	public Watchlist(String talkId) {
		this.talkId = talkId;
	}

	public String getTalkId() {
		return talkId;
	}

	public void setTalkId(String talkId) {
		this.talkId = talkId;
	}

	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.talkId);
	}

	protected Watchlist(Parcel in) {
		this.talkId = in.readString();
	}

	public static final Creator<Watchlist> CREATOR = new Creator<Watchlist>() {
		public Watchlist createFromParcel(Parcel source) {
			return new Watchlist(source);
		}

		public Watchlist[] newArray(int size) {
			return new Watchlist[size];
		}
	};
}
