package net.noratek.tvoxx.androidtv.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.utils.Utils;


/**
 * Created by eloudsa on 31/07/16.
 */
public class SpeakerPresenter extends Presenter {

    private static final String TAG = SpeakerPresenter.class.getSimpleName();

    private static Context mContext;

    private int mSelectedBackgroundColor = -1;
    private int mDefaultBackgroundColor = -1;
    private Drawable mDefaultCardImage;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();

        mDefaultBackgroundColor = ContextCompat.getColor(mContext, R.color.default_background);
        mSelectedBackgroundColor = ContextCompat.getColor(mContext, R.color.default_background);
        mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.ic_anonymous, null);


        ImageCardView cardView = new ImageCardView(mContext) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };


        cardView.setCardType(BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA);
        cardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }


    private void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Speaker speaker = (Speaker) item;

        final ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setTitleText(speaker.getFirstName() + " " + speaker.getLastName());
        cardView.setContentText(speaker.getCompany());


        Uri uri;
        if (speaker.getAvatarUrl() != null) {
            uri = Uri.parse(speaker.getAvatarUrl());
        } else {
            uri = Utils.getUri(mContext, R.drawable.ic_anonymous);
        }

        // Set card size from dimension resources.
        Resources res = cardView.getResources();
        final int width = res.getDimensionPixelSize(R.dimen.speaker_card_width);
        final int height = res.getDimensionPixelSize(R.dimen.speaker_card_height);
        cardView.setMainImageDimensions(width, height);

        Glide.with(cardView.getContext())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .error(mDefaultCardImage)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        cardView.setMainImage(new BitmapDrawable(mContext.getResources(), resource));
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        cardView.setMainImage(null);
                        //cardView.setMainImageDimensions(width, height);

                    }
                });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        // Free up memory.
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        // TO DO
    }
}
