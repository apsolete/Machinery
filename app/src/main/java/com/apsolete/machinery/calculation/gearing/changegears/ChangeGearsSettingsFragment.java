package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomSettingsFragment;

public class ChangeGearsSettingsFragment extends CustomSettingsFragment<ChangeGearsSettingsViewModel>
{
    public interface OnChangeListener
    {
        void onDiffTeethGearingChanged(boolean newValue);
        void onDiffTeethDoubleGearChanged(boolean newValue);
        void onRatioPrecisionChanged(int newValue);
    }
    
    private OnChangeListener _listener;
    
    public ChangeGearsSettingsFragment()
    {
        super(R.xml.settings_changegears, R.string.title_changegears, ChangeGearsSettingsViewModel.class);
    }
    
    public void setListener(OnChangeListener listener)
    {
        _listener = listener;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (_listener == null)
            return;
            
        switch(key)
        {
            case "diffteethgearing":
                _listener.onDiffTeethGearingChanged(sharedPreferences.getBoolean(key, true));
                break;
            case "diffteethdoublegear":
                _listener.onDiffTeethDoubleGearChanged(sharedPreferences.getBoolean(key, true));
                break;
            case "accuracyofratio":
                _listener.onRatioPrecisionChanged(sharedPreferences.getInt(key, 1));
                break;
        }
    }

    public boolean getDiffTeethGearing()
    {
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sharedPref = (pm != null)
            ? pm.getSharedPreferences()
            : PreferenceManager.getDefaultSharedPreferences(_activity);
        return sharedPref.getBoolean("diffteethgearing", false);
    }
    
    public boolean getDiffTeethDoubleGear()
    {
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sharedPref = (pm != null)
            ? pm.getSharedPreferences()
            : PreferenceManager.getDefaultSharedPreferences(_activity);
        return sharedPref.getBoolean("diffteethdoublegear", false);
    }
    
    public int getRatioPrecision()
    {
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sharedPref = (pm != null)
            ? pm.getSharedPreferences()
            : PreferenceManager.getDefaultSharedPreferences(_activity);
        return sharedPref.getInt("accuracyofratio", 1);
    }

    public int getMinTeethNumber()
    {
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sharedPref = (pm != null)
                ? pm.getSharedPreferences()
                : PreferenceManager.getDefaultSharedPreferences(_activity);
        return sharedPref.getInt("minteethnumber", 1);
    }

    public int getMaxTeethNumber()
    {
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sharedPref = (pm != null)
                ? pm.getSharedPreferences()
                : PreferenceManager.getDefaultSharedPreferences(_activity);
        return sharedPref.getInt("maxteethnumber", 1);
    }
}
