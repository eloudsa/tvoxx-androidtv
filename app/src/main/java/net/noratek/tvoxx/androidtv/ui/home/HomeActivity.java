package net.noratek.tvoxx.androidtv.ui.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;

@EActivity
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            Fragment fragment = new HomeFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
