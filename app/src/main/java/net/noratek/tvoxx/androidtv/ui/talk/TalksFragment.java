package net.noratek.tvoxx.androidtv.ui.talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.view.View;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.TalksCache;
import net.noratek.tvoxx.androidtv.data.manager.TalkManager;
import net.noratek.tvoxx.androidtv.event.TalksEvent;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.presenter.TalkPresenter;
import net.noratek.tvoxx.androidtv.ui.search.SearchActivity_;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

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


    //
    // Events
    //

    @Subscribe
    public void onMessageEvent(TalksEvent talksEvent) {

        TreeMap<String, List<Talk>> tracks = talksCache.getDataByTrack();
        if (tracks == null) {
            getActivity().finish();
            return;
        }

        HeaderItem trackHeaderPresenter;
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        TalkPresenter talkPresenter = new TalkPresenter();

        Long headerId = 0L;

        for(Map.Entry<String,List<Talk>> track : tracks.entrySet()) {

            List<Talk> talks = track.getValue();
            if ((talks != null) && (talks.size() > 0)) {
                // get the track title with its proper case
                String trackTitle = talks.get(0).getTrackTitle();

                trackHeaderPresenter = new HeaderItem(headerId++, trackTitle);

                ArrayObjectAdapter trackRowAdapter = new ArrayObjectAdapter(talkPresenter);

                for (Talk talk : talks) {
                    trackRowAdapter.add(talk);
                }

                rowsAdapter.add(new ListRow(trackHeaderPresenter, trackRowAdapter));
            }
        }


        setAdapter(rowsAdapter);
    }


}