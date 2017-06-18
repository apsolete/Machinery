package com.apsolete.machinery.activity.design;

import com.apsolete.machinery.activity.common.ContentFragment;
import com.apsolete.machinery.activity.common.*;

public abstract class DesignContent extends ContentFragment
{
    private DesignType _type;
    
    public DesignContent(DesignType type, int layout, int title)
    {
        super(layout, title);
        _type = type;
    }
    
    public DesignType type()
    {
        return _type;
    }
    
    public abstract SettingsFragment  getSettings();
    public abstract void save();
    public abstract void clear();
    public abstract void close();
    public abstract void setOptions();
    protected abstract void calculate();
}
