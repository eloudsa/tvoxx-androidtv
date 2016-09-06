package net.noratek.tvoxx.androidtv.ui.speaker;

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
import android.view.View;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.data.manager.SpeakerManager;
import net.noratek.tvoxx.androidtv.event.ErrorEvent;
import net.noratek.tvoxx.androidtv.event.SpeakersEvent;
import net.noratek.tvoxx.androidtv.manager.BackgroundImageManager;
import net.noratek.tvoxx.androidtv.model.Card;
import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.presenter.SpeakerPresenter;
import net.noratek.tvoxx.androidtv.ui.util.SpinnerFragment;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

@EFragment
public class SpeakersFragment extends VerticalGridFragment {
    private static final String TAG = SpeakersFragment.class.getSimpleName();

    private static final int NUM_COLUMNS = 4;

    @Bean
    SpeakersCache speakersCache;

    @Bean
    SpeakerManager speakerManager;


    private ArrayObjectAdapter mAdapter;

    // Background image
    private BackgroundImageManager mBackgroundImageManager;


    private SpinnerFragment mSpinnerFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        // Prepare the manager that maintains the same background image between activities.
        mBackgroundImageManager = new BackgroundImageManager(getActivity());

        mSpinnerFragment = new SpinnerFragment();

        setupUIElements();
        setupEventListeners();
        loadRows();
    }

    @Override
    public void onStop() {
        mBackgroundImageManager.cancel();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void setupUIElements() {
        setTitle(getString(R.string.speakers));
    }


    private void loadRows() {

        // Display the spinner
        getFragmentManager().beginTransaction().add(R.id.speakers_fragment, mSpinnerFragment).commit();

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

        mAdapter = new ArrayObjectAdapter(new SpeakerPresenter());

        try {
            speakerManager.fetchAllSpeakers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setAdapter(mAdapter);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());

        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Subscribe
    public void onMessageEvent(SpeakersEvent speakersEvent) {

        List<Speaker> speakers = speakersCache.getData();
        if (speakers == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }

        mAdapter.clear();

        // display speakers
        for (Speaker speaker : speakers) {
            mAdapter.add(speaker);
        }

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {

            mBackgroundImageManager.cancel();

            String imageUrl = null;

            if (item instanceof Card) {
                imageUrl = ((Card) item).getCardImageUrl();

            } else if (item instanceof Speaker) {
                imageUrl = ((Speaker) item).getAvatarUrl();
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


    // Events

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Speaker) {

                Speaker speaker = (Speaker) item;

                mBackgroundImageManager.cancel();
                Intent intent = new Intent(getActivity(), SpeakerDetailActivity_.class);
                intent.putExtra(SpeakerDetailActivity.UUID, speaker.getUuid());
                getActivity().startActivity(intent);
            }
        }
    }


    @Subscribe
    public void onMessageEvent(ErrorEvent errorEvent) {

        // unable to retrieve the detail of a speaker

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
        ((SpeakersActivity) getActivity()).displayErrorMessage(errorEvent.getErrorMessage(), true);
    }
}
