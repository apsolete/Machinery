package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.utils.*;

public enum ThreadPitchUnit
{
    mm,
    TPI;

    public double toMm(double pitch)
    {
        if (this == mm)
            return pitch;
        return 25.4 / pitch;
    }
    
    public Fraction toMmFraction(double pitch)
    {
        if (this == mm)
            return new Fraction(pitch, 1);
        return new Fraction(25.4, pitch);
    }

    public double toTpi(double pitch)
    {
        if (this == TPI)
            return pitch;
        return 25.4 / pitch;
    }
    
    public Fraction toTpiFraction(double pitch)
    {
        if (this == TPI)
            return new Fraction(pitch, 1);
        return new Fraction(25.4, pitch);
    }
}
