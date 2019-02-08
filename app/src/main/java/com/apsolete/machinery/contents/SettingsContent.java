package com.apsolete.machinery.contents;

import com.apsolete.machinery.*;
import com.apsolete.machinery.common.*;

import android.content.*;

public class SettingsContent extends SettingsBase
{
    public SettingsContent()
    {
        super(R.xml.settings_main, R.string.nav_settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        // TODO: Implement this method
    }
}
