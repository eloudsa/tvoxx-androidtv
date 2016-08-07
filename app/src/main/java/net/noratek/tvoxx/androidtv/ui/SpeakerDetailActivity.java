package net.noratek.tvoxx.androidtv.ui;

import android.app.Activity;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_speaker_detail)
public class SpeakerDetailActivity extends Activity {

    public static final String UUID = "UUID";
    public static final String SHARED_ELEMENT_NAME = "hero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
