package com.apsolete.machinery.activity.calculation;

import com.apsolete.machinery.activity.*;

public abstract class CalculationContent extends Content
{
    public CalculationContent(int layout, int title)
    {
        super(layout, title);
    }
    
    public abstract void save();
    public abstract void clear();
    public abstract void close();
    public abstract void setOptions();
    protected abstract void calculate();
}
