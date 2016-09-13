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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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

        //watchListImageView.setImageDrawable(getContext().getResources().getDrawable(watchList ? R.drawable.ic_watchlist_on : R.drawable.ic_watchlist_off, null));

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


                        // display the duration over the image
                        //showExtraInformation(resource, talk.getDurationInSeconds(), watchList);

                        imageView.setImageBitmap(resource);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        imageView.setImageBitmap(null);
                    }
                });
    }


    private void showExtraInformation(Bitmap resource, int durationInSeconds, boolean watchlist) {

        // format the duration
        String text = Utils.formatVideoDuration(durationInSeconds);

        // prepare the background
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setAlpha(100);

        // prepare the text
        Paint txtPaint = new Paint();
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTextSize(30);  //set text size

        // get the text size
        Rect bounds = new Rect();
        txtPaint.getTextBounds(text, 0, text.length(), bounds);
        int textWidth = bounds.left + bounds.width();

        txtPaint.getTextBounds(text, 0, text.length(), bounds);
        int textHeight = bounds.bottom + bounds.height();

        int margin = 10;

        float width = resource.getWidth();
        float height = resource.getHeight();

        Canvas canvas = new Canvas(resource);

        // Show duration
        canvas.drawRect(width - textWidth - margin, height - textHeight - margin, width, height , rectPaint);
        canvas.drawText(text, width - textWidth - (margin / 2), height - textHeight + (margin  * 2), txtPaint);


        if (watchlist) {
            // show watchlist icon

            Bitmap bitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.ic_watchlist_on, null)).getBitmap();

            canvas.drawBitmap(bitmap, 0, height - bitmap.getHeight(), null);
            bitmap.recycle();
        }
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
