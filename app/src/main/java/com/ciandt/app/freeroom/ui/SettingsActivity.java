package com.ciandt.app.freeroom.ui;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.constants.Constants;
import com.firebase.client.Firebase;

/**
 * Created by thales on 13/08/2015.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareLayout();
    }

    private void prepareLayout() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_prefs, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        Toolbar mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);

        mToolBar.setTitle(getTitle());
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigateUp();
            }
        });
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            Firebase myFirebaseRef = new Firebase(Constants.URL_FIREBASE);
            String stringValue = value.toString();
            preference.setSummary(stringValue);

            if(preference.getKey().equals("key_url_service")) {
                myFirebaseRef.child(Constants.URL_FREE_ROOM).setValue(stringValue);
            }else {
                myFirebaseRef.child(Constants.URL_TIME_RELOAD).setValue(stringValue);
            }

            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_url_service)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_time_reload)));
        }
    }
}
