/*
 * Copyright (C) 2015 The Android Open Source Project
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

package net.noratek.tvoxx.androidtv.ui.settings;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v17.preference.LeanbackPreferenceFragment;
import android.support.v17.preference.LeanbackSettingsFragment;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import net.noratek.tvoxx.androidtv.R;
import net.noratek.tvoxx.androidtv.data.cache.BaseCache;
import net.noratek.tvoxx.androidtv.utils.Utils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import java.util.Stack;

@EFragment
public class SettingsFragment extends LeanbackSettingsFragment implements DialogPreference.TargetFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Bean
    BaseCache baseCache;


    private final Stack<Fragment> fragments = new Stack<Fragment>();

    @Override
    public void onPreferenceStartInitialScreen() {
        startPreferenceFragment(buildPreferenceFragment(R.xml.settings, null));
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment,
                                             Preference preference) {
        return false;
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragment preferenceFragment,
                                           PreferenceScreen preferenceScreen) {
        PreferenceFragment frag = buildPreferenceFragment(R.xml.settings, preferenceScreen.getKey());
        startPreferenceFragment(frag);
        return true;
    }

    @Override
    public Preference findPreference(CharSequence prefKey) {
        return ((PreferenceFragment) fragments.peek()).findPreference(prefKey);
    }

    private PreferenceFragment buildPreferenceFragment(int preferenceResId, String root) {
        PreferenceFragment fragment = new PrefFragment();
        Bundle args = new Bundle();
        args.putInt("preferenceResource", preferenceResId);
        args.putString("root", root);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private class PrefFragment extends LeanbackPreferenceFragment {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            String root = getArguments().getString("root", null);
            int prefResId = getArguments().getInt("preferenceResource");
            if (root == null) {
                addPreferencesFromResource(prefResId);
            } else {
                setPreferencesFromResource(prefResId, root);
            }

            Preference prefApp = findPreference("settings_key_about_app");
            if (prefApp != null) {
                prefApp.setSummary(Utils.getVersionInfo(getActivity()));
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {

            if (preference.getKey().equalsIgnoreCase(getString(R.string.settings_key_cache))) {

                final ListPreference list = (ListPreference) preference;
                list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        if (newValue.toString().equalsIgnoreCase(getString(R.string.settings_value_yes))) {

                            // Clear the local cache
                            baseCache.clearAllCache();

                            Toast.makeText(getActivity(), getString(R.string.settings_cache_info_clear), Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
            }

            return super.onPreferenceTreeClick(preference);
        }

        @Override
        public void onAttach(Context context) {
            fragments.push(this);
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            fragments.pop();
            super.onDetach();
        }
    }

}
