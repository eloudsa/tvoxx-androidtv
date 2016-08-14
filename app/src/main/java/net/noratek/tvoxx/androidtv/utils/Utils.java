/*
 * Copyright (c) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.noratek.tvoxx.androidtv.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import java.util.HashMap;

/**
 * A collection of utility methods, all static.
 */
public class Utils {

    /*
     * Making sure public utility methods remain static
     */
    private Utils() {
    }


    /**
     * Get an URI from a drawable
     */
    public static Uri getUri(Context context, int resId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(resId)
                + '/' + context.getResources().getResourceTypeName(resId)
                + '/' + context.getResources().getResourceEntryName(resId) );
    }




    public static long getDuration(String videoUrl) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mmr.setDataSource(videoUrl, new HashMap<String, String>());
        } else {
            mmr.setDataSource(videoUrl);
        }
        return Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }


    public static String getYouTubeUrl(String youtubeId) {
        return Constants.YOUTUBE_URL_PREFIX + youtubeId;

    }


}
