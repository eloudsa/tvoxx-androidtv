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
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.model.RealmString;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.utils.Utils;

import java.util.List;

public class TalkPresenter extends Presenter {

    private static final String TAG = TalkPresenter.class.getSimpleName();

    private static Context mContext;

    private int mSelectedBackgroundColor = -1;
    private int mDefaultBackgroundColor = -1;
    private Drawable mDefaultCardImage;

    private int mWidth = -1;
    private int mHeight = -1;

    private List<String> mWatchList;


/*


    public TalkPresenter() {
        mWidth = context.getDimensionPixelSize(R.dimen.talk_card_width);
        mHeight = context.getDimensionPixelSize(R.dimen.talk_card_height);
    }

    public TalkPresenter(int width, int height) {
        this.width = width;
        this.height = height;
    }
    */

    public TalkPresenter(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public TalkPresenter() {
    }

    public TalkPresenter(final List<String> watchList) {
        mWatchList = watchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();

        mDefaultBackgroundColor = ContextCompat.getColor(mContext, R.color.default_background);
        mSelectedBackgroundColor = ContextCompat.getColor(mContext, R.color.default_background);
        mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.conferences, null);


        ImageCardView cardView = new ImageCardView(mContext) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };


        customizeCardView(cardView);

        return new ViewHolder(cardView);
    }


    private void customizeCardView(ImageCardView cardView) {

        cardView.setCardType(BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA);
        cardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);

        // set a marquee on the title
        TextView titleView = (TextView) cardView.findViewById(R.id.title_text);
        titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleView.setMarqueeRepeatLimit(-1);
        titleView.setSingleLine();

        // set a marquee on speakers
        TextView contentView = (TextView) cardView.findViewById(R.id.content_text);
        contentView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        contentView.setMarqueeRepeatLimit(-1);
        contentView.setSingleLine();
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
        final Talk talk = (Talk) item;

        final ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setTitleText(talk.getTitle());

        if (talk.getSpeakerNames() != null) {
            String speakerNames = "";
            for (RealmString speakerName : talk.getSpeakerNames()) {
                speakerNames += speakerNames.length() > 0 ? " \u2022 " + speakerName.value : speakerName.value;
            }
            cardView.setContentText(speakerNames);
        }

        Uri uri;
        if (talk.getThumbnailUrl() != null) {
            uri = Uri.parse(talk.getThumbnailUrl());
        } else {
            uri = Utils.getUri(mContext, R.drawable.conferences);
        }

        // Set card size from dimension resources.
        Resources res = cardView.getResources();
        final int width = mWidth != -1 ? mWidth : res.getDimensionPixelSize(R.dimen.talk_card_width);
        final int height = mHeight != -1 ? mHeight : res.getDimensionPixelSize(R.dimen.talk_card_height);
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


                        // display the duration over the image
                        showDuration(resource, cardView, talk.getDurationInSeconds());

                        cardView.setMainImage(new BitmapDrawable(mContext.getResources(), resource));

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        cardView.setMainImage(null);
                    }
                });
    }


    private void showDuration(Bitmap resource, ImageCardView cardView, int durationInSeconds) {

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
        int width = bounds.left + bounds.width();

        txtPaint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();

        int margin = 10;

        float top = cardView.getTop();
        float right = cardView.getRight();

        Canvas canvas = new Canvas(resource);

        // draw the duration over the image
        canvas.drawRect(right - width - margin, top, right, top + height + margin, rectPaint);
        canvas.drawText(text, right - width - (margin / 2), top + height + (margin / 2), txtPaint);

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
