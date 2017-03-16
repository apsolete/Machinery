package com.apsolete.machinery.activity;

import android.os.*;

import com.apsolete.machinery.*;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.app.*;

public class ContentSettings extends PreferenceFragmentCompat
{
    private AppCompatActivity _activity;
    private int _layoutId;
    private int _titleId;
    public ContentSettings()
    {
        _layoutId = R.xml.preferences_main;
        _titleId = R.string.nav_settings;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        _activity = (AppCompatActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null)
        {
            _layoutId = savedInstanceState.getInt("layout");
            _titleId = savedInstanceState.getInt("title");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("layout", _layoutId);
        outState.putInt("title", _titleId);
    }
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences_main, rootKey);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        _activity.getSupportActionBar().setTitle(_titleId);
    }
}
