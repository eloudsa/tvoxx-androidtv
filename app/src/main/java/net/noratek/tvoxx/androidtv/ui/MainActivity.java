package net.noratek.tvoxx.androidtv.ui;

import android.app.Activity;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
