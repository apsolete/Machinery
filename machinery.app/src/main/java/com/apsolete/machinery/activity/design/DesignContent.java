package com.apsolete.machinery.activity.design;

import com.apsolete.machinery.activity.common.ContentBase;
import com.apsolete.machinery.activity.common.*;

public abstract class DesignContent extends ContentBase
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
    
    public abstract SettingsBase  getSettings();
    public abstract void save();
    public abstract void clear();
    public abstract boolean close();
    public abstract void setOptions();
    protected abstract void calculate();
}
