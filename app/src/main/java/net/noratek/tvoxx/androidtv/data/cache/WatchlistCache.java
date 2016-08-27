package net.noratek.tvoxx.androidtv.data.cache;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class WatchlistCache implements DataCache<List<String>, String> {

    public static final long CACHE_LIFE_TIME_MS =
            TimeUnit.MINUTES.toMillis(Constants.CACHE_LIFE_TIME_MINS);

    @Bean
    BaseCache baseCache;

    public String upsert(String talkId) {
        List<String> watchlist = getData(Constants.WATCHLIST_KEY);
        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }

        if (!watchlist.contains(talkId)) {
            watchlist.add(talkId);
        }

        String favoriteJSON = serializeData(watchlist);
        baseCache.upsert(favoriteJSON, Constants.WATCHLIST_KEY);
        return favoriteJSON;
    }


    @Override
    public String upsert(List<String> watchlist) {
        String speakersJSON = serializeData(watchlist);
        baseCache.upsert(serializeData(watchlist), Constants.WATCHLIST_KEY);
        return speakersJSON;
    }


    @Override
    public List<String> getData() {
        final Optional<String> optionalCache = baseCache.getData(Constants.WATCHLIST_KEY);
        return deserializeData(optionalCache.orElse(null));
    }


    @Override
    public List<String> getData(String query) {
        final Optional<String> optionalData = baseCache.getData(query);
        return optionalData.isPresent() ? deserializeData(optionalData.get()) : null;
    }


    public Boolean isExist(String talkId) {
        List<String> watchlist = getData(Constants.WATCHLIST_KEY);
        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }

        return watchlist.contains(talkId);
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


    private List<String> deserializeData(String fromCache) {
        return new Gson().fromJson(fromCache, getType());
    }

    private String serializeData(List<String> data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<List<String>>() {
        }.getType();
    }
}
