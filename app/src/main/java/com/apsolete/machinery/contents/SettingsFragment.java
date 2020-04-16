package com.apsolete.machinery.contents;

import android.content.SharedPreferences;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomSettingsFragment;

public class SettingsFragment extends CustomSettingsFragment<SettingsViewModel>
{
    public SettingsFragment()
    {
        super(R.xml.settings_main, R.string.nav_settings, SettingsViewModel.class);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        // TODO: Implement this method
    }
}
