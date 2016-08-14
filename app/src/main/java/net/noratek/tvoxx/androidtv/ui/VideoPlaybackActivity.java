package net.noratek.tvoxx.androidtv.ui;

import android.app.Activity;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_video_playback)
public class VideoPlaybackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*

    private static final String TAG = VideoPlaybackActivity.class.getSimpleName();

    @Bean
    private TalkFullCache mTalkFullCache;


    @ViewById(R.id.videoView)
    private VideoView mVideoView;


    private PlaybackController mPlaybackController;

    private TalkFullModel mSelectedTalk;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        String talkId = getIntent().getStringExtra(Constants.TALK_ID);

        mSelectedTalk = mTalkFullCache.getData(talkId);
        if (mSelectedTalk == null) {
            finish();
        }
    }


    @AfterInject
    private void afterInject() {

        mPlaybackController = new PlaybackController(this);

        mPlaybackController.setVideoView(mVideoView);
        mPlaybackController.setMovie(mSelectedTalk); // it must after video view setting
        loadViews();
    }

    private void loadViews() {
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);

        mPlaybackController.setVideoPath(Constants.YOUTUBE_URL_PREFIX + mSelectedTalk.getYoutubeVideoId());
    }


    public PlaybackController getPlaybackController() {
        return mPlaybackController;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlaybackController.finishPlayback();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (!requestVisibleBehind(true)) {
            // Try to play behind launcher, but if it fails, stop playback.
            mPlaybackController.playPause(false);
        }
    }


    public TalkFullModel getSelectedTalk() {
        return mSelectedTalk;
    }

    public void setmSelectedTalk(TalkFullModel mSelectedTalk) {
        this.mSelectedTalk = mSelectedTalk;
    }
    */
}
