package com.apsolete.machinery.activity.calculation;

import com.apsolete.machinery.activity.*;

public abstract class ContentCalculation extends Content
{
    public ContentCalculation(int layout, int title)
    {
        super(layout, title);
    }
    
    public abstract void save();
    public abstract void clear();
    public abstract void close();
    public abstract void setOptions();
}
