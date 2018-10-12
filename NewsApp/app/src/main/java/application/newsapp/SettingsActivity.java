package application.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    public static class SettingsActivityPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference searchPreference = findPreference("search_edit_text_preference");
            setSummaryValueForPreferenceFromSharedPreferences(searchPreference);

            Preference tagsPreference = findPreference("tags_edit_text_preference");
            setSummaryValueForPreferenceFromSharedPreferences(tagsPreference);

            Preference sectionsPreference = findPreference("sections_edit_text_preference");
            setSummaryValueForPreferenceFromSharedPreferences(sectionsPreference);

            searchPreference.setOnPreferenceChangeListener(this);
            tagsPreference.setOnPreferenceChangeListener(this);
            sectionsPreference.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            //add summary under preference title (the strings saying what is selected in that preference)
            preference.setSummary(o.toString());

            return true;
        }

        public void setSummaryValueForPreferenceFromSharedPreferences(Preference preference) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            String preferenceString = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }

}
