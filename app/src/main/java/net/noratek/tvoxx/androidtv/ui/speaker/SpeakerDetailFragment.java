package net.noratek.tvoxx.androidtv.ui.speaker;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
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

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.SpeakerCache;
import net.noratek.tvoxx.androidtv.data.manager.SpeakerManager;
import net.noratek.tvoxx.androidtv.event.ErrorEvent;
import net.noratek.tvoxx.androidtv.event.SpeakerEvent;
import net.noratek.tvoxx.androidtv.model.Card;
import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.presenter.CardPresenter;
import net.noratek.tvoxx.androidtv.presenter.DetailDescriptionPresenter;
import net.noratek.tvoxx.androidtv.ui.talk.TalkDetailActivity;
import net.noratek.tvoxx.androidtv.ui.talk.TalkDetailActivity_;
import net.noratek.tvoxx.androidtv.ui.util.SpinnerFragment;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


@EFragment
public class SpeakerDetailFragment extends DetailsFragment {

    private static final String TAG = SpeakerDetailFragment.class.getSimpleName();


    @Bean
    SpeakerCache speakerCache;

    @Bean
    SpeakerManager speakerManager;

    private SpinnerFragment mSpinnerFragment;

    private FullWidthDetailsOverviewSharedElementHelper mHelper;
    private ClassPresenterSelector mPresenterSelector;
    private ArrayObjectAdapter mAdapter;

    // Background image
    BackgroundManager mBackgroundManager;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        mSpinnerFragment = new SpinnerFragment();

        String uuid = getActivity().getIntent().getStringExtra(SpeakerDetailActivity.UUID);

        // prepare the background image
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        if (uuid != null) {
            loadSpeakerDetail(uuid);
        }

        // When a Related item is clicked.
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }



    private void loadSpeakerDetail(String uuid) {

        // Display the spinner
        getFragmentManager().beginTransaction().add(R.id.speaker_detail_fragment, mSpinnerFragment).commit();

        try {
            speakerManager.fetchSpeaker(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // change background image
        mBackgroundManager.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.footer_lodyas));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



    @Override
    public void onStop() {
        mBackgroundManager.release();
        super.onStop();
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Card) {
                Card card = (Card) item;

                Intent intent = new Intent(getActivity(), TalkDetailActivity_.class);
                intent.putExtra(TalkDetailActivity.TALK_ID, card.getId());
                getActivity().startActivity(intent);
            }
        }
    }

    private void setupAdapter() {

        prepareEntranceTransition();

        // Set detail background and style.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailDescriptionPresenter(getActivity()),
                        new SpeakerDetailsOverviewLogoPresenter());

        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.speaker_detail_background));
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


    private void setupDetailsOverviewRow(Speaker speaker) {

        final DetailsOverviewRow row = new DetailsOverviewRow(speaker);

        Uri uri;
        if (speaker.getAvatarUrl() != null) {
            uri = Uri.parse(speaker.getAvatarUrl());
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

        mAdapter.add(row);
    }


    private void setupMovieListRow(Speaker speaker) {
        String subcategories[] = {getString(R.string.related_talks)};

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

        if (speaker.getTalks() != null) {

            for (Talk talk : speaker.getTalks()) {
                Card card = new Card();
                card.setId(talk.getTalkId());
                card.setCardImageUrl(talk.getThumbnailUrl());
                card.setTitle(talk.getTitle());
                listRowAdapter.add(card);
            }
        }


        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));

        startEntranceTransition();
    }


    @Subscribe
    public void onMessageEvent(SpeakerEvent speakerEvent) {

        Speaker speaker = speakerCache.getData(speakerEvent.getUuid());
        if (speaker == null) {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
            return;
        }


        setupAdapter();
        setupDetailsOverviewRow(speaker);
        setupMovieListRow(speaker);


        try {
            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "Cannot remove spinner after onSaveInstanceState has been called");
        }

    }


    @Subscribe
    public void onMessageEvent(ErrorEvent errorEvent) {

        // unable to retrieve the detail of a speaker

        getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
        ((SpeakerDetailActivity) getActivity()).displayErrorMessage(errorEvent.getErrorMessage(), true);
    }

}
