package net.noratek.tvoxx.androidtv.data.cache;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.noratek.tvoxx.androidtv.model.TalkFullModel;
import net.noratek.tvoxx.androidtv.utils.AssetsUtil;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by eloudsa on 01/08/16.
 */
@EBean
public class TalksCache implements DataCache<List<TalkFullModel>, String> {

    private static final String TAG = TalksCache.class.getSimpleName();

    private static final String TALKS_CACHE_KEY = "talks_cache_key";

    public static final long CACHE_LIFE_TIME_MS =
            TimeUnit.MINUTES.toMillis(Constants.CACHE_LIFE_TIME_MINS);

    @Bean
    BaseCache baseCache;

    @Bean
    AssetsUtil assetsUtil;


    @Override
    public String upsert(List<TalkFullModel> talksModel) {
        String talkssJSON = serializeData(talksModel);
        baseCache.upsert(serializeData(talksModel), TALKS_CACHE_KEY);
        return talkssJSON;
    }

    @Override
    public List<TalkFullModel> getData() {
        final Optional<String> optionalData = baseCache.getData(TALKS_CACHE_KEY);
        return optionalData.isPresent() ? deserializeData(optionalData.get()) : null;
    }


    public TreeMap<String, List<TalkFullModel>> getDataByTrack() {
        HashMap<String, List<TalkFullModel>> tracks = new HashMap<>();

        List<TalkFullModel> allTalks = getData();
        if (allTalks == null) {
            return null;
        }

        for (TalkFullModel currentTalk : allTalks) {

            // at this stage, we ignore unknown tracks
            if (currentTalk.getTrackTitle() != null) {

                List<TalkFullModel> talks = tracks.get(currentTalk.getTrackTitle().toUpperCase());
                if (talks != null) {
                    talks.add(currentTalk);
                } else {
                    talks = new ArrayList<>();
                    talks.add(currentTalk);

                    tracks.put(currentTalk.getTrackTitle().toUpperCase(), talks);
                }
            }
        }

        return new TreeMap(tracks);
    }

    @Override
    public boolean isValid() {
        return baseCache.isValid(TALKS_CACHE_KEY, CACHE_LIFE_TIME_MS);
    }

    @Override
    public void clearCache(String query) {
        baseCache.clearCache(TALKS_CACHE_KEY);
    }

    @Override
    public void upsert(String rawData, String query) {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public List<TalkFullModel> getData(String query) {
        throw new IllegalStateException("Not needed here!");
    }

    @Override
    public boolean isValid(String query) {
        throw new IllegalStateException("Not needed here!");
    }








    private List<TalkFullModel> deserializeData(String fromCache) {
        return new Gson().fromJson(fromCache, getType());
    }

    private String serializeData(List<TalkFullModel> data) {
        return new Gson().toJson(data);
    }

    private Type getType() {
        return new TypeToken<List<TalkFullModel>>() {
        }.getType();
    }

}
