package com.apsolete.machinery.activity.design.changegears;
import com.apsolete.machinery.util.*;

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
    
    public Fraction toMmFraction(double pitch)
    {
        if (this == Mm)
            return new Fraction(pitch, 1);
        return new Fraction(25.4, pitch);
    }

    public double toTpi(double pitch)
    {
        if (this == Tpi)
            return pitch;
        return 25.4 / pitch;
    }
    
    public Fraction toTpiFraction(double pitch)
    {
        if (this == Tpi)
            return new Fraction(pitch, 1);
        return new Fraction(25.4, pitch);
    }
}
