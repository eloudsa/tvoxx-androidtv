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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.SpeakerFullCache;
import net.noratek.tvoxx.androidtv.data.manager.SpeakerManager;
import net.noratek.tvoxx.androidtv.event.SpeakerEvent;
import net.noratek.tvoxx.androidtv.model.CardModel;
import net.noratek.tvoxx.androidtv.model.SpeakerFullModel;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;
import net.noratek.tvoxx.androidtv.model.TalkFullModel;
import net.noratek.tvoxx.androidtv.model.TalkShortModel;
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
public class DetailFragment extends DetailsFragment {

    private static final String TAG = DetailFragment.class.getSimpleName();


    @Bean
    SpeakerFullCache mSpeakerFullCache;

    /*
    @Bean
    TalkFullCache mTalkFullCache;

    @Bean
    TalkManager mTalkManager;
*/

    @Bean
    SpeakerManager mSpeakerManager;



    private SpinnerFragment mSpinnerFragment;

    private FullWidthDetailsOverviewSharedElementHelper mHelper;
    private ClassPresenterSelector mPresenterSelector;
    private ArrayObjectAdapter mAdapter;

    private String mDetailType;

    // Background image
    //private BackgroundImageManager mBackgroundImageManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ready to receive events
        EventBus.getDefault().register(this);

        mSpinnerFragment = new SpinnerFragment();

        // get parameters
        String detailId = getActivity().getIntent().getStringExtra(DetailActivity.DETAIL_ID);
        if (detailId == null) {
            getActivity().finish();
            return;
        }

        mDetailType = getActivity().getIntent().getStringExtra(DetailActivity.DETAIL_TYPE);


        // Prepare the manager that maintains the same background image between activities.
        //mBackgroundImageManager = new BackgroundImageManager(getActivity());


        // When a Related item is clicked.
        setOnItemViewClickedListener(new ItemViewClickedListener());

