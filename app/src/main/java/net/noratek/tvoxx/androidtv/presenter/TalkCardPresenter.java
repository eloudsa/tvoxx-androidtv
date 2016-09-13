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

package net.noratek.tvoxx.androidtv.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.model.RealmString;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.ui.cards.TalkCardView;
import net.noratek.tvoxx.androidtv.utils.Utils;

import java.util.List;


/**
 * The Presenter displays a card consisting of text as a replacement for a big image. The footer is
 * also quite unique since it does contain two images rather than one or non.
 */
public class TalkCardPresenter extends AbstractTalkCardPresenter<TalkCardView> {

    private static final String TAG = TalkCardPresenter.class.getSimpleName();

    private Drawable mDefaultCardImage;

    private List<String> mWatchList;


    public TalkCardPresenter(Context context) {
        super(context);
    }

    public TalkCardPresenter(Context context, List<String> watchList) {
        super(context);
        mWatchList = watchList;
    }


    @Override
    protected TalkCardView onCreateView() {
        mDefaultCardImage = getContext().getResources().getDrawable(R.drawable.conferences, null);
        return new TalkCardView(getContext());
    }



    @Override
    public void onBindViewHolder(final Talk talk, final TalkCardView cardView) {


        // Talk's title
        final TextView primaryText = (TextView) cardView.findViewById(R.id.title_text);
        primaryText.setText(talk.getTitle());


        // List of speakers
        final TextView secondaryText = (TextView) cardView.findViewById(R.id.content_text);
        if (talk.getSpeakerNames() != null) {
            String speakerNames = "";
            for (RealmString speakerName : talk.getSpeakerNames()) {
                speakerNames += speakerNames.length() > 0 ? " \u2022 " + speakerName.value : speakerName.value;
            }
            secondaryText.setText(speakerNames);
        }

        // Rating
        final RatingBar ratingBar = (RatingBar) cardView.findViewById(R.id.ratingBar);
        if (talk.getAverageRating() > 0f) {
            ratingBar.setRating(talk.getAverageRating());
        } else {
            ratingBar.setVisibility(View.GONE);
        }

        // Watchlist
        final ImageView watchlistImage = (ImageView) cardView.findViewById(R.id.watchlist);
        final boolean watchList = mWatchList != null ? mWatchList.contains(talk.getTalkId()) : false;
        watchlistImage.setVisibility(watchList ? View.VISIBLE : View.GONE);

        // Duration
        final TextView textDuration = (TextView) cardView.findViewById(R.id.duration);
        textDuration.setText(Utils.formatVideoDuration(talk.getDurationInSeconds()));

        // Talk's image
        final ImageView imageView = (ImageView) cardView.findViewById(R.id.main_image);

        // Set card size from dimension resources.
        Resources res = cardView.getResources();
        final int width = res.getDimensionPixelSize(R.dimen.talk_card_width);
        final int height = res.getDimensionPixelSize(R.dimen.talk_card_height);

        Uri uri;
        if (talk.getThumbnailUrl() != null) {
            uri = Uri.parse(talk.getThumbnailUrl());
        } else {
            uri = Utils.getUri(getContext(), R.drawable.conferences);
        }

        Glide.with(cardView.getContext())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .error(mDefaultCardImage)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {

                        imageView.setImageBitmap(resource);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        imageView.setImageBitmap(null);
                    }
                });
    }




    @Override
    public void onUnbindViewHolder(TalkCardView cardView) {
        // Free up memory.
        final ImageView imageView = (ImageView) cardView.findViewById(R.id.main_image);
        imageView.setImageBitmap(null);
    }

    public void setWatchList(List<String> mWatchList) {
        this.mWatchList = mWatchList;
    }
}
