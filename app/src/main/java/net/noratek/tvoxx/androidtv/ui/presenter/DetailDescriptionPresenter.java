/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.noratek.tvoxx.androidtv.ui.presenter;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.model.Speaker;
import net.noratek.tvoxx.androidtv.model.Talk;
import net.noratek.tvoxx.androidtv.utils.ResourceCache;


public class DetailDescriptionPresenter extends Presenter {

    private static final String TAG = DetailDescriptionPresenter.class.getSimpleName();

    private ResourceCache mResourceCache = new ResourceCache();
    private Context mContext;

    public DetailDescriptionPresenter(Context context) {
        this.mContext = context;
    }

    /*
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {

        if (item == null) {
            return;
        }

        String title = "";
        String subTitle = "";
        String body = "";

        if (item instanceof Speaker) {

            Speaker speaker = (Speaker) item;

            title = speaker.getFirstName() + " " + speaker.getLastName();
            subTitle = speaker.getCompany();
            body = speaker.getBio();


        } else if (item instanceof Talk) {

            Talk talk = (Talk) item;

            title = talk.getTitle();
            subTitle = talk.getConferenceLabel();
            body = talk.getSummary();
        }


        viewHolder.getTitle().setText(title);
        viewHolder.getSubtitle().setText(subTitle);
        viewHolder.getBody().setText(body);

        //viewHolder.

        viewHolder.getBody().setMaxLines(10);


    }
    */



    @Override public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_view_content, null);
        return new Presenter.ViewHolder(view);
    }

    @Override public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        TextView primaryText = mResourceCache.getViewById(viewHolder.view, R.id.primary_text);
        TextView sndText1 = mResourceCache.getViewById(viewHolder.view, R.id.secondary_text_first);
        TextView sndText2 = mResourceCache.getViewById(viewHolder.view, R.id.secondary_text_second);
        TextView extraText = mResourceCache.getViewById(viewHolder.view, R.id.extra_text);



        if (item == null) {
            return;
        }

        String title = "";
        String subTitle = "";
        String body = "";

        if (item instanceof Speaker) {

            Speaker speaker = (Speaker) item;

            title = speaker.getFirstName() + " " + speaker.getLastName();
            subTitle = speaker.getCompany();
            body = speaker.getBio();

            sndText1.setVisibility(View.GONE);


        } else if (item instanceof Talk) {

            Talk talk = (Talk) item;

            title = talk.getTitle();
            subTitle = talk.getConferenceLabel();
            body = talk.getSummary();
        }

        primaryText.setText(title);
        sndText1.setText(subTitle);
        sndText2.setText("Year ");
        extraText.setText(body);
    }

    @Override public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // Nothing to do here.
    }


}
