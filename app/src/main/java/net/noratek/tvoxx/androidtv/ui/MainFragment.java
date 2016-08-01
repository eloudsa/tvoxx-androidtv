package net.noratek.tvoxx.androidtv.ui;

import android.graphics.Color;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.SpeakersCache;
import net.noratek.tvoxx.androidtv.model.CardModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.ui.presenter.CardPresenter;
import net.noratek.tvoxx.androidtv.ui.presenter.SpeakerPresenter;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public class MainFragment extends BrowseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    @Bean
    SpeakersCache speakersCache;

    private ArrayObjectAdapter mRowsAdapter;

    @AfterViews
    protected void afterViews() {
        setupUIElements();
        loadRows();
    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUIElements();

        loadRows();
    }
    */


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
        HeaderItem speakerHeaderPresenter = new HeaderItem(Constants.HEADER_SPEAKER, getString(R.string.speakers));
        SpeakerPresenter speakerPresenter = new SpeakerPresenter();
        ArrayObjectAdapter speakersRowAdapter = new ArrayObjectAdapter(speakerPresenter);
        mRowsAdapter.add(new ListRow(speakerHeaderPresenter, speakersRowAdapter));
        downloadSpeakers(speakerHeaderPresenter, speakersRowAdapter);

        setAdapter(mRowsAdapter);
    }


    private void downloadSpeakers(final HeaderItem speakerHeaderPresenter, final ArrayObjectAdapter speakersRowAdapter) {


        List<SpeakerModel> speakersModel = speakersCache.getData();
        if (speakersModel == null) {
            return;
        }

        // load list of Devoxx speakers
        for (SpeakerModel speakerModel : speakersModel) {
            CardModel cardModel = new CardModel();
            cardModel.setCardImageUrl(speakerModel.getAvatarUrl());
            cardModel.setTitle(speakerModel.getFirstName() + " " + speakerModel.getLastName());
            cardModel.setContent(speakerModel.getCompany());
            speakersRowAdapter.add(cardModel);
        }

        mRowsAdapter.replace(Constants.HEADER_SPEAKER, new ListRow(speakerHeaderPresenter, speakersRowAdapter));


/*
        TvoxxApi methods = Utils.getRestClient(getActivity(), Constants.TVOXX_API_URL, TvoxxApi.class);
        if (methods == null) {
            return;
        }

        // retrieve the schedules list from the server
        Call<List<SpeakerModel>> call = methods.getSpeakers();
        call.enqueue(new Callback<List<SpeakerModel>>() {
            @Override
            public void onResponse(Call<List<SpeakerModel>> call, Response<List<SpeakerModel>> response) {
                if (response.isSuccessful()) {
                    List<SpeakerModel> speakersModel = response.body();

                    if (speakersModel == null) {
                        Log.d(TAG, "No speakers!");
                        return;
                    }

                    // load list of Devoxx speakers
                    for (SpeakerModel speakerModel : speakersModel) {
                        CardModel cardModel = new CardModel();
                        cardModel.setCardImageUrl(speakerModel.getAvatarUrl());
                        cardModel.setTitle(speakerModel.getFirstName() + " " + speakerModel.getLastName());
                        cardModel.setContent(speakerModel.getCompany());
                        speakersRowAdapter.add(cardModel);
                    }

                    mRowsAdapter.replace(Constants.HEADER_SPEAKER, new ListRow(speakerHeaderPresenter, speakersRowAdapter));

                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SpeakerModel>> call, Throwable t) {
                Log.e(TAG, "On Failure");
            }
        });

        */

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
}