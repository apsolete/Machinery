package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.*;

public abstract class DesignContent extends ContentBase
{
    public static final int CHANGEGEARS = 0;
    public static final int GEARWHEELS = 1;
    public static final int GEARWHEELSEXT = 2;
    public static final int BELTS = 3;
    
    private int _type;
    
    public DesignContent(int type, int layout, int title)
    {
        super(layout, title);
        _type = type;
    }
    
    public int type()
    {
        return _type;
    }
    
    public abstract SettingsBase  getSettings();
    public abstract void save();
    public abstract void clear();
    public abstract boolean close();
    public abstract void setOptions();
    protected abstract void calculate();
}
