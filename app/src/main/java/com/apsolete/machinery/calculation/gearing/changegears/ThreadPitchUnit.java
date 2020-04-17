package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.*;

public enum ThreadPitchUnit
{
    mm,
    TPI;

    public double toMm(double pitch)
    {
        if (this == mm)
            return pitch;
        return G.INCH / pitch;
    }
    
    public Fraction toMmFraction(double pitch)
    {
        if (this == mm)
            return Fraction.fromDouble(pitch);
        return Fraction.fromDouble(G.INCH, pitch);
    }

    public double toTpi(double pitch)
    {
        if (this == TPI)
            return pitch;
        return G.INCH / pitch;
    }
    
    public Fraction toTpiFraction(double pitch)
    {
        if (this == TPI)
            return Fraction.fromDouble(pitch);
        return Fraction.fromDouble(G.INCH, pitch);
    }

    public static int toInt(ThreadPitchUnit unit)
    {
        return unit.ordinal();
    }

    public static ThreadPitchUnit fromInt(int unit)
    {
        ThreadPitchUnit[] units = ThreadPitchUnit.values();
        if (unit > units.length)
            unit = units.length - 1;
        return units[unit];
    }
}
