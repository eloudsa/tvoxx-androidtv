package net.noratek.tvoxx.androidtv.connection;

import net.noratek.tvoxx.androidtv.model.SpeakerFullModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.model.TalkFullModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eloudsa on 31/07/16.
 */
public interface TvoxxApi {

    @GET("speakers.json?withVideo=true")
    Call<List<SpeakerModel>> getAllSpeakers();

    @GET("talks.json?withVideo=true")
    Call<List<TalkFullModel>> getAllTalks();

    @GET("talks/search?withVideo=true")
    Call<List<TalkFullModel>> searchTalks(@Query("q") String query);


    @GET("speakers/{uuid}") Call<SpeakerFullModel> getSpeaker(@Path("uuid") String uuid);

    @GET("talks/{talkid}") Call<TalkFullModel> getTalk(@Path("talkid") String talkId);


}
