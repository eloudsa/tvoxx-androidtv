package net.noratek.tvoxx.androidtv.data.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.utils.AssetsUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class SpeakersCache implements DataCache<List<SpeakerModel>, String> {

    @Bean
    AssetsUtil assetsUtil;


    @Override
    public void upsert(String rawData, String query) {

    }

    @Override
    public void upsert(List<SpeakerModel> rawData) {

    }

    @Override
    public List<SpeakerModel> getData() {
        return deserializeData(fallbackData());
    }

    @Override
    public List<SpeakerModel> getData(String query) {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isValid(String query) {
        return false;
    }

    @Override
    public void clearCache(String query) {

    }

    private List<SpeakerModel> deserializeData(String fromCache) {
        return new Gson().fromJson(fromCache, getType());
    }

    private Type getType() {
        return new TypeToken<List<SpeakerModel>>() {
        }.getType();
    }

    private String fallbackData() {
        return assetsUtil.loadStringFromAssets("data/speakers.json");
    }

}
