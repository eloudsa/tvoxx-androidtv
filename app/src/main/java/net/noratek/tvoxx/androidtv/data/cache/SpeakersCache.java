package net.noratek.tvoxx.androidtv.data.cache;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.utils.AssetsUtil;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class SpeakersCache implements DataCache<List<Speaker>, String> {

    public static final long CACHE_LIFE_TIME_MS =
            TimeUnit.MINUTES.toMillis(Constants.CACHE_LIFE_TIME_MINS);

    @Bean
    BaseCache baseCache;

    @Bean
    AssetsUtil assetsUtil;


    @Override
    public String upsert(List<Speaker> speakers) {
        String speakersJSON = serializeData(speakers);
        baseCache.upsert(serializeData(speakers), Constants.SPEAKERS_KEY);
        return speakersJSON;
    }

    @Override
    public List<Speaker> getData() {
        final Optional<String> optionalCache = baseCache.getData(Constants.SPEAKERS_KEY);
        return deserializeData(optionalCache.orElse(null));
    }

    @Override
    public boolean isValid() {
        return baseCache.isValid(Constants.SPEAKERS_KEY, CACHE_LIFE_TIME_MS);
    }

    @Override
    public void clearCache(String query) {
        baseCache.clearCache(Constants.SPEAKERS_KEY);
    }

    @Override
    public void upsert(String rawData, String query) {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public List<Speaker> getData(String query) {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public boolean isValid(String query) {
        throw new IllegalStateException("Not needed here!");
    }


    private List<Speaker> deserializeData(String fromCache) {
        return new Gson().fromJson(fromCache, getType());
    }

    private String serializeData(List<Speaker> data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<List<Speaker>>() {
        }.getType();
    }

}
