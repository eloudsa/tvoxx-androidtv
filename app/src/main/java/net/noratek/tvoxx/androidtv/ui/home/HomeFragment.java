package net.noratek.tvoxx.androidtv.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.manager.BackgroundImageManager;
import net.noratek.tvoxx.androidtv.model.Card;
import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.presenter.CardPresenter;
import net.noratek.tvoxx.androidtv.ui.speaker.SpeakersActivity_;
import net.noratek.tvoxx.androidtv.ui.talk.TalksActivity_;
import net.noratek.tvoxx.androidtv.ui.watchlist.WatchlistActivity_;
import net.noratek.tvoxx.androidtv.utils.Constants;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.EFragment;

@EFragment
public class HomeFragment extends BrowseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private ArrayObjectAdapter mAdapter;

    // Background image
    private BackgroundImageManager mBackgroundImageManager;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare the manager that maintains the same background image between activities.
        mBackgroundImageManager = new BackgroundImageManager(getActivity());


        setupUIElements();
        setupRowAdapter();
        setupEventListeners();
    }

    private void setupUIElements() {
        setTitle(getString(R.string.app_title));
        setBadgeDrawable(getResources().getDrawable(R.drawable.tvoxx_logo, null));
        setHeadersState(HEADERS_DISABLED);
        setHeadersTransitionOnBackEnabled(false);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
    }


    private void setupRowAdapter() {
        mAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mAdapter.add(createRows());
        setAdapter(mAdapter);
    }


    private ListRow createRows() {

        CardPresenter cardPresenter = new CardPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);

        Card card = new Card();
        card.setCardImageUrl(Utils.getUri(getActivity(), R.drawable.conferences).toString());
        card.setTitle(getString(R.string.talks));
        card.setType(Constants.CARD_TYPE_TALKS);
        listRowAdapter.add(card);

        card = new Card();
        card.setCardImageUrl(Utils.getUri(getActivity(), R.drawable.speakers).toString());
        card.setTitle(getString(R.string.speakers));
        card.setType(Constants.CARD_TYPE_SPEAKERS);
        listRowAdapter.add(card);

        card = new Card();
        card.setCardImageUrl(Utils.getUri(getActivity(), R.drawable.favorite).toString());
        card.setTitle(getString(R.string.watchlist));
        card.setType(Constants.CARD_TYPE_WATCHLIST);
        listRowAdapter.add(card);

        card = new Card();
        card.setCardImageUrl(Utils.getUri(getActivity(), R.drawable.about).toString());
        card.setTitle(getString(R.string.about));
        card.setType(Constants.CARD_TYPE_ABOUT);
        listRowAdapter.add(card);

        return new ListRow(listRowAdapter);
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


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Card) {

                Intent intent = null;

                switch (((Card) item).getType()) {

                    case Constants.CARD_TYPE_SPEAKERS:
                        intent = new Intent(getActivity(), SpeakersActivity_.class);
                        break;

                    case Constants.CARD_TYPE_TALKS:
                        intent = new Intent(getActivity(), TalksActivity_.class);
                        break;

                    case Constants.CARD_TYPE_WATCHLIST:
                        intent = new Intent(getActivity(), WatchlistActivity_.class);
                        break;
                }

                if (intent == null) {
                    return;
                }

                getActivity().startActivity(intent);
            }
        }
    }

}
