package com.example.adolescentonlinesafety;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sh;
    Preference editText;
    Preference switchPref;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);

        sh = PreferenceManager.getDefaultSharedPreferences(getContext());

        editText = findPreference("user_msg");
        switchPref = findPreference("reset_switch");


        // read the value from the SharedPreference
        String sosMsg = sh.getString("user_msg", getString(R.string.default_msg));
        editText.setSummary(sosMsg);

        editText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                // just show the new value in summary
                // and write the newValue after returning from the Settings Screen
                editText.setSummary(newValue.toString());

                return true;
            }
        });


        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if ((boolean) newValue) {
                    // true

                    // Write back the default preference values
                    SharedPreferences.Editor edit = sh.edit();
                    //edit.clear();
                    //edit.remove("user_msg");

                    edit.putString("user_msg", getString(R.string.default_msg));

                    edit.apply();


                    // just show the new value in summary
                    editText.setSummary(getString(R.string.default_msg));

                    Toast.makeText(getContext(), "Default text has been set", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }
}
