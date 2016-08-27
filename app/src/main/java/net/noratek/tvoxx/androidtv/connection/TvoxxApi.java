package net.noratek.tvoxx.androidtv.connection;


import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.model.Talk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eloudsa on 31/07/16.
 */
public interface TvoxxApi {

    @GET("speakers.json?withVideo=true")
    Call<List<Speaker>> getAllSpeakers(@Header("If-None-Match") String eTag);

    @GET("talks.json?withVideo=true")
    Call<List<Talk>> getAllTalks(@Header("If-None-Match") String eTag);

    @GET("talks/search?withVideo=true")
    Call<List<Talk>> searchTalks(@Query("q") String query);


    @GET("speakers/{uuid}") Call<Speaker> getSpeaker(@Header("If-None-Match") String eTag, @Path("uuid") String uuid);

    @GET("talks/{talkid}") Call<Talk> getTalk(@Header("If-None-Match") String eTag, @Path("talkid") String talkId);


}
