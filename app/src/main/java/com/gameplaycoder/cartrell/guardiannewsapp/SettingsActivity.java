package com.gameplaycoder.cartrell.guardiannewsapp;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // public
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // GuardianNewsPreferenceFragment
  //=======================================================================================
  public static class GuardianNewsPreferenceFragment extends PreferenceFragment
  implements Preference.OnPreferenceChangeListener {

    ///////////////////////////////////////////////////////////////////////////////////////
    // public
    ///////////////////////////////////////////////////////////////////////////////////////

    //=====================================================================================
    // onCreate
    //=====================================================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings_main);

      Preference numArticles = findPreference(getString(R.string.settings_num_articles_key));
      bindPreferenceSummaryToValue(numArticles);

      Preference section = findPreference(getString(R.string.settings_section_key));
      bindPreferenceSummaryToValue(section);
    }

    //=====================================================================================
    // onPreferenceChange
    //=====================================================================================
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
      String stringValue = value.toString();

      if (preference instanceof ListPreference) {
        ListPreference listPreference = (ListPreference)preference;
        int prefIndex = listPreference.findIndexOfValue(stringValue);
        if (prefIndex >= 0) {
          CharSequence[] labels = listPreference.getEntries();
          preference.setSummary(labels[prefIndex]);
        }
      } else {
        preference.setSummary(stringValue);
      }

      return(true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // private
    ///////////////////////////////////////////////////////////////////////////////////////

    //=====================================================================================
    // bindPreferenceSummaryToValue
    //=====================================================================================
    private void bindPreferenceSummaryToValue(Preference preference) {
      preference.setOnPreferenceChangeListener(this);
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
      String preferenceString = preferences.getString(preference.getKey(), "");
      onPreferenceChange(preference, preferenceString);
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // protected
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // onCreate
  //=======================================================================================
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
  }
}
