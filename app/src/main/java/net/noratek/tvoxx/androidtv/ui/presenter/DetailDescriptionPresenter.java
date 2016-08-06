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
import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.model.SpeakerModel;

public class DetailDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    private final Context mContext;

    public DetailDescriptionPresenter(Context context) {
        super();
        this.mContext = context;
    }


    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {

        if (item == null) {
            return;
        }

        if (item instanceof SpeakerModel) {

            SpeakerModel speaker = (SpeakerModel) item;

            viewHolder.getTitle().setText(speaker.getFirstName() + " " + speaker.getLastName());
            viewHolder.getSubtitle().setText(speaker.getCompany());
            viewHolder.getBody().setText(mContext.getText(R.string.lorem_ipsum_1));
        }

    }
}
