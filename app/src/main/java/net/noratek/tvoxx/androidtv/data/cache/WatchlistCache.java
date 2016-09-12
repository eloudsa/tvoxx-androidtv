package net.noratek.tvoxx.androidtv.data.cache;

import android.util.Log;

import net.noratek.tvoxx.androidtv.data.Settings_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eloudsa on 12/09/16.
 */

@EBean
public class WatchlistCache {

    private static final String TAG = WatchlistCache.class.getSimpleName();

    @Pref
    Settings_ settings;



    public List<String> getData() {

        // Retrieve the existing watchlist
        List<String> watchList = null;
        try {
            watchList = jsonToArray(new JSONArray(settings.watchList().get()));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return watchList;
    }


    public void remove(String talkId) {

        List<String> watchList = getData();
        if (watchList == null) {
            return;
        }

        // search and remove the talk
        int index = 0;
        for (String currTalkId : watchList) {
            if (currTalkId.equalsIgnoreCase(talkId)) {
                watchList.remove(index);
                break;
            }
            index++;
        }

        // update the list

        settings.watchList().put(new JSONArray(watchList).toString());
    }


    public void add(String talkId) {

        List<String> watchList = getData();
        if (watchList == null) {
            watchList = new ArrayList<String>();
        }

        // avoid duplicates
        if (watchList.contains(talkId) == false) {
            watchList.add(talkId);
        }

        // update the list
        settings.watchList().put(new JSONArray(watchList).toString());
    }


    public boolean isExist(String talkId) {
        List<String> watchList = getData();
        if (watchList == null) {
            return false;
        }

        // search and remove the talk
        for (String currTalkId : watchList) {
            if (currTalkId.equalsIgnoreCase(talkId)) {
                return true;
            }
        }

        return false;
    }


    private List<String> jsonToArray(JSONArray jsonArray) {

        if (jsonArray == null) {
            return null;
        }

        List<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){

            try {
                list.add(jsonArray.get(i).toString());
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

        }

        return list;
    }


}
