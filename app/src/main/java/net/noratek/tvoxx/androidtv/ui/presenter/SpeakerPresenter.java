package net.noratek.tvoxx.androidtv.ui.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.model.Card;


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

        mDefaultBackgroundColor = ContextCompat.getColor(mContext,R.color.default_background);
        mSelectedBackgroundColor = ContextCompat.getColor(mContext, R.color.selected_background);
        mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.ic_anonymous, null);


        ImageCardView cardView = new ImageCardView(mContext) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

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
        Card card = (Card) item;

        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setTitleText(card.getTitle());
        cardView.setContentText(card.getContent());

        if (card.getCardImageUrl() != null) {
            // Set card size from dimension resources.
            Resources res = cardView.getResources();
            int width = res.getDimensionPixelSize(R.dimen.card_width);
            int height = res.getDimensionPixelSize(R.dimen.card_height);
            cardView.setMainImageDimensions(width, height);

            Glide.with(cardView.getContext())
                    .load(card.getCardImageUrl())
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }

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
