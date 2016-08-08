package net.noratek.tvoxx.androidtv;

import android.app.Application;

import net.noratek.tvoxx.androidtv.data.RealmProvider;
import net.noratek.tvoxx.androidtv.data.Settings_;
import net.noratek.tvoxx.androidtv.data.manager.SpeakerManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by eloudsa on 02/08/16.
 */
@EApplication
public class MainApplication extends Application {

    @Bean
    RealmProvider realmProvider;

    @Bean
    SpeakerManager speakerManager;

    @Pref
    Settings_ settings;

    @Override
    public void onCreate() {
        super.onCreate();
        realmProvider.init();

        /*
        if (settings.isFirstStart().getOr(true)) {
            settings.edit().isFirstStart().put(false).apply();
            speakerManager.initWitStaticData();
        }
        */
    }




}
