package net.noratek.tvoxx.androidtv.ui.talk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.ui.util.ErrorFragment;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_talk_detail)
public class TalkDetailActivity extends Activity {

    private static final String TAG = TalkDetailActivity.class.getSimpleName();


    public static final String TALK_ID = "UUID";
    public static final String SHARED_ELEMENT_NAME = "hero";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }


    public void displayErrorMessage(int errorId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ERROR_RESOURCE_ID, errorId);

        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.talk_detail_fragment, errorFragment).addToBackStack(null).commit();
        //getFragmentManager().beginTransaction().add(R.id.talk_detail_fragment, errorFragment).commit();
    }




}
