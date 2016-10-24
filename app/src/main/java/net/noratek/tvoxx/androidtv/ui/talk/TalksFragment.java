package net.noratek.tvoxx.androidtv.ui.talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.view.View;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.TalksCache;
import net.noratek.tvoxx.androidtv.data.cache.WatchlistCache;
import net.noratek.tvoxx.androidtv.data.manager.TalkManager;
import net.noratek.tvoxx.androidtv.event.ErrorEvent;
import net.noratek.tvoxx.androidtv.event.TalksEvent;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.presenter.IconHeaderItemPresenter;
import net.noratek.tvoxx.androidtv.presenter.TalkCardPresenter;
import net.noratek.tvoxx.androidtv.ui.cards.TalkCardView;
import net.noratek.tvoxx.androidtv.ui.search.SearchActivity_;
import net.noratek.tvoxx.androidtv.ui.util.SpinnerFragment;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@EFragment
public class TalksFragment extends BrowseFragment {
    private static final String TAG = TalksFragment.class.getSimpleName();

    @Bean
    TalksCache talksCache;

    @Bean
    TalkManager talkManager;

    @Bean
    WatchlistCache watchlistCache;


    private SpinnerFragment mSpinnerFragment;

    TreeMap<String, List<Talk>> mTracks;

    TalkCardPresenter mTalkCardPresenter;

    // selected talk
    TalkCardView mSelectedCardView;
    String mSelectedTalkId;

    // Background image
    BackgroundManager mBackgroundManager;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        mSpinnerFragment = new SpinnerFragment();

        setupUIElements();
        setupEventListeners();

        try {
            // Display the spinner
            getFragmentManager().beginTransaction().add(R.id.talks_fragment, mSpinnerFragment).commit();

            talkManager.fetchAllTalks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // Retrieve the existing watchlist
        List<String> watchList = watchlistCache.getData();

        if (mTalkCardPresenter != null) {
            mTalkCardPresenter.setWatchList(watchList);
        }

        if ((mSelectedCardView != null) && (watchList != null)) {
            mSelectedCardView.updateWatchList(watchList.contains(mSelectedTalkId));
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mBackgroundManager.release();
        super.onDestroy();
    }


    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tvoxx_logo));
        setTitle(getString(R.string.app_title)); // Badge, when set, takes precedent

        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));

        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));

        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });

        // change background image
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_background));

        prepareEntranceTransition();
    }


    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());

        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity_.class);
                startActivity(intent);
            }
        });
    }

    private void loadRows() {

        if (mTracks == null) {
            return;
        }

        // Retrieve the existing watchlist
        List<String> watchList = watchlistCache.getData();

        HeaderItem trackHeader;
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        mTalkCardPresenter = new TalkCardPresenter(getActivity(), watchList);

        Long headerId = 0L;

        for(Map.Entry<String,List<Talk>> track : mTracks.entrySet()) {

            List<Talk> talks = track.getValue();
            if ((talks != null) && (talks.size() > 0)) {
                // get the track title with its proper case
                String trackTitle = talks.get(0).getTrackTitle();

                trackHeader = new HeaderItem(headerId++, trackTitle);

                ArrayObjectAdapter trackRowAdapter = new ArrayObjectAdapter(mTalkCardPresenter);

                for (Talk talk : talks) {

                    if (watchList != null) {
                        talk.setWatchlist(watchList.contains(talk.getTalkId()));
                    }

                    trackRowAdapter.add(talk);
                }

                rowsAdapter.add(new ListRow(trackHeader, trackRowAdapter));
            }
        }

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();


        setAdapter(rowsAdapter);

        startEntranceTransition();
    }



    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Talk) {
                Talk talk = (Talk) item;

                mSelectedCardView = (TalkCardView) itemViewHolder.view;
                mSelectedTalkId = talk.getTalkId();

                Intent intent = new Intent(getActivity(), TalkDetailActivity_.class);
                intent.putExtra(Constants.TALK_ID, talk.getTalkId());
                getActivity().startActivity(intent);
            }
        }
    }


    //
    // Events
    //

    @Subscribe
    public void onMessageEvent(TalksEvent talksEvent) {

        mTracks = talksCache.getDataByTrack();
        if (mTracks == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            getActivity().finish();
            return;
        }

        loadRows();
    }

    @Subscribe
    public void onMessageEvent(ErrorEvent errorEvent) {
        // unable to retrieve talks

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
        ((TalksActivity) getActivity()).displayErrorMessage(errorEvent.getErrorMessage(), true);
    }

}