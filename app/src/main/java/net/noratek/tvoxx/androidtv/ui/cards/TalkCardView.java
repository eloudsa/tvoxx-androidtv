/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.noratek.tvoxx.androidtv.ui.cards;

import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import net.noratek.tvoxx.androidtv.R;

public class TalkCardView extends BaseCardView {

    public TalkCardView(Context context) {
        super(context, null, R.style.TalkCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.talk_card, this);
        setFocusable(true);
    }


    public void updateWatchList(boolean watchlist) {
        // Watchlist
        final ImageView watchlistImage = (ImageView) findViewById(R.id.watchlist);
        watchlistImage.setVisibility(watchlist ? View.VISIBLE : View.GONE);
    }

}
