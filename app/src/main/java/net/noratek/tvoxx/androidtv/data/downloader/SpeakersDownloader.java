package net.noratek.tvoxx.androidtv.data.downloader;

import android.util.Log;

import net.noratek.tvoxx.androidtv.connection.Connection;
import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.event.SpeakersEvent;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;

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
    SpeakersCache speakersCache;


    public void initWitStaticData() {
        speakersCache.initWithFallbackData();
    }


    public void fetchAllSpeakers() throws IOException {

        if (speakersCache.isValid()) {
            EventBus.getDefault().post(new SpeakersEvent());
            return;
        }

        // retrieve the schedules list from the server
        Call<List<SpeakerModel>> call =connection.getTvoxxApi().getSpeakers();
        call.enqueue(new Callback<List<SpeakerModel>>() {
            @Override
            public void onResponse(Call<List<SpeakerModel>> call, Response<List<SpeakerModel>> response) {
                if (response.isSuccessful()) {
                    List<SpeakerModel> speakersModel = response.body();
                    if (speakersModel == null) {
                        Log.d(TAG, "No speakers!");
                        speakersCache.initWithFallbackData();
                    } else {
                        speakersCache.upsert(speakersModel);
                    }
                } else {
                    Log.e(TAG, response.message());
                    speakersCache.initWithFallbackData();
                }

                EventBus.getDefault().post(new SpeakersEvent());
            }

            @Override
            public void onFailure(Call<List<SpeakerModel>> call, Throwable t) {
                Log.e(TAG, "On Failure");
                speakersCache.initWithFallbackData();
                EventBus.getDefault().post(new SpeakersEvent());
            }
        });
    }

}
