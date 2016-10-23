package net.noratek.tvoxx.androidtv.ui.watchlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.TalkCache;
import net.noratek.tvoxx.androidtv.data.cache.WatchlistCache;
import net.noratek.tvoxx.androidtv.data.manager.TalkManager;
import net.noratek.tvoxx.androidtv.event.TalkEvent;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.presenter.TalkCardPresenter;
import net.noratek.tvoxx.androidtv.ui.talk.TalkDetailActivity_;
import net.noratek.tvoxx.androidtv.ui.util.SpinnerFragment;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

@EFragment
public class WatchlistFragment extends VerticalGridFragment {
    private static final String TAG = WatchlistFragment.class.getSimpleName();

    private static final int NUM_COLUMNS = 4;

    @Bean
    WatchlistCache watchlistCache;

    @Bean
    TalkCache talkCache;

    @Bean
    TalkManager talkManager;


    TalkCardPresenter mTalkPresenter;

    private ArrayObjectAdapter mAdapter;

    // Background image
    BackgroundManager mBackgroundManager;


    private SpinnerFragment mSpinnerFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        EventBus.getDefault().register(this);

        mSpinnerFragment = new SpinnerFragment();

        mTalkPresenter = new TalkCardPresenter(getActivity(), watchlistCache.getData());

        mAdapter = new ArrayObjectAdapter(mTalkPresenter);

        setupUIElements();
        setupEventListeners();
        loadRows();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mTalkPresenter != null) {
            mTalkPresenter.setWatchList(watchlistCache.getData());
        }

        // change background image
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.footer_lodyas));

        loadRows();
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        mBackgroundManager.release();
        super.onStop();
    }


    private void setupUIElements() {
        setTitle(getString(R.string.watchlist));

        // prepare the background image
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
    }


    private void loadRows() {

        mAdapter.clear();

        // Display the spinner
        getFragmentManager().beginTransaction().add(R.id.watchlist_fragment, mSpinnerFragment).commit();

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

        List<String> watchlist = watchlistCache.getData();
        if (watchlist == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }

        // display the list of talks
        for (String talkId : watchlist) {

            Talk talk = talkCache.getData(talkId);
            if (talk != null) {
                mAdapter.add(talk);
            } else {
                try {
                    talkManager.fetchTalk(talkId);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        setAdapter(mAdapter);

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }



    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Talk) {
                Talk talk = (Talk) item;

                Intent intent = new Intent(getActivity(), TalkDetailActivity_.class);
                intent.putExtra(Constants.TALK_ID, talk.getTalkId());
                getActivity().startActivity(intent);
            }
        }
    }


    @Subscribe
    public void onMessageEvent(TalkEvent talkEvent) {

        Log.d(TAG, "Into onMessageEvent->TalkFullEvent: " + talkEvent.getTalkId());

        Talk talk = talkCache.getData(talkEvent.getTalkId());
        if (talk != null) {
            for (int i = 0; i < mAdapter.size(); i++) {

                Talk currentTalk = (Talk) mAdapter.get(i);

                if (currentTalk.getTalkId().equalsIgnoreCase(talk.getTalkId())) {
                    return;
                }
            }

            mAdapter.add(talk);
        }
    }

}
