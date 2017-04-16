package com.apsolete.machinery.activity.calculation;

import android.os.*;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.apsolete.machinery.activity.*;

public class DesignContentSettings extends PreferenceFragmentCompat
{
    private int _layoutId;
    private int _titleId;

    protected DesignContentSettings(int layoutId, int titleId)
    {
        _layoutId = layoutId;
        _titleId = titleId;
    }
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.settings_changegears, rootKey);
    }
}
