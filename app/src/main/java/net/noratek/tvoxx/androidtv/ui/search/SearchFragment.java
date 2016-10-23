package net.noratek.tvoxx.androidtv.ui.search;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.text.TextUtils;
import android.util.Log;

import net.noratek.tvoxx.androidtv.connection.Connection;
import net.noratek.tvoxx.androidtv.data.cache.WatchlistCache;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.presenter.TalkCardPresenter;
import net.noratek.tvoxx.androidtv.ui.cards.TalkCardView;
import net.noratek.tvoxx.androidtv.ui.talk.TalkDetailActivity_;
import net.noratek.tvoxx.androidtv.utils.Constants;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@EFragment
public class SearchFragment extends android.support.v17.leanback.app.SearchFragment
        implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider {

    private static final String TAG = SearchFragment.class.getSimpleName();

    @Bean
    Connection connection;

    @Bean
    WatchlistCache watchlistCache;

    private static final int REQUEST_SPEECH = 0x00000010;
    private static final long SEARCH_DELAY_MS = 1000L;

    private Call<List<Talk>> mCall;
    private AsyncTask<Void, Void, ListRow> mLoadRowsAsync;

    private List<Talk> mTalks;

    TalkCardPresenter mTalkPresenter;

    // selected talk
    TalkCardView mSelectedCardView;
    String mSelectedTalkId;


    private ArrayObjectAdapter mRowsAdapter;

    private final Handler mHandler = new Handler();

    private final Runnable mDelayedLoad = new Runnable() {
        @Override
        public void run() {
            searchTalks();
        }
    };

    private String mQuery;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setSearchResultProvider(this);
        setOnItemViewClickedListener(new ItemViewClickedListener());

        if (!Utils.hasPermission(getActivity(), Manifest.permission.RECORD_AUDIO)) {

            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    try {
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "Cannot find activity for speech recognizer", e);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        List<String> watchList = watchlistCache.getData();

        if (mTalkPresenter != null) {
            mTalkPresenter.setWatchList(watchList);
        }

        if ((mSelectedCardView != null) && (watchList != null)) {
            mSelectedCardView.updateWatchList(watchList.contains(mSelectedTalkId));
        }
    }


    public boolean hasResults() {
        return mRowsAdapter.size() > 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SPEECH) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    setSearchQuery(data, true);
                    break;

                case RecognizerIntent.RESULT_CLIENT_ERROR:
                    Log.w(TAG, Integer.toString(requestCode));
            }

        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        // data already prepared in loadRows method
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        loadQueryWithDelay(newQuery, SEARCH_DELAY_MS);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // No need to delay(wait) loadQuery, since the query typing has completed.
        loadQueryWithDelay(query, 0);
        return true;
    }

    /**
     * Starts {@link #loadRows()} method after delay.
     * It also cancels previously registered task if it has not yet executed.
     *
     * @param query the word to be searched
     * @param delay the time to wait until loadRows will be executed (milliseconds).
     */
    private void loadQueryWithDelay(String query, long delay) {
        mHandler.removeCallbacks(mDelayedLoad);
        if ((!TextUtils.isEmpty(query)) && (query.length() >= 3)) {
            mQuery = query;
            mHandler.postDelayed(mDelayedLoad, delay);
        }
    }


    private void searchTalks() {

        cancelTasks();

        // retrieve the list of talks from the server
        mCall = connection.getTvoxxApi().searchTalks(mQuery);
        mCall.enqueue(new Callback<List<Talk>>() {
            @Override
            public void onResponse(Call<List<Talk>> call, Response<List<Talk>> response) {
                if (response.isSuccessful()) {
                    mTalks = response.body();
                    loadRows();
                }
            }

            @Override
            public void onFailure(Call<List<Talk>> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG, "request was cancelled");
                }
                else {
                    Log.e(TAG, t.getMessage());
                }

            }
        });
    }


    private void cancelTasks() {
        if (mCall != null) {
            mCall.cancel();
        }

        if (mLoadRowsAsync != null) {
            mLoadRowsAsync.cancel(true);
        }
    }


    /**
     * Searches query specified by mQuery, and sets the result to mRowsAdapter.
     */
    private void loadRows() {
        // offload processing from the UI thread
        mLoadRowsAsync = new AsyncTask<Void, Void, ListRow>() {

            @Override
            protected void onPreExecute() {
                mRowsAdapter.clear();
            }

            @Override
            protected ListRow doInBackground(Void... params) {

                mTalkPresenter = new TalkCardPresenter(getActivity(), watchlistCache.getData());

                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(mTalkPresenter);
                listRowAdapter.addAll(0, mTalks);
                HeaderItem header = new HeaderItem("Search Results");
                return new ListRow(header, listRowAdapter);
            }

            @Override
            protected void onPostExecute(ListRow listRow) {
                mRowsAdapter.add(listRow);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Talk) {
                Talk talk = (Talk) item;

                mSelectedCardView = (TalkCardView) itemViewHolder.view;
                mSelectedTalkId = talk.getTalkId();

                Intent intent = new Intent(getActivity(), TalkDetailActivity_.class);
                intent.putExtra(Constants.TALK_ID, talk.getTalkId());
                getActivity().startActivity(intent);
            }

        }
    }
}