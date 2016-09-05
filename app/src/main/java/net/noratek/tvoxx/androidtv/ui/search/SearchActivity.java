package net.noratek.tvoxx.androidtv.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_search)
public class SearchActivity extends Activity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity_.class));
        return true;
    }

}
