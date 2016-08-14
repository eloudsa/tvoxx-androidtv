package net.noratek.tvoxx.androidtv.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;


@EActivity
public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = YouTubeActivity.class.getSimpleName();

    private YouTubePlayerView mYouTubeView;



    private static final int RECOVERY_REQUEST = 1;
    private String mYouTubeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_youtube);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            mYouTubeKey = bundle.getString("youtube_key");

            mYouTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            mYouTubeView.initialize(mYouTubeKey, this);

        } catch (Exception e) {
            Log.e(TAG, "Dear developer. Don't forget to configure <meta-data android:name=\"my_test_metagadata\" android:value=\"testValue\"/> in your AndroidManifest.xml file.");
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Player error", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(mYouTubeKey, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return mYouTubeView;
    }


}
