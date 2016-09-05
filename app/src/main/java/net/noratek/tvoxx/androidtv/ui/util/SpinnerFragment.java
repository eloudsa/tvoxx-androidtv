package net.noratek.tvoxx.androidtv.ui.util;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import net.noratek.tvoxx.androidtv.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by eloudsa on 05/08/16.
 */
@EFragment
public class SpinnerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar = new ProgressBar(container.getContext());

        if (container instanceof FrameLayout) {
            Resources res = getResources();
            int width = res.getDimensionPixelSize(R.dimen.spinner_width);
            int height = res.getDimensionPixelSize(R.dimen.spinner_height);
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(width, height, Gravity.CENTER);
            progressBar.setLayoutParams(layoutParams);

            progressBar.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(getActivity(), R.color.spinner_color), PorterDuff.Mode.SRC_IN);

        }

        return progressBar;
    }
}
