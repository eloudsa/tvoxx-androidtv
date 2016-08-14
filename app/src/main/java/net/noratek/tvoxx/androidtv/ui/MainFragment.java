package net.noratek.tvoxx.androidtv.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.data.cache.TalksCache;
import net.noratek.tvoxx.androidtv.data.manager.SpeakerManager;
import net.noratek.tvoxx.androidtv.data.manager.TalkManager;
import net.noratek.tvoxx.androidtv.event.SpeakersEvent;
import net.noratek.tvoxx.androidtv.event.TalksEvent;
import net.noratek.tvoxx.androidtv.model.CardModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.model.TalkFullModel;
import net.noratek.tvoxx.androidtv.ui.presenter.CardPresenter;
import net.noratek.tvoxx.androidtv.ui.presenter.SpeakerPresenter;
import net.noratek.tvoxx.androidtv.ui.presenter.TalkPresenter;
import net.noratek.tvoxx.androidtv.utils.Constants;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@EFragment
public class MainFragment extends BrowseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    @Bean
    SpeakersCache speakersCache;

    @Bean
    SpeakerManager speakerManager;

    @Bean
    TalksCache talksCache;

    @Bean
    TalkManager talkManager;



    private ArrayObjectAdapter mRowsAdapter;

    // Headers
    HeaderItem mSpeakerHeaderPresenter;

    // Background image
   // private BackgroundImageManager mBackgroundImageManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        // Prepare the manager that maintains the same background image between activities.
        //mBackgroundImageManager = new BackgroundImageManager(getActivity());

        setupUIElements();

        setupEventListeners();

        try {
            talkManager.fetchAllTalks();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        //mBackgroundImageManager.cancel();
        super.onStop();
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

        /*
        // change the background image
        BackgroundManager backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        backgroundManager.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
        */
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

            if (item instanceof CardModel) {
                imageUrl = ((CardModel) item).getCardImageUrl();

            } else if (item instanceof SpeakerModel) {
                imageUrl = ((SpeakerModel) item).getAvatarUrl();

            }  else if (item instanceof TalkFullModel) {
                imageUrl = ((TalkFullModel) item).getThumbnailUrl();
            }

            Uri backgroundURI;

            if (imageUrl != null) {
                backgroundURI = Uri.parse(imageUrl);
            } else {
                backgroundURI = Utils.getUri(getContext(), R.drawable.default_background);
            }

            //mBackgroundImageManager.updateBackgroundWithDelay(backgroundURI);
        }
    }



    private void loadRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        /* GridItemPresenter */
        HeaderItem gridItemPresenterHeader = new HeaderItem(0, "GridItemPresenter");

        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add("ITEM 1");
        gridRowAdapter.add("ITEM 2");
        gridRowAdapter.add("ITEM 3");
        mRowsAdapter.add(new ListRow(gridItemPresenterHeader, gridRowAdapter));

        /* CardPresenter */
        HeaderItem cardPresenterHeader = new HeaderItem(1, "CardPresenter");
        CardPresenter cardPresenter = new CardPresenter();
        ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(cardPresenter);

        for(int i=0; i<10; i++) {
            CardModel cardModel = new CardModel();
            cardModel.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg");
            cardModel.setTitle("title" + i);
            cardModel.setContent("studio" + i);
            cardRowAdapter.add(cardModel);
        }
        mRowsAdapter.add(new ListRow(cardPresenterHeader, cardRowAdapter));

        /* Speakers */
        mSpeakerHeaderPresenter = new HeaderItem(Constants.HEADER_SPEAKER, getString(R.string.speakers));
        SpeakerPresenter speakerPresenter = new SpeakerPresenter();
        ArrayObjectAdapter speakersRowAdapter = new ArrayObjectAdapter(speakerPresenter);
        mRowsAdapter.add(new ListRow(mSpeakerHeaderPresenter, speakersRowAdapter));

        try {
            speakerManager.fetchAllSpeakers();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            talkManager.fetchAllTalks();
        } catch (IOException e) {
            e.printStackTrace();
        }


        setAdapter(mRowsAdapter);
    }


    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {

            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.grid_item_width), getResources().getDimensionPixelSize(R.dimen.grid_item_height)));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {

        }
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof TalkFullModel) {
                TalkFullModel talk = (TalkFullModel) item;

                Intent intent = new Intent(getActivity(), TalkDetailActivity_.class);
                intent.putExtra(Constants.TALK_ID, talk.getTalkId());
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

        SpeakerPresenter speakerPresenter = new SpeakerPresenter();
        ArrayObjectAdapter speakersRowAdapter = new ArrayObjectAdapter(speakerPresenter);

        // display speakers
        for (SpeakerModel speaker : speakersModel) {
            speakersRowAdapter.add(speaker);
        }

        mRowsAdapter.replace(Constants.HEADER_SPEAKER, new ListRow(mSpeakerHeaderPresenter, speakersRowAdapter));
    }


    @Subscribe
    public void onMessageEvent(TalksEvent talksEvent) {

        TreeMap<String, List<TalkFullModel>> tracks = talksCache.getDataByTrack();
        if (tracks == null) {
            getActivity().finish();
            return;
        }

        HeaderItem trackHeaderPresenter;
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        TalkPresenter talkPresenter = new TalkPresenter();


        Long headerId = 0L;

        for(Map.Entry<String,List<TalkFullModel>> track : tracks.entrySet()) {

            //String trackTitle = track.getKey();
            List<TalkFullModel> talks = track.getValue();

            if ((talks != null) && (talks.size() > 0)) {
                String trackTitle = talks.get(0).getTrackTitle();

                trackHeaderPresenter = new HeaderItem(headerId++, trackTitle);

                ArrayObjectAdapter trackRowAdapter = new ArrayObjectAdapter(talkPresenter);

                for (TalkFullModel talk : talks) {
                    trackRowAdapter.add(talk);
                }

                rowsAdapter.add(new ListRow(trackHeaderPresenter, trackRowAdapter));
            }
        }


        setAdapter(rowsAdapter);

        /*
        SpeakerPresenter speakerPresenter = new SpeakerPresenter();
        ArrayObjectAdapter speakersRowAdapter = new ArrayObjectAdapter(speakerPresenter);

        // display speakers
        for (SpeakerModel speaker : speakersModel) {
            speakersRowAdapter.add(speaker);
        }

        mRowsAdapter.replace(Constants.HEADER_SPEAKER, new ListRow(mSpeakerHeaderPresenter, speakersRowAdapter));
        */
    }


}