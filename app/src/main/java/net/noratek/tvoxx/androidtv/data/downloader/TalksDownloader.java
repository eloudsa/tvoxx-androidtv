package net.noratek.tvoxx.androidtv.data.downloader;

import android.util.Log;

import net.noratek.tvoxx.androidtv.connection.Connection;
import net.noratek.tvoxx.androidtv.data.RealmProvider;
import net.noratek.tvoxx.androidtv.data.cache.EtagCache;
import net.noratek.tvoxx.androidtv.data.cache.TalkCache;
import net.noratek.tvoxx.androidtv.data.cache.TalksCache;
import net.noratek.tvoxx.androidtv.event.TalkEvent;
import net.noratek.tvoxx.androidtv.event.TalksEvent;
import net.noratek.tvoxx.androidtv.model.Etag;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

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
    TalkCache talkCache;

    @Bean
    TalksCache talksCache;

    @Bean
    EtagCache etagCache;


    public void fetAllTalks() throws IOException {

        if (talksCache.isValid()) {
            EventBus.getDefault().post(new TalksEvent());
            return;
        }


        // Retrieve any previous etag
        Etag etag = etagCache.getData(Constants.ETAG_TALKS);

        // retrieve the list of talks from the server
        Call<List<Talk>> call = connection.getTvoxxApi().getAllTalks(etag != null ? etag.getEtag() : "");
        call.enqueue(new Callback<List<Talk>>() {
            @Override
            public void onResponse(Call<List<Talk>> call, Response<List<Talk>> response) {
                if (response.isSuccessful()) {
                    List<Talk> talks = response.body();
                    if (talks == null) {
                        Log.d(TAG, "No talks!");
                    } else {
                        talksCache.upsert(talks);

                        // keep the Etag
                        etagCache.upsert(new Etag(
                                Constants.ETAG_TALKS,
                                response.headers().get("Etag"),
                                call.request().url().toString()));
                    }
                } else {
                    // this condition may be reached if the HTTP code is 304 (Not modified)
                    Log.e(TAG, response.message());
                }

                EventBus.getDefault().post(new TalksEvent());
            }

            @Override
            public void onFailure(Call<List<Talk>> call, Throwable t) {
                Log.e(TAG, "On Failure");
                EventBus.getDefault().post(new TalksEvent());
            }
        });
    }



    public void fetchTalk(final String talkId) throws IOException {

        if (talkCache.isValid(talkId)) {
            EventBus.getDefault().post(new TalkEvent(talkId));
            return;
        }

        // Retrieve any previous etag
        Etag etag = etagCache.getData(Constants.ETAG_TALK + talkId);

        // retrieve the talk information from the server
        Call<Talk> call = connection.getTvoxxApi().getTalk(etag != null ? etag.getEtag() : "", talkId);
        call.enqueue(new Callback<Talk>() {
            @Override
            public void onResponse(Call<Talk> call, Response<Talk> response) {

                if (response.isSuccessful()) {
                    Talk talk = response.body();
                    if (talk == null) {
                        Log.d(TAG, "No talk!");
                    } else {
                        talkCache.upsert(talk);

                        // keep the Etag
                        etagCache.upsert(new Etag(
                                Constants.ETAG_TALK + talkId,
                                response.headers().get("Etag"),
                                call.request().url().toString()));
                    }
                } else {
                    // this condition may be reached if the HTTP code is 304 (Not modified)
                    Log.e(TAG, response.message());
                }

                EventBus.getDefault().post(new TalkEvent(talkId));
            }

            @Override
            public void onFailure(Call<Talk> call, Throwable t) {
                Log.e(TAG, "On Failure");
            }
        });

    }


}
