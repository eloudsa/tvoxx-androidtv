package net.noratek.tvoxx.androidtv.ui.talk;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewLogoPresenter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.youtube.player.YouTubeIntents;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.TalkCache;
import net.noratek.tvoxx.androidtv.data.cache.WatchlistCache;
import net.noratek.tvoxx.androidtv.data.manager.TalkManager;
import net.noratek.tvoxx.androidtv.event.ErrorEvent;
import net.noratek.tvoxx.androidtv.event.TalkEvent;
import net.noratek.tvoxx.androidtv.manager.BackgroundImageManager;
import net.noratek.tvoxx.androidtv.model.Card;
import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.presenter.CardPresenter;
import net.noratek.tvoxx.androidtv.presenter.DetailDescriptionPresenter;
import net.noratek.tvoxx.androidtv.ui.speaker.SpeakerDetailActivity;
import net.noratek.tvoxx.androidtv.ui.speaker.SpeakerDetailActivity_;
import net.noratek.tvoxx.androidtv.ui.util.SpinnerFragment;
import net.noratek.tvoxx.androidtv.utils.Constants;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


@EFragment
public class TalkDetailFragment extends DetailsFragment {

    private static final String TAG = TalkDetailFragment.class.getSimpleName();


    @Bean
    TalkCache mTalkCache;

    @Bean
    TalkManager mTalkManager;

    @Bean
    WatchlistCache watchlistCache;

    OnItemViewClickedListener onItemViewClickedListener;


    private SpinnerFragment mSpinnerFragment;

    private FullWidthDetailsOverviewSharedElementHelper mHelper;
    private ClassPresenterSelector mPresenterSelector;
    private ArrayObjectAdapter mAdapter;

    private String mTalkId;
    private Talk mSelectedTalk;

    // Background image
    private BackgroundImageManager mBackgroundImageManager;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        onItemViewClickedListener = new ItemViewClickedListener();

        mSpinnerFragment = new SpinnerFragment();

