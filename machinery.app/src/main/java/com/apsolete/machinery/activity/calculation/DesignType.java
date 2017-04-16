package com.apsolete.machinery.activity.calculation;

public enum DesignType
{
    ChangeGears(0),
    GearWheels(1),
    GearWheelsExtended(2);
    
    public final int Value;
    
    private DesignType(int value)
    {
        Value = value;
    }
}
