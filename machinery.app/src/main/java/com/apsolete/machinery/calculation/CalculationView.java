package com.apsolete.machinery.calculation;
import com.apsolete.machinery.common.*;

public abstract class CalculationView extends ContentBase
{
    private int _type;

    public CalculationView(int type, int layout, int title)
    {
        super(layout, title);
        _type = type;
    }

    public int type()
    {
        return _type;
    }
}
