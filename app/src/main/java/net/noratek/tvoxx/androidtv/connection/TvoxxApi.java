package net.noratek.tvoxx.androidtv.connection;


import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.model.Talk;

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
    Call<List<Speaker>> getAllSpeakers();

    @GET("talks.json?withVideo=true")
    Call<List<Talk>> getAllTalks();

    @GET("talks/search?withVideo=true")
    Call<List<Talk>> searchTalks(@Query("q") String query);


    @GET("speakers/{uuid}") Call<Speaker> getSpeaker(@Path("uuid") String uuid);

    @GET("talks/{talkid}") Call<Talk> getTalk(@Path("talkid") String talkId);


}
