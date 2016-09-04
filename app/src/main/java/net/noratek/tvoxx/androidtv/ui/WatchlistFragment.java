package net.noratek.tvoxx.androidtv.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.TalkCache;
import net.noratek.tvoxx.androidtv.data.cache.WatchlistCache;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.manager.BackgroundImageManager;
import net.noratek.tvoxx.androidtv.presenter.TalkPresenter;
import net.noratek.tvoxx.androidtv.utils.Constants;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

@EFragment
public class WatchlistFragment extends VerticalGridFragment {
    private static final String TAG = WatchlistFragment.class.getSimpleName();

    private static final int NUM_COLUMNS = 4;

    @Bean
    WatchlistCache watchlistCache;

    @Bean
    TalkCache talkCache;



    private ArrayObjectAdapter mAdapter;

    // Background image
    private BackgroundImageManager mBackgroundImageManager;


    private SpinnerFragment mSpinnerFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        // Prepare the manager that maintains the same background image between activities.
        mBackgroundImageManager = new BackgroundImageManager(getActivity());

        mSpinnerFragment = new SpinnerFragment();

        int width = getResources().getDimensionPixelSize(R.dimen.watchlist_width);
        int height = getResources().getDimensionPixelSize(R.dimen.watchlist_height);

        mAdapter = new ArrayObjectAdapter(new TalkPresenter(width, height));

        setupUIElements();
        setupEventListeners();
        loadRows();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRows();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    private void setupUIElements() {
        setTitle(getString(R.string.watchlist));
        mBackgroundImageManager.updateBackgroundWithDelay(Utils.getUri(getActivity(), R.drawable.default_background));
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
            }
        }

        setAdapter(mAdapter);

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        //setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }


    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {

            String imageUrl = null;

            if (item instanceof Talk) {
                imageUrl = ((Talk) item).getThumbnailUrl();
            }

            Uri backgroundURI;

            if (imageUrl != null) {
                backgroundURI = Uri.parse(imageUrl);
            } else {
                backgroundURI = Utils.getUri(getContext(), R.drawable.default_background);
            }

            mBackgroundImageManager.updateBackgroundWithDelay(backgroundURI);
        }
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

}
