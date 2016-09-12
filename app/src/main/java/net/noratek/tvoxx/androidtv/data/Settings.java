package net.noratek.tvoxx.androidtv.data;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Settings {

	@DefaultString("")
	String watchList();
}
