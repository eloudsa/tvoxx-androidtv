package net.noratek.tvoxx.androidtv.ui.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.model.Movie;

import java.net.URI;

/**
 * Created by eloudsa on 31/07/16.
 */
public class CardPresenter extends Presenter {

    private static final String TAG = CardPresenter.class.getSimpleName();

    private static Context mContext;

    static class ViewHolder extends Presenter.ViewHolder {
        private Movie mMovie;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            mDefaultCardImage = ContextCompat.getDrawable(mContext,R.drawable.movie);
        }

        protected void updateCardViewImage(URI uri) {
            /*
            Picasso.with(mContext)
                    .load(uri.toString())
                    .resize(Utils.convertDpToPixel(mContext, CARD_WIDTH),
                            Utils.convertDpToPixel(mContext, CARD_HEIGHT))
                    .error(mDefaultCardImage)
                    .into(mImageCardViewTarget);
                    */


        }


        public void setMovie(Movie m) {
            mMovie = m;
        }

        public Movie getMovie() {
            return mMovie;
        }

        public ImageCardView getCardView() {
            return mCardView;
        }

        public Drawable getDefaultCardImage() {
            return mDefaultCardImage;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");
        mContext = parent.getContext();

        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.fastlane_background));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;
        ((ViewHolder) viewHolder).setMovie(movie);

        Log.d(TAG, "onBindViewHolder");
        ((ViewHolder) viewHolder).mCardView.setTitleText(movie.getTitle());
        ((ViewHolder) viewHolder).mCardView.setContentText(movie.getStudio());
        ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(mContext.getResources().getDimensionPixelSize(R.dimen.card_width), mContext.getResources().getDimensionPixelSize(R.dimen.card_height));
        ((ViewHolder) viewHolder).mCardView.setMainImage(((ViewHolder) viewHolder).getDefaultCardImage());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder viewHolder) {
        // TO DO
    }
}
