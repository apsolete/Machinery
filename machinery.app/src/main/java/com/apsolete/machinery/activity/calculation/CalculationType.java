package com.apsolete.machinery.activity.calculation;

public enum CalculationType
{
    ChangeGears(0),
    GearsCommon(1),
    GearsExtended(2);
    
    public final int Value;
    private CalculationType(int value)
    {
        Value = value;
    }
}
