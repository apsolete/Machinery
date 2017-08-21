package com.apsolete.machinery.activity.common;

import android.content.Context;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.app.*;
import android.os.*;
import android.content.SharedPreferences;

public abstract class SettingsBase extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
{
    protected Context _context;
    protected AppCompatActivity _activity;
    private int _layoutId;
    private int _titleId;

    public SettingsBase(Context context, int layoutId, int titleId)
    {
        _context = context;
        _layoutId = layoutId;
        _titleId = titleId;
    }

    @Override
    public void onAttach(Context context)
    {
        _activity = (AppCompatActivity)getActivity();
        super.onAttach(context);
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
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
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
        setPreferencesFromResource(_layoutId, rootKey);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        _activity.getSupportActionBar().setTitle(_titleId);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
    }
    
    public abstract void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key);
}
