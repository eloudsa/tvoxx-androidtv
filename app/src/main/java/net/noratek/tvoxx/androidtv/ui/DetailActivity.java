package net.noratek.tvoxx.androidtv.ui;

import android.app.Activity;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_detail)
public class DetailActivity extends Activity {

    public static final String DETAIL_ID = "id";
    public static final String DETAIL_TYPE = "type";
    public static final String DETAIL_SPEAKER = "speaker";
    public static final String DETAIL_TALK = "talk";
    public static final String SHARED_ELEMENT_NAME = "hero";


    public static final String UUID = "UUID";


    public static final String MOVIE = "Movie";
    public static final String NOTIFICATION_ID = "ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