        // load appropriate data
        if (mDetailType.equalsIgnoreCase(DetailActivity.DETAIL_SPEAKER)) {
            loadSpeakerDetail(detailId);
        } if (mDetailType.equalsIgnoreCase(DetailActivity.DETAIL_TALK)) {
           // loadTalkDetail(detailId);
        } else {
            // not supported
            getActivity().finish();
            return;
        }
    }

    private void loadSpeakerDetail(String uuid) {

        // Display the spinner
        //getFragmentManager().beginTransaction().add(R.id.detail_fragment, mSpinnerFragment).commit();

        try {
            mSpeakerManager.fetchSpeaker(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/*
    private void loadTalkDetail(String talkId) {

        // Display the spinner
        getFragmentManager().beginTransaction().add(R.id.detail_fragment, mSpinnerFragment).commit();

        try {
            mTalkManager.fetchTalk(talkId);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    private void setupAdapter() {
        // Set detail background and style.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailDescriptionPresenter(),
                        new SpeakerDetailsOverviewLogoPresenter());

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

                if (action.getId() == 0) {
                    //Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                    //intent.putExtra(VideoDetailsActivity.VIDEO, mSelectedVideo);
                    //startActivity(intent);
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

    static class SpeakerDetailsOverviewLogoPresenter extends DetailsOverviewLogoPresenter {

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
                SpeakerDetailsOverviewLogoPresenter.ViewHolder vh =
                        (SpeakerDetailsOverviewLogoPresenter.ViewHolder) viewHolder;
                vh.getParentPresenter().notifyOnBindLogo(vh.getParentViewHolder());
            }
        }
    }



    /*
    private void setupAdapter() {
        // Set detail background and style.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailDescriptionPresenter(getActivity()),
                        new OverviewLogoPresenter());

       detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.selected_background));
        detailsPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_HALF);

        // Hook up transition element.
        mHelper = new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(getActivity(),
                DetailActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(mHelper);
        detailsPresenter.setParticipatingEntranceTransition(false);
        prepareEntranceTransition();

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {

                if (action.getId() == 0) {
                    //Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                    //intent.putExtra(VideoDetailsActivity.VIDEO, mSelectedVideo);
                    //startActivity(intent);
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
*/
    static class OverviewLogoPresenter extends DetailsOverviewLogoPresenter {

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
                OverviewLogoPresenter.ViewHolder vh =
                        (OverviewLogoPresenter.ViewHolder) viewHolder;
                vh.getParentPresenter().notifyOnBindLogo(vh.getParentViewHolder());
            }
        }
    }


    private void setupSpeakerDetailsOverviewRow(SpeakerFullModel speakerFullModel) {

        // change the background image
        //BackgroundImageManager backgroundImageManager = new BackgroundImageManager(getActivity());
        //backgroundImageManager.updateBackgroundWithDelay(Uri.parse(speakerFullModel.getAvatarUrl()));


        final DetailsOverviewRow row = new DetailsOverviewRow(speakerFullModel);

        Uri uri;
        if (speakerFullModel.getAvatarUrl() != null) {
            uri = Uri.parse(speakerFullModel.getAvatarUrl());
        } else {
            uri = Utils.getUri(getActivity(), R.drawable.ic_anonymous);
        }

        // Set card size from dimension resources.
        final int width = getResources().getDimensionPixelSize(R.dimen.detail_thumb_width);
        final int height = getResources().getDimensionPixelSize(R.dimen.detail_thumb_heigth);

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .dontAnimate()
                .centerCrop()
                .error(R.drawable.ic_anonymous)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        row.setImageBitmap(getActivity(), resource);
                        startEntranceTransition();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        row.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_anonymous));
                        startEntranceTransition();
                    }

                    @Override
                    public void onDestroy() {
                        Glide.get(getActivity()).clearMemory();
                        super.onDestroy();
                    }
                });


        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();

        adapter.set(Constants.SPEAKER_DETAIL_ACTION_ADD_FAVORITIES,
                new Action(Constants.SPEAKER_DETAIL_ACTION_ADD_FAVORITIES, getResources()
                        .getString(R.string.detail_header_action_add_Favorite)));

        row.setActionsAdapter(adapter);

        mAdapter.add(row);
    }


    public class onRelatedTalkClickedListener implements OnActionClickedListener {
        @Override
        public void onActionClicked(Action action) {
            /*
            if (action.getId() == ACTION_PLAY_VIDEO) {
                Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie);
                intent.putExtra(getResources().getString(R.string.should_start), true);
                startActivity(intent);
            }
            */
        }
    }


    private void setupSpeakerMovieListRow(SpeakerFullModel speakerFullModel) {
        String subcategories[] = {getString(R.string.related_talks)};

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

        if (speakerFullModel.getTalks() != null) {

            for (TalkShortModel talkShortModel : speakerFullModel.getTalks()) {
                CardModel cardModel = new CardModel();
                cardModel.setId(talkShortModel.getTalkId());
                cardModel.setCardImageUrl(talkShortModel.getThumbnailUrl());
                cardModel.setTitle(talkShortModel.getTitle());
                cardModel.setType(Constants.CARD_TYPE_RELATED_TALK);
                listRowAdapter.add(cardModel);
            }
        }


        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof CardModel) {
                CardModel cardModel = (CardModel) item;

                Intent intent = new Intent(getActivity(), DetailActivity_.class);
                intent.putExtra(DetailActivity.DETAIL_ID, cardModel.getId());
                intent.putExtra(DetailActivity.DETAIL_TYPE, DetailActivity.DETAIL_TALK);

                getActivity().startActivity(intent);
            }
        }
    }



    private void setupTalkDetailsOverviewRow(TalkFullModel talkFullModel) {

        // change the background image
        //mBackgroundImageManager.updateBackgroundWithDelay(Uri.parse(talkFullModel.getThumbnailUrl()));


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

        adapter.set(Constants.TALK_DETAIL_ACTION_ADD_FAVORITIES,
                new Action(Constants.TALK_DETAIL_ACTION_ADD_FAVORITIES, getResources()
                        .getString(R.string.detail_header_action_add_Favorite)));

        row.setActionsAdapter(adapter);

        mAdapter.add(row);
    }


    private void setupTalkMovieListRow(TalkFullModel talkFullModel) {
        String subcategories[] = {getString(R.string.speakers)};

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

        if (talkFullModel.getSpeakers() != null) {

            for (SpeakerModel speakerModel : talkFullModel.getSpeakers()) {
                CardModel cardModel = new CardModel();
                cardModel.setCardImageUrl(speakerModel.getAvatarUrl());
                cardModel.setTitle(speakerModel.getFirstName() + " " + speakerModel.getLastName());
                cardModel.setContent(speakerModel.getCompany());
                listRowAdapter.add(cardModel);
            }
        }


        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));
    }


    private void setupDetailsOverviewRow(SpeakerFullModel speakerFullModel) {

        // change the background image
        BackgroundImageManager backgroundImageManager = new BackgroundImageManager(getActivity());
        backgroundImageManager.updateBackgroundWithDelay(Uri.parse(speakerFullModel.getAvatarUrl()));


        final DetailsOverviewRow row = new DetailsOverviewRow(speakerFullModel);

        Uri uri;
        if (speakerFullModel.getAvatarUrl() != null) {
            uri = Uri.parse(speakerFullModel.getAvatarUrl());
        } else {
            uri = Utils.getUri(getActivity(), R.drawable.ic_anonymous);
        }

        // Set card size from dimension resources.
        final int width = getResources().getDimensionPixelSize(R.dimen.detail_thumb_width);
        final int height = getResources().getDimensionPixelSize(R.dimen.detail_thumb_heigth);

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .dontAnimate()
                .centerCrop()
                .error(R.drawable.ic_anonymous)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        row.setImageBitmap(getActivity(), resource);
                        startEntranceTransition();

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        row.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_anonymous));
                        startEntranceTransition();
                    }

                    @Override
                    public void onDestroy() {
                        Glide.get(getActivity()).clearMemory();
                        super.onDestroy();
                    }
                });


        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();

        adapter.set(Constants.SPEAKER_DETAIL_ACTION_ADD_FAVORITIES,
                new Action(Constants.SPEAKER_DETAIL_ACTION_ADD_FAVORITIES, getResources()
                        .getString(R.string.detail_header_action_add_Favorite)));

        row.setActionsAdapter(adapter);

        mAdapter.add(row);
    }


    private void setupMovieListRow(SpeakerFullModel speakerFullModel) {
        String subcategories[] = {getString(R.string.related_talks)};

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

        if (speakerFullModel.getTalks() != null) {

            for (TalkShortModel talkShortModel : speakerFullModel.getTalks()) {
                CardModel cardModel = new CardModel();
                cardModel.setCardImageUrl(talkShortModel.getThumbnailUrl());
                cardModel.setTitle(talkShortModel.getTitle());
                listRowAdapter.add(cardModel);
            }
        }


        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));
    }

    //
    // Events
    //

    @Subscribe
    public void onMessageEvent(SpeakerEvent speakerEvent) {

        SpeakerFullModel speakerFullModel = mSpeakerFullCache.getData(speakerEvent.getUuid());
        if (speakerFullModel == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }


        setupAdapter();
        //setupSpeakerDetailsOverviewRow(speakerFullModel);
        //setupSpeakerMovieListRow(speakerFullModel);

        setupDetailsOverviewRow(speakerFullModel);
        setupMovieListRow(speakerFullModel);


        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
    }

/*
    @Subscribe
    public void onEventMainThread(TalkFullEvent talkFullEvent) {

        TalkFullModel talkFullModel = mTalkFullCache.getData(talkFullEvent.getTalkId());
        if (talkFullModel == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }


        setupAdapter();
        setupTalkDetailsOverviewRow(talkFullModel);
        setupTalkMovieListRow(talkFullModel);

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
    }
*/

}
