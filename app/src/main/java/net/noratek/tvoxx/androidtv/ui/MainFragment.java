package net.noratek.tvoxx.androidtv.ui;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v4.content.ContextCompat;

import net.noratek.tvoxx.androidtv.R;


public class MainFragment extends BrowseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUIElements();
    }


    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tvoxx_logo));
        setTitle(getString(R.string.app_title)); // Badge, when set, takes precedent

        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));

        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));

    }
}