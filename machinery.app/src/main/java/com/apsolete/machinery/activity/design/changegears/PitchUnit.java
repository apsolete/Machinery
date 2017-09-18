package com.apsolete.machinery.activity.design.changegears;

public enum PitchUnit
{
    Mm,
    Tpi;

    public double ToMm(double pitch)
    {
        if (this == Mm)
            return pitch;
        return 25.4 / pitch;
    }

    public double ToTpi(double pitch)
    {
        if (this == Tpi)
            return pitch;
        return 25.4 / pitch;
    }
}
