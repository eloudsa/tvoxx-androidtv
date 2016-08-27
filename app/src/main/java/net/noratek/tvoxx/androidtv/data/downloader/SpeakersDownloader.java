package net.noratek.tvoxx.androidtv.data.downloader;

import android.util.Log;

import net.noratek.tvoxx.androidtv.connection.Connection;
import net.noratek.tvoxx.androidtv.data.RealmProvider;
import net.noratek.tvoxx.androidtv.data.cache.EtagCache;
import net.noratek.tvoxx.androidtv.data.cache.SpeakerCache;
import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.event.SpeakerEvent;
import net.noratek.tvoxx.androidtv.event.SpeakersEvent;
import net.noratek.tvoxx.androidtv.model.Etag;
import net.noratek.tvoxx.androidtv.model.Speaker;
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
public class SpeakersDownloader {
    private static final String TAG = SpeakersDownloader.class.getSimpleName();

    @Bean
    Connection connection;

    @Bean
    RealmProvider realmProvider;

    @Bean
    SpeakerCache speakerCache;

    @Bean
    SpeakersCache speakersCache;

    @Bean
    EtagCache etagCache;



    public void fetchAllSpeakers() throws IOException {

        if (speakersCache.isValid()) {
            EventBus.getDefault().post(new SpeakersEvent());
            return;
        }

        // Retrieve any previous etag
        Etag etag = etagCache.getData(Constants.ETAG_SPEAKERS);

        // retrieve the list of speakers from the server
        Call<List<Speaker>> call = connection.getTvoxxApi().getAllSpeakers(etag != null ? etag.getEtag() : "");

        call.enqueue(new Callback<List<Speaker>>() {
            @Override
            public void onResponse(Call<List<Speaker>> call, Response<List<Speaker>> response) {
                if (response.isSuccessful()) {
                    List<Speaker> speakers = response.body();

                    if (speakers == null) {
                        Log.d(TAG, "No speakers!");
                    } else {
                        // Save the speaker information
                        speakersCache.upsert(speakers);

                        // keep the Etag
                        etagCache.upsert(new Etag(
                                Constants.ETAG_SPEAKERS,
                                response.headers().get("Etag"),
                                call.request().url().toString()));
                    }
                } else {
                    // this condition may be reached if the HTTP code is 304 (Not modified)
                    Log.e(TAG, response.message());
                }

                EventBus.getDefault().post(new SpeakersEvent());
            }

            @Override
            public void onFailure(Call<List<Speaker>> call, Throwable t) {
                Log.e(TAG, "On Failure");
                EventBus.getDefault().post(new SpeakersEvent());
            }
        });
    }



    public void fetchSpeaker(final String uuid) throws IOException {

        if (speakerCache.isValid(uuid)) {
            EventBus.getDefault().post(new SpeakerEvent(uuid));
            return;
        }

        // Retrieve any previous etag
        Etag etag = etagCache.getData(Constants.ETAG_SPEAKER + uuid);

        // retrieve the speaker information from the server
        Call<Speaker> call = connection.getTvoxxApi().getSpeaker(etag != null ? etag.getEtag() : "", uuid);
        call.enqueue(new Callback<Speaker>() {
            @Override
            public void onResponse(Call<Speaker> call, Response<Speaker> response) {

                if (response.isSuccessful()) {
                    Speaker speaker = response.body();
                    if (speaker == null) {
                        Log.d(TAG, "No speakers!");

                    } else {
                        speakerCache.upsert(speaker);

                        // keep the Etag
                        etagCache.upsert(new Etag(
                                Constants.ETAG_SPEAKER + uuid,
                                response.headers().get("Etag"),
                                call.request().url().toString()));
                    }
                } else {
                    // this condition may be reached if the HTTP code is 304 (Not modified)
                    Log.e(TAG, response.message());
                }

                EventBus.getDefault().post(new SpeakerEvent(uuid));
            }

            @Override
            public void onFailure(Call<Speaker> call, Throwable t) {
                Log.e(TAG, "On Failure");
            }
        });

    }


}