        mTalkId = getActivity().getIntent().getStringExtra(TalkDetailActivity.TALK_ID);
        if (mTalkId != null) {
            loadDetail();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void loadDetail() {

        // Display the spinner
        getFragmentManager().beginTransaction().add(R.id.talk_detail_fragment, mSpinnerFragment).commit();

        try {
            mTalkManager.fetchTalk(mTalkId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // When a Related item is clicked.
        setOnItemViewClickedListener(onItemViewClickedListener);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mBackgroundImageManager = null;
        super.onDestroy();
    }


    @Override
    public void onStop() {
        if (mBackgroundImageManager != null) {
            mBackgroundImageManager.cancel();
        }
        super.onStop();
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Card) {
                Card card = (Card) item;

                Intent intent = new Intent(getActivity(), SpeakerDetailActivity_.class);
                intent.putExtra(SpeakerDetailActivity.UUID, card.getId());
                getActivity().startActivity(intent);
            }
        }
    }



    private void setupAdapter() {

        prepareEntranceTransition();


        // Prepare the manager that maintains the same background image between activities.
        if (mBackgroundImageManager != null) {
            mBackgroundImageManager.cancel();
            mBackgroundImageManager = null;
        }
        mBackgroundImageManager = new BackgroundImageManager(getActivity());

        // Set detail background and style.
        final FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailDescriptionPresenter(getActivity()),
                        new DetailsOverviewPresenter());

        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.talk_detail_background));
        detailsPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_HALF);

        // Hook up transition element.
        mHelper = new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(getActivity(),
                SpeakerDetailActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(mHelper);
        detailsPresenter.setParticipatingEntranceTransition(false);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {

                if (action.getId() == Constants.TALK_DETAIL_ACTION_PLAY_VIDEO) {
                    // Check if the Android TV device has a the YouTube capabilities
                    if (YouTubeIntents.canResolvePlayVideoIntent(getActivity())) {
                        // open the video in fullscreen in YouTube native application
                        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(getActivity(), mSelectedTalk.getYoutubeVideoId(), true, true);
                        startActivity(intent);

                    } else {
                        // unable to view the application
                        ((TalkDetailActivity) getActivity()).displayErrorMessage(getString(R.string.error_youtube_player_failed), false);
                    }

                } else if (action.getId() == Constants.TALK_DETAIL_ACTION_ADD_WATCHLIST) {


                    Boolean isWatchlist = watchlistCache.isExist(mTalkId);

                    if (isWatchlist) {
                        // remove from the watchlist
                        watchlistCache.remove(mTalkId);


                    } else {
                        // add to the watchlist
                        watchlistCache.add(mTalkId);

                    }

                    updateWatchlistAction(action);

                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());

                } else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPresenterSelector = new ClassPresenterSelector();
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mAdapter = new ArrayObjectAdapter(mPresenterSelector);
        setAdapter(mAdapter);
    }



    static class DetailsOverviewPresenter extends DetailsOverviewLogoPresenter {

        static class ViewHolder extends DetailsOverviewLogoPresenter.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }

            public FullWidthDetailsOverviewRowPresenter getParentPresenter() {
                return mParentPresenter;
            }

            public FullWidthDetailsOverviewRowPresenter.ViewHolder getParentViewHolder() {
                return mParentViewHolder;
            }
        }

        @Override
        public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
            ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lb_fullwidth_details_overview_logo, parent, false);

            Resources res = parent.getResources();
            int width = res.getDimensionPixelSize(R.dimen.detail_thumb_width);
            int height = res.getDimensionPixelSize(R.dimen.detail_thumb_heigth);
            imageView.setLayoutParams(new ViewGroup.MarginLayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            return new ViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
            DetailsOverviewRow row = (DetailsOverviewRow) item;
            ImageView imageView = ((ImageView) viewHolder.view);
            imageView.setImageDrawable(row.getImageDrawable());
            if (isBoundToImage((ViewHolder) viewHolder, row)) {
                DetailsOverviewPresenter.ViewHolder vh =
                        (DetailsOverviewPresenter.ViewHolder) viewHolder;
                vh.getParentPresenter().notifyOnBindLogo(vh.getParentViewHolder());
            }
        }
    }


    private void setupDetailsOverviewRow(Talk talk) {

        // change the background image
        mBackgroundImageManager.updateBackgroundWithDelay(talk.getThumbnailUrl());

        final DetailsOverviewRow row = new DetailsOverviewRow(talk);

        Uri uri;
        if (talk.getThumbnailUrl() != null) {
            uri = Uri.parse(talk.getThumbnailUrl());
        } else {
            uri = Utils.getUri(getActivity(), R.drawable.conferences);
        }

        // Set card size from dimension resources.
        final int width = getResources().getDimensionPixelSize(R.dimen.detail_thumb_width);
        final int height = getResources().getDimensionPixelSize(R.dimen.detail_thumb_heigth);

       Glide.with(this)
                .load(uri)
                .asBitmap()
                .dontAnimate()
                .centerCrop()
                .error(R.drawable.conferences)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        row.setImageBitmap(getActivity(), resource);
                        startEntranceTransition();

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Log.e(TAG, e.getMessage());
                        row.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.conferences));
                        startEntranceTransition();
                    }

                    @Override
                    public void onDestroy() {
                        Glide.get(getActivity()).clearMemory();
                        super.onDestroy();
                    }
                });

        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();

        adapter.set(Constants.TALK_DETAIL_ACTION_PLAY_VIDEO,
                new Action(Constants.TALK_DETAIL_ACTION_PLAY_VIDEO, getResources()
                        .getString(R.string.detail_header_action_play_video)));


        Action action = new Action(Constants.TALK_DETAIL_ACTION_ADD_WATCHLIST);
        adapter.set(Constants.TALK_DETAIL_ACTION_ADD_WATCHLIST, updateWatchlistAction(action));

        row.setActionsAdapter(adapter);

        mAdapter.add(row);
    }

    private Action updateWatchlistAction(Action action){

        Boolean isWatchlist = watchlistCache.isExist(mTalkId);

        action.setLabel1(getResources().getString(isWatchlist ? R.string.detail_header_action_remove_from : R.string.detail_header_action_add_to));
        action.setLabel2(getResources().getString(R.string.watchlist).toUpperCase());
        action.setIcon(ContextCompat.getDrawable(getActivity(), isWatchlist ? R.drawable.ic_watchlist_on : R.drawable.ic_watchlist_off));

        return action;
    }


    private void setupRelatedListRow(Talk talk) {
        String subcategories[] = {getString(R.string.speakers)};

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

        if (talk.getSpeakers() != null) {

            for (Speaker speaker : talk.getSpeakers()) {
                Card card = new Card();
                card.setId(speaker.getUuid());
                card.setCardImageUrl(speaker.getAvatarUrl());
                card.setTitle(speaker.getFirstName() + " " + speaker.getLastName());
                card.setContent(speaker.getCompany());
                listRowAdapter.add(card);
            }
        }


        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));

        startEntranceTransition();
    }



    @Subscribe
    public void onMessageEvent(TalkEvent talkEvent) {

        Log.d(TAG, "Into onMessageEvent->TalkFullEvent");

        mSelectedTalk = mTalkCache.getData(talkEvent.getTalkId());
        if (mSelectedTalk == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }


        setupAdapter();
        setupDetailsOverviewRow(mSelectedTalk);
        setupRelatedListRow(mSelectedTalk);


        try {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "Cannot remove spinner after onSaveInstanceState has been called");
        }


    }


    @Subscribe
    public void onMessageEvent(ErrorEvent errorEvent) {

        // unable to retrieve the detail of a talk
        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
        ((TalkDetailActivity) getActivity()).displayErrorMessage(errorEvent.getErrorMessage(), true);
    }

}
