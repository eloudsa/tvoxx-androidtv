/*
 * Copyright (c) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.noratek.tvoxx.androidtv.ui.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import net.noratek.tvoxx.androidtv.model.TalkFullModel;

public class VideoPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        TalkFullModel talkFullModel = (TalkFullModel) item;

        if (talkFullModel != null) {
            viewHolder.getTitle().setText(talkFullModel.getTitle());
            viewHolder.getSubtitle().setText(talkFullModel.getConferenceLabel());
            viewHolder.getBody().setText(talkFullModel.getSummary());
        }
    }
}
