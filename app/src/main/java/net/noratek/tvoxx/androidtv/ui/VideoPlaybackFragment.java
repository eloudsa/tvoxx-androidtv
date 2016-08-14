package net.noratek.tvoxx.androidtv.ui;

import android.support.v17.leanback.app.PlaybackOverlayFragment;

import org.androidannotations.annotations.EFragment;

@EFragment
public class VideoPlaybackFragment extends PlaybackOverlayFragment {

    /*
    private static final String TAG = VideoPlaybackFragment.class.getSimpleName();

    private static final int ACTION_PLAY_VIDEO = 1;

    private static final int FULL_WIDTH_DETAIL_THUMB_WIDTH = 220;
    private static final int FULL_WIDTH_DETAIL_THUMB_HEIGHT = 120;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    public static final String CATEGORY_FULL_WIDTH_DETAILS_OVERVIEW_ROW_PRESENTER = "FullWidthDetailsOverviewRowPresenter";

    // Attribute 
    private ArrayObjectAdapter mAdapter;
    private CustomFullWidthDetailsOverviewRowPresenter mFwdorPresenter;
    private ClassPresenterSelector mClassPresenterSelector;
    private ListRow mRelatedVideoRow = null;

    private DetailsRowBuilderTask mDetailsRowBuilderTask;

    // Relation 
    private TalkFullModel mSelectedTalk;
    private LinkedHashMap<String, List<TalkFullModel>> mVideoLists = null;

    // Background image
    private BackgroundImageManager mBackgroundImageManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSelectedTalk = ((VideoPlaybackActivity) getActivity()).getSelectedTalk();
        if (mSelectedTalk == null) {
            getActivity().finish();
            return;
        }


        mFwdorPresenter = new CustomFullWidthDetailsOverviewRowPresenter(new DetailDescriptionPresenter());

        mDetailsRowBuilderTask = (DetailsRowBuilderTask) new DetailsRowBuilderTask().execute(mSelectedTalk);

        setOnItemViewClickedListener(new ItemViewClickedListener());

        mBackgroundImageManager.updateBackgroundWithDelay(Utils.getYouTubeUrl(mSelectedTalk.getYoutubeVideoId()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mClassPresenterSelector = new ClassPresenterSelector();

        mClassPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mFwdorPresenter);

        mClassPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        mAdapter = new ArrayObjectAdapter(mClassPresenterSelector);
        setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        mDetailsRowBuilderTask.cancel(true);
        mBackgroundImageManager.cancel();
        super.onStop();
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof TalkFullModel) {
                TalkFullModel talk = (TalkFullModel) item;
                Intent intent = new Intent(getActivity(), VideoPlaybackActivity.class);
                intent.putExtra(Constants.TALK_ID, talk.getTalkId());

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }

    private class DetailsRowBuilderTask extends AsyncTask<TalkFullModel, Integer, DetailsOverviewRow> {
        @Override
        protected DetailsOverviewRow doInBackground(TalkFullModel... params) {
            Log.v(TAG, "DetailsRowBuilderTask doInBackground");
            int width, height;
            if(mSelectedTalk.getCategory().equals(CATEGORY_DETAILS_OVERVIEW_ROW_PRESENTER)) {
                // If category name is "DetailsOverviewRowPresenter", show DetailsOverviewRowPresenter for demo purpose (this class is deprecated from API level 22) 
                width = DETAIL_THUMB_WIDTH;
                height = DETAIL_THUMB_HEIGHT;
            } else {
                // Default behavior, show FullWidthDetailsOverviewRowPresenter 
                width = FULL_WIDTH_DETAIL_THUMB_WIDTH;
                height = FULL_WIDTH_DETAIL_THUMB_HEIGHT;
            }

            DetailsOverviewRow row = new DetailsOverviewRow(mSelectedTalk);
            try {
                // Bitmap loading must be done in background thread in Android.
                Bitmap poster = Picasso.with(getActivity())
                        .load(mSelectedTalk.getCardImageUrl())
                        .resize(Utils.convertDpToPixel(getActivity().getApplicationContext(), width),
                                Utils.convertDpToPixel(getActivity().getApplicationContext(), height))
                        .centerCrop()
                        .get();
                row.setImageBitmap(getActivity(), poster);

                mVideoLists = VideoProvider.buildMedia(getActivity());
            } catch (IOException e) {
                Log.w(TAG, e.toString());
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
            return row;
        }

        @Override
        protected void onPostExecute(DetailsOverviewRow row) {
            Log.v(TAG, "DetailsRowBuilderTask onPostExecute");
            // 1st row: DetailsOverviewRow 

              // action setting
            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            sparseArrayObjectAdapter.set(0, new Action(ACTION_PLAY_VIDEO, "Play Video"));
            sparseArrayObjectAdapter.set(1, new Action(1, "Action 2", "label"));
            sparseArrayObjectAdapter.set(2, new Action(2, "Action 3", "label"));

            row.setActionsAdapter(sparseArrayObjectAdapter);

            mFwdorPresenter.setOnActionClickedListener(new DetailsOverviewRowActionClickedListener());

            // 2nd row: ListRow CardPresenter 

            if (mVideoLists == null) {
                // Error occured while fetching videos
                Log.i(TAG, "mVideoLists is null, skip creating mRelatedVideoRow");
            } else {
                CardPresenter cardPresenter = new CardPresenter();

                for (Map.Entry<String, List<TalkFullModel>> entry : mVideoLists.entrySet()) {
                    // Find only same category
                    String categoryName = entry.getKey();
                    if(!categoryName.equals(mSelectedTalk.getCategory())) {
                        continue;
                    }

                    ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(cardPresenter);
                    List<TalkFullModel> list = entry.getValue();

                    for (int j = 0; j < list.size(); j++) {
                        cardRowAdapter.add(list.get(j));
                    }
                    //HeaderItem header = new HeaderItem(index, entry.getKey());
                    HeaderItem header = new HeaderItem(0, "Related Videos");
                    mRelatedVideoRow = new ListRow(header, cardRowAdapter);
                }
            }

            // 2nd row: ListRow 
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());

            ArrayList<TalkFullModel> mItems = MovieProvider.getMovieItems();
            for (TalkFullModel talk : mItems) {
                listRowAdapter.add(talk);
            }
            HeaderItem headerItem = new HeaderItem(0, "Related Videos");


            mAdapter = new ArrayObjectAdapter(mClassPresenterSelector);
            // 1st row 
            mAdapter.add(row);

            // 2nd row 
            if(mRelatedVideoRow != null){
                mAdapter.add(mRelatedVideoRow);
            }
            //mAdapter.add(new ListRow(headerItem, listRowAdapter));

            // 3rd row 
            //adapter.add(new ListRow(headerItem, listRowAdapter));
            setAdapter(mAdapter);
        }
    }

    public class DetailsOverviewRowActionClickedListener implements OnActionClickedListener {
        @Override
        public void onActionClicked(Action action) {
            if (action.getId() == ACTION_PLAY_VIDEO) {
                Intent intent = new Intent(getActivity(), VideoPlaybackActivity.class);
                intent.putExtra(Constants.TALK_ID., mSelectedTalk.getTalkId());
                startActivity(intent);
            }
        }
    }
    */
}