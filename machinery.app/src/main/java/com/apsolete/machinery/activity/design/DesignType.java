package com.apsolete.machinery.activity.design;

public enum DesignType
{
    ChangeGears(0),
    GearWheels(1),
    GearWheelsExt(2),
    Belt(3);
    
    public final int Value;
    
    private DesignType(int value)
    {
        Value = value;
    }
}
