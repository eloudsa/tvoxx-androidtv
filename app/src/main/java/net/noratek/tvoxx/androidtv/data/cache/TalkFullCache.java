package net.noratek.tvoxx.androidtv.data.cache;

import android.text.TextUtils;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.TalkFullModel;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class TalkFullCache implements DataCache<TalkFullModel, String> {

    public static final long CACHE_LIFE_TIME_MS =
            TimeUnit.MINUTES.toMillis(Constants.CACHE_LIFE_TIME_MINS);

    @Bean
    BaseCache baseCache;


    @Override
    public String upsert(TalkFullModel model) {

        String speakerJSON = serializeData(model);
        baseCache.upsert(speakerJSON, model.getTalkId());
        return speakerJSON;
    }

    @Override
    public TalkFullModel getData(String query) {
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
    public TalkFullModel getData() {
        throw new IllegalStateException("Not needed here!");
    }

    private TalkFullModel deserializeData(String fromCache) {
        if (!TextUtils.isEmpty(fromCache)) {
            return new Gson().fromJson(fromCache, getType());
        } else {
            return null;
        }
    }

    private String serializeData(TalkFullModel data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<TalkFullModel>() {
        }.getType();
    }

}
