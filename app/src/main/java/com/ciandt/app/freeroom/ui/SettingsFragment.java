package com.ciandt.app.freeroom.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.constants.Constants;
import com.firebase.client.Firebase;

/**
 * Created by thales on 13/08/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_url_service)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_time_reload)));
        bindPreferenceClickListener(findPreference(getString(R.string.key_close_app)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void bindPreferenceClickListener(Preference preference) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("key_close_app")) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }

                return true;
            }
        });
    }

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Firebase myFirebaseRef = new Firebase(Constants.URL_FIREBASE);
            String stringValue = value.toString();
            preference.setSummary(stringValue);

            if (preference.getKey().equals("key_url_service")) {
                myFirebaseRef.child(Constants.URL_FREE_ROOM).setValue(stringValue);
            } else if (preference.getKey().equals("key_time_reload")) {
                myFirebaseRef.child(Constants.URL_TIME_RELOAD).setValue(stringValue);
            }

            return true;
        }
    };
}
