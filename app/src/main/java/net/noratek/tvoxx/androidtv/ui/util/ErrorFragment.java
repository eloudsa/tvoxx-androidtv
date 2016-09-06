package net.noratek.tvoxx.androidtv.ui.util;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.utils.Constants;

import org.androidannotations.annotations.EFragment;


@EFragment
public class ErrorFragment extends android.support.v17.leanback.app.ErrorFragment {

    private static final String TAG = ErrorFragment.class.getSimpleName();

    private static final boolean TRANSLUCENT = true;

    private String mErrorMessage;
    private boolean mFinishOnDismiss;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mErrorMessage = getArguments().getString(Constants.ERROR_RESOURCE_MESSAGE);
        mFinishOnDismiss = getArguments().getBoolean(Constants.ERROR_FINISH_ON_DISMISS, false);

        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tvoxx_logo));
        setTitle(getString(R.string.oops));

        setErrorContent();
    }

    void setErrorContent() {
        setImageDrawable(getActivity().getDrawable(R.drawable.lb_ic_sad_cloud));
        setMessage(mErrorMessage);
        setDefaultBackground(TRANSLUCENT);

        setButtonText(getResources().getString(R.string.dismiss_error));
        setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mFinishOnDismiss) {
                    getActivity().finish();
                } else {
                    getFragmentManager().beginTransaction().remove(ErrorFragment.this).commit();
                    getFragmentManager().popBackStack();
                }
            }
        });
    }





}