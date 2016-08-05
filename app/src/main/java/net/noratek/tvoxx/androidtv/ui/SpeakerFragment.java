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
import android.support.v4.content.ContextCompat;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.data.manager.SpeakerManager;
import net.noratek.tvoxx.androidtv.event.SpeakersEvent;
import net.noratek.tvoxx.androidtv.model.CardModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.ui.manager.BackgroundImageManager;
import net.noratek.tvoxx.androidtv.ui.presenter.SpeakerPresenter;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

@EFragment
public class SpeakerFragment extends VerticalGridFragment {
    private static final String TAG = SpeakerFragment.class.getSimpleName();

    private static final int NUM_COLUMNS = 4;

    @Bean
    SpeakersCache speakersCache;

    @Bean
    SpeakerManager speakerManager;


    private ArrayObjectAdapter mAdapter;

    // Background image
    private BackgroundImageManager mBackgroundImageManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        // Prepare the manager that maintains the same background image between activities.
        mBackgroundImageManager = new BackgroundImageManager(getActivity());

        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tvoxx_logo));
        setTitle(getString(R.string.app_title));

        setupUIElements();
        setupEventListeners();
        loadRows();
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tvoxx_logo));
        setTitle(getString(R.string.app_title));
    }


    private void loadRows() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

        mAdapter = new ArrayObjectAdapter(new SpeakerPresenter());

        try {
            speakerManager.fetchSpeakersASync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setAdapter(mAdapter);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }


    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {

            String imageUrl = null;

            if (item instanceof CardModel) {
                imageUrl = ((CardModel) item).getCardImageUrl();

            } else if (item instanceof SpeakerModel) {
                imageUrl = ((SpeakerModel) item).getAvatarUrl();
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

            if (item instanceof SpeakerModel) {
                Intent intent = new Intent(getActivity(), MainActivity_.class);
                getActivity().startActivity(intent);
            }
        }
    }


    // Events

    @Subscribe
    public void onMessageEvent(SpeakersEvent speakersEvent) {

        List<SpeakerModel> speakersModel = speakersCache.getData();
        if (speakersModel == null) {
            return;
        }

        mAdapter.clear();

        // display speakers
        for (SpeakerModel speaker : speakersModel) {
            mAdapter.add(speaker);
        }
    }
}
