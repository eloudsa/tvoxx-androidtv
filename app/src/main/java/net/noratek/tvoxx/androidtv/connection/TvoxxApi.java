package net.noratek.tvoxx.androidtv.connection;

import net.noratek.tvoxx.androidtv.model.SpeakerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by eloudsa on 31/07/16.
 */
public interface TvoxxApi {

    @GET("speakers")
    Call<List<SpeakerModel>> getSpeakers();

}
