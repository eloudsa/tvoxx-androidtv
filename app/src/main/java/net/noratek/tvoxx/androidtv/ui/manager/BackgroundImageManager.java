package net.noratek.tvoxx.androidtv.ui.manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by eloudsa on 03/08/16.
 */
public class BackgroundImageManager {
    private static final String TAG = BackgroundImageManager.class.getSimpleName();

    private Activity mActivity;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private Drawable mDefaultBackground;
    private Uri mBackgroundURI;
    private Timer mBackgroundTimer;
    private final Handler mHandler = new Handler();


    public BackgroundImageManager(Activity activity) {

        mActivity = activity;

        mBackgroundManager = BackgroundManager.getInstance(mActivity);
        mBackgroundManager.attach(mActivity.getWindow());
        mDefaultBackground = mActivity.getResources().getDrawable(R.drawable.default_background, null);
        mMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

    }


    public void updateBackgroundWithDelay(Uri uri) {
        mBackgroundURI = uri;
        startBackgroundTimer();
    }


    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), Constants.BACKGROUND_UPDATE_DELAY);
    }


    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mBackgroundURI != null) {
                        updateBackground(mBackgroundURI.toString());
                    } else {
                        mBackgroundManager.setDrawable(mDefaultBackground);
                    }
                }
            });
        }
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(mActivity)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        mBackgroundManager.setBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        mBackgroundManager.setDrawable(mDefaultBackground);
                    }
                });
        mBackgroundTimer.cancel();
    }


}
