package net.noratek.tvoxx.androidtv.ui.speaker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.ui.util.ErrorFragment;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_speakers)
public class SpeakersActivity extends Activity {

    private boolean mFinishOnDismiss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void displayErrorMessage(String errorMessage, boolean finishOnDismiss) {

        mFinishOnDismiss = finishOnDismiss;

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ERROR_RESOURCE_MESSAGE, errorMessage);
        bundle.putBoolean(Constants.ERROR_FINISH_ON_DISMISS, mFinishOnDismiss);

        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.speakers_fragment, errorFragment, Constants.ERROR_FRAGMENT_ID).addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentByTag(Constants.ERROR_FRAGMENT_ID);
        if ((fragment != null) && (fragment.isVisible()) && mFinishOnDismiss) {

            finish();
        } else {
            super.onBackPressed();
        }
    }
}
