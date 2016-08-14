package net.noratek.tvoxx.androidtv.ui;

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
import net.noratek.tvoxx.androidtv.data.cache.TalkFullCache;
import net.noratek.tvoxx.androidtv.data.manager.TalkManager;
import net.noratek.tvoxx.androidtv.event.TalkEvent;
import net.noratek.tvoxx.androidtv.model.CardModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.model.TalkFullModel;
import net.noratek.tvoxx.androidtv.ui.manager.BackgroundImageManager;
import net.noratek.tvoxx.androidtv.ui.presenter.CardPresenter;
import net.noratek.tvoxx.androidtv.ui.presenter.DetailDescriptionPresenter;
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
    TalkFullCache mTalkFullCache;

    @Bean
    TalkManager mTalkManager;

    private SpinnerFragment mSpinnerFragment;

    private FullWidthDetailsOverviewSharedElementHelper mHelper;
    private ClassPresenterSelector mPresenterSelector;
    private ArrayObjectAdapter mAdapter;

    private String mTalkId;
    private TalkFullModel mSelectedTalk;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "+++++ REgister eventbus");
        EventBus.getDefault().register(TalkDetailFragment.this);

        mSpinnerFragment = new SpinnerFragment();

        mTalkId = getActivity().getIntent().getStringExtra(TalkDetailActivity.TALK_ID);
        if (mTalkId != null) {
            loadDetail();
        }
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
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }




    @Override
    public void onStop() {
        Log.d(TAG, "----- UNregister eventbus");
        EventBus.getDefault().unregister(TalkDetailFragment.this);
        super.onStop();
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof CardModel) {
                CardModel cardModel = (CardModel) item;

                Intent intent = new Intent(getActivity(), SpeakerDetailActivity_.class);
                intent.putExtra(SpeakerDetailActivity.UUID, cardModel.getId());
                getActivity().startActivity(intent);
            }
        }
    }



    private void setupAdapter() {
        // Set detail background and style.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailDescriptionPresenter(),
                        new detailsOverviewLogoPresenter());

        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.selected_background));
        detailsPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_HALF);

        // Hook up transition element.
        mHelper = new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(getActivity(),
                SpeakerDetailActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(mHelper);
        detailsPresenter.setParticipatingEntranceTransition(false);
        prepareEntranceTransition();

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
                        ((TalkDetailActivity) getActivity()).displayErrorMessage(R.string.error_youtube_player_failed);
                    }

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



    static class detailsOverviewLogoPresenter extends DetailsOverviewLogoPresenter {

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
                detailsOverviewLogoPresenter.ViewHolder vh =
                        (detailsOverviewLogoPresenter.ViewHolder) viewHolder;
                vh.getParentPresenter().notifyOnBindLogo(vh.getParentViewHolder());
            }
        }
    }


    private void setupDetailsOverviewRow(TalkFullModel talkFullModel) {

        // change the background image
        BackgroundImageManager backgroundImageManager = new BackgroundImageManager(getActivity());
        backgroundImageManager.updateBackgroundWithDelay(Uri.parse(talkFullModel.getThumbnailUrl()));


        final DetailsOverviewRow row = new DetailsOverviewRow(talkFullModel);

        Uri uri;
        if (talkFullModel.getThumbnailUrl() != null) {
            uri = Uri.parse(talkFullModel.getThumbnailUrl());
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

        adapter.set(Constants.TALK_DETAIL_ACTION_ADD_FAVORITIES,
                new Action(Constants.TALK_DETAIL_ACTION_ADD_FAVORITIES, getResources()
                        .getString(R.string.detail_header_action_add_Favorite)));


        row.setActionsAdapter(adapter);

        mAdapter.add(row);
    }


    private void setupRelatedListRow(TalkFullModel talkFullModel) {
        String subcategories[] = {getString(R.string.speakers)};

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

        if (talkFullModel.getSpeakers() != null) {

            for (SpeakerModel speakerModel : talkFullModel.getSpeakers()) {
                CardModel cardModel = new CardModel();
                cardModel.setId(speakerModel.getUuid());
                cardModel.setCardImageUrl(speakerModel.getAvatarUrl());
                cardModel.setTitle(speakerModel.getFirstName() + " " + speakerModel.getLastName());
                cardModel.setContent(speakerModel.getCompany());
                listRowAdapter.add(cardModel);
            }
        }


        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));
    }



    @Subscribe
    public void onMessageEvent(TalkEvent talkEvent) {

        Log.d(TAG, "Into onMessageEvent->TalkFullEvent");

        mSelectedTalk = mTalkFullCache.getData(talkEvent.getTalkId());
        if (mSelectedTalk == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }


        setupAdapter();
        setupDetailsOverviewRow(mSelectedTalk);
        setupRelatedListRow(mSelectedTalk);

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
    }

}
