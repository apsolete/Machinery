package com.apsolete.machinery.activity.design.changegears;

import android.content.*;
import android.support.v7.preference.*;
import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.common.*;

import com.apsolete.machinery.activity.R;

public class ChangeGearsSettings extends SettingsBase
{
    public interface OnChangeListener
    {
        void onDiffTeethGearingChanged(boolean newValue);
        void onDiffTeethDoubleGearChanged(boolean newValue);
        void onRatioPrecisionChanged(int newValue);
    }
    
    private OnChangeListener _listener;
    
    public ChangeGearsSettings(Context context)
    {
        super(context, R.xml.settings_changegears, R.string.title_change_gears_design);
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
    
    public int getRatioPrecision()
    {
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sharedPref = (pm != null)
            ? pm.getSharedPreferences()
            : PreferenceManager.getDefaultSharedPreferences(_context);
        return sharedPref.getInt("accuracyofratio", 1);
    }
}
