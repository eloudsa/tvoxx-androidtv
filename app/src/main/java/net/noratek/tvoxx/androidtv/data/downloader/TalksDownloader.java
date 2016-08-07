package net.noratek.tvoxx.androidtv.data.downloader;

import android.util.Log;

import net.noratek.tvoxx.androidtv.connection.Connection;
import net.noratek.tvoxx.androidtv.data.RealmProvider;
import net.noratek.tvoxx.androidtv.data.cache.TalkFullCache;
import net.noratek.tvoxx.androidtv.event.SpeakerFullEvent;
import net.noratek.tvoxx.androidtv.event.TalkFullEvent;
import net.noratek.tvoxx.androidtv.model.RealmFullTalk;
import net.noratek.tvoxx.androidtv.model.TalkFullModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class TalksDownloader {
    private static final String TAG = TalksDownloader.class.getSimpleName();

    @Bean
    Connection connection;

    @Bean
    RealmProvider realmProvider;

    @Bean
    TalkFullCache talkFullCache;



    public void fetchTalk(final String talkId) throws IOException {

        if (talkFullCache.isValid(talkId)) {
            EventBus.getDefault().post(new SpeakerFullEvent(talkId));
            return;
        }

        // retrieve the talk information from the server
        Call<TalkFullModel> call =connection.getTvoxxApi().getTalkFull(talkId);
        call.enqueue(new Callback<TalkFullModel>() {
            @Override
            public void onResponse(Call<TalkFullModel> call, Response<TalkFullModel> response) {

                if (response.isSuccessful()) {
                    TalkFullModel talkFullModel = response.body();
                    if (talkFullModel == null) {
                        Log.d(TAG, "No talk!");
                    } else {
                        String talkJSON = talkFullCache.upsert(talkFullModel);

                        Realm realm = realmProvider.getRealm();
                        realm.beginTransaction();
                        realm.createOrUpdateObjectFromJson(RealmFullTalk.class, talkJSON);
                        realm.commitTransaction();
                        realm.close();

                        EventBus.getDefault().post(new TalkFullEvent(talkFullModel.getTalkId()));
                    }
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<TalkFullModel> call, Throwable t) {
                Log.e(TAG, "On Failure");
            }
        });

    }


}
