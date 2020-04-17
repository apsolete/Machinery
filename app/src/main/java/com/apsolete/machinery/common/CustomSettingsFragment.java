package com.apsolete.machinery.common;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.annotation.XmlRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import android.os.Bundle;

public abstract class CustomSettingsFragment<VM extends CustomViewModel> extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
{
    protected AppCompatActivity _activity;
    private int _layoutId;
    private int _titleId;
    private Class<VM> mVmClass;
    
    public CustomSettingsFragment(@XmlRes int layoutId, @StringRes int titleId, Class<VM> vmClass)
    {
        _layoutId = layoutId;
        _titleId = titleId;
        mVmClass = vmClass;
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