package net.noratek.tvoxx.androidtv.connection;

import net.noratek.tvoxx.androidtv.model.SpeakerFullModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.model.TalkFullModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by eloudsa on 31/07/16.
 */
public interface TvoxxApi {

    @GET("speakers.json?withVideo=true")
    Call<List<SpeakerModel>> getAllSpeakers();

    @GET("talks.json?withVideo=true")
    Call<List<TalkFullModel>> getAllTalks();

    @GET("speakers/{uuid}") Call<SpeakerFullModel> getSpeaker(@Path("uuid") String uuid);

    @GET("talks/{talkid}") Call<TalkFullModel> getTalk(@Path("talkid") String talkId);


}
