package com.apsolete.machinery.activity.calculation;

import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.util.ContentFragment;

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
    
    public abstract void save();
    public abstract void clear();
    public abstract void close();
    public abstract void setOptions();
    protected abstract void calculate();
}
