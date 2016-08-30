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
import android.content.pm.PackageManager;
import android.net.Uri;

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


    /**
     * Check if a permission is granted
     * @param context
     * @param permission
     * @return true if the permission is granted
     */
    public static boolean hasPermission(final Context context, final String permission) {
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }


    public static String formatVideoDuration(int seconds) {

        String duration;

        int hh = seconds / 3600;
        int mm = (seconds / 60) % 60;
        int ss = seconds % 60;

        if (hh > 0) {
            duration = String.format("%d:%02d:%02d", hh, mm, ss);
        } else {
            duration = String.format("%02d:%02d", mm, ss);
        }

        return duration;

    }

}
