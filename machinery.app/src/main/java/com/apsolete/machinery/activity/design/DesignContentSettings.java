package com.apsolete.machinery.activity.design;

import android.content.*;
import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.common.*;

public class DesignContentSettings extends SettingsBase
{
    protected DesignContentSettings(Context context)
    {
        super(context, R.xml.settings_changegears, 0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        // TODO: Implement this method
    }
}
