package net.noratek.tvoxx.androidtv.data;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Settings {

	@DefaultBoolean(true) boolean isFirstStart();
}
