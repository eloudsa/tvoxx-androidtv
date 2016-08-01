package net.noratek.tvoxx.androidtv.data.downloader;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;

import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class SpeakersDownloader {

    @Bean
    SpeakersCache speakersCache;


    public void downloadSpeakers(final HeaderItem speakerHeaderPresenter, final ArrayObjectAdapter speakersRowAdapter) {


        List<SpeakerModel> speakersModel = speakersCache.getData();
        if (speakersModel != null) {



        }

        /*

        TvoxxApi methods = Utils.getRestClient(getActivity(), Constants.TVOXX_API_URL, TvoxxApi.class);
        if (methods == null) {
            return;
        }

        // retrieve the schedules list from the server
        Call<List<SpeakerModel>> call = methods.getSpeakers();
        call.enqueue(new Callback<List<SpeakerModel>>() {
            @Override
            public void onResponse(Call<List<SpeakerModel>> call, Response<List<SpeakerModel>> response) {
                if (response.isSuccessful()) {
                    List<SpeakerModel> speakersModel = response.body();

                    if (speakersModel == null) {
                        Log.d(TAG, "No speakers!");
                        return;
                    }

                    // load list of Devoxx speakers
                    for (SpeakerModel speakerModel : speakersModel) {
                        CardModel cardModel = new CardModel();
                        cardModel.setCardImageUrl(speakerModel.getAvatarUrl());
                        cardModel.setTitle(speakerModel.getFirstName() + " " + speakerModel.getLastName());
                        cardModel.setContent(speakerModel.getCompany());
                        speakersRowAdapter.add(cardModel);
                    }

                    mRowsAdapter.replace(Constants.HEADER_SPEAKER, new ListRow(speakerHeaderPresenter, speakersRowAdapter));

                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SpeakerModel>> call, Throwable t) {
                Log.e(TAG, "On Failure");
            }
        });
        */

    }


}
