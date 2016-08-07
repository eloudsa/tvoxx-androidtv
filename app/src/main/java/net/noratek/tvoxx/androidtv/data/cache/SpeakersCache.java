package net.noratek.tvoxx.androidtv.data.cache;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.utils.AssetsUtil;
import net.noratek.tvoxx.androidtv.utils.Configuration;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class SpeakersCache implements DataCache<List<SpeakerModel>, String> {

    private static final String SPEAKERS_CACHE_KEY = "speakers_cache_key";

    public static final long CACHE_LIFE_TIME_MS =
            TimeUnit.MINUTES.toMillis(Configuration.SPEAKERS_CACHE_LIFE_TIME_MINS);

    @Bean
    BaseCache baseCache;

    @Bean
    AssetsUtil assetsUtil;


    @Override
    public String upsert(List<SpeakerModel> speakersModel) {
        String speakersJSON = serializeData(speakersModel);
        baseCache.upsert(serializeData(speakersModel), SPEAKERS_CACHE_KEY);
        return speakersJSON;
    }

    @Override
    public List<SpeakerModel> getData() {
        final Optional<String> optionalCache = baseCache.getData(SPEAKERS_CACHE_KEY);
        return deserializeData(optionalCache.orElse(fallbackData()));
    }

    @Override
    public boolean isValid() {
        return baseCache.isValid(SPEAKERS_CACHE_KEY, CACHE_LIFE_TIME_MS);
    }

    @Override
    public void clearCache(String query) {
        baseCache.clearCache(SPEAKERS_CACHE_KEY);
    }

    public void initWithFallbackData() {
        clearCache(null);
        upsert(deserializeData(fallbackData()));
    }

    @Override
    public void upsert(String rawData, String query) {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public List<SpeakerModel> getData(String query) {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public boolean isValid(String query) {
        throw new IllegalStateException("Not needed here!");
    }

    private String fallbackData() {
        return assetsUtil.loadStringFromAssets(Configuration.SPEAKERS_JSON_DATA_FILE);
    }

    private List<SpeakerModel> deserializeData(String fromCache) {
        return new Gson().fromJson(fromCache, getType());
    }

    private String serializeData(List<SpeakerModel> data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<List<SpeakerModel>>() {
        }.getType();
    }

}
