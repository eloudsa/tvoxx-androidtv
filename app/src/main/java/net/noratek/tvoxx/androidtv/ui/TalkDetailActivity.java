package net.noratek.tvoxx.androidtv.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import net.noratek.tvoxx.androidtv.R;

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
}
