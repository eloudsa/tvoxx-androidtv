package net.noratek.tvoxx.androidtv.data.cache;

import android.text.TextUtils;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.SpeakerFullModel;
import net.noratek.tvoxx.androidtv.utils.AssetsUtil;
import net.noratek.tvoxx.androidtv.utils.Configuration;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class SpeakerFullCache implements DataCache<SpeakerFullModel, String> {

    private static final String SPEAKER_FULL_CACHE_KEY = "speaker_full_cache_key";

    public static final long CACHE_LIFE_TIME_MS =
            TimeUnit.MINUTES.toMillis(Configuration.SPEAKERS_CACHE_LIFE_TIME_MINS);

    @Bean
    BaseCache baseCache;

    @Bean
    AssetsUtil assetsUtil;


    @Override
    public String upsert(SpeakerFullModel model) {

        String speakerJSON = serializeData(model);
        baseCache.upsert(speakerJSON, model.getUuid());
        return speakerJSON;
    }

    @Override
    public SpeakerFullModel getData(String query) {
        final Optional<String> optionalData = baseCache.getData(query);
        return optionalData.isPresent() ? deserializeData(optionalData.get()) : null;
    }

    @Override
    public boolean isValid(String query) {
        return baseCache.isValid(query, CACHE_LIFE_TIME_MS);
    }

    @Override
    public void clearCache(String query) {
        baseCache.clearCache(query);
    }

    @Override
    public void upsert(String rawData, String query) {
        baseCache.upsert(rawData, query);
    }

    @Override
    public boolean isValid() {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public SpeakerFullModel getData() {
        throw new IllegalStateException("Not needed here!");
    }

    private SpeakerFullModel deserializeData(String fromCache) {
        if (!TextUtils.isEmpty(fromCache)) {
            return new Gson().fromJson(fromCache, getType());
        } else {
            return null;
        }
    }

    private String serializeData(SpeakerFullModel data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<SpeakerFullModel>() {
        }.getType();
    }

}
