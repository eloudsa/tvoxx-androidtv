package net.noratek.tvoxx.androidtv.data.cache;

import android.text.TextUtils;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.Etag;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class EtagCache implements DataCache<Etag, String> {

    @Bean
    BaseCache baseCache;

    @Override
    public String upsert(Etag eTag) {

        String speakerJSON = serializeData(eTag);
        baseCache.upsert(speakerJSON, eTag.getId());
        return speakerJSON;
    }

    @Override
    public Etag getData(String query) {
        final Optional<String> optionalData = baseCache.getData(query);
        return optionalData.isPresent() ? deserializeData(optionalData.get()) : null;
    }

    @Override
    public boolean isValid(String query) {
            throw new IllegalStateException("Not needed here!");
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
    public Etag getData() {
        throw new IllegalStateException("Not needed here!");
    }

    private Etag deserializeData(String fromCache) {
        if (!TextUtils.isEmpty(fromCache)) {
            return new Gson().fromJson(fromCache, getType());
        } else {
            return null;
        }
    }

    private String serializeData(Etag data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<Etag>() {
        }.getType();
    }
}
