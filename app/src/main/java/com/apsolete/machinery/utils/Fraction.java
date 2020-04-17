package com.apsolete.machinery.utils;

import java.math.BigInteger;

public class Fraction implements Comparable<Fraction>
{
    private long _numerator;
    private long _denominator;
    
    public static final Fraction ZERO = new Fraction(0);
    public static final Fraction ONE = new Fraction(1);
    
    private Fraction(double decimal)
    {
        String[] parts = Double.toString(decimal).split("\\.");
        // denominator
        BigInteger den = BigInteger.TEN.pow(parts[1].length());
        // numerator
        BigInteger num = (new BigInteger(parts[0]).multiply(den)).add(new BigInteger(parts[1]));
        
        setReduced(num, den);
    }

    private Fraction(double num, double den)
    {
        String[] numParts = Double.toString(num).split("\\.");
        String[] denParts = Double.toString(den).split("\\.");
        int p = java.lang.Math.max(numParts[1].length(), denParts[1].length());
        BigInteger m = BigInteger.TEN.pow(p);
        BigInteger binum = (new BigInteger(numParts[0]).multiply(m)).add(new BigInteger(numParts[1]));
        BigInteger biden = (new BigInteger(denParts[0]).multiply(m)).add(new BigInteger(denParts[1]));

        setReduced(binum, biden);
    }

    private Fraction(long num)
    {
        this(num, 1, false);
    }

    protected Fraction(long num, long den)
    {
        this(num, den, false);
    }

    private Fraction(long num, long den, boolean reduced)
    {
        if (reduced)
            setReduced(BigInteger.valueOf(num), BigInteger.valueOf(den));
        else
        {
            _numerator = num;
            _denominator = den;
        }
    }

    public static Fraction fromLong(long value)
    {
        return new Fraction(value);
    }

    public static Fraction fromLong(long num, long den)
    {
        return new Fraction(num, den);
    }

    public static Fraction fromDouble(double value)
    {
        return new Fraction(value);
    }

    public static Fraction fromDouble(double num, double den)
    {
        return new Fraction(num, den);
    }

    protected void setNumerator(long numerator)
    {
        _numerator = numerator;
    }

    public long numerator()
    {
        return _numerator;
    }

    protected void setDenominator(long denominator)
    {
        _denominator = denominator;
    }

    public long denominator()
    {
        return _denominator;
    }

    private void setReduced(BigInteger num, BigInteger den)
    {
        BigInteger gcd = num.gcd(den);
        _numerator = num.divide(gcd).longValue();
        _denominator = den.divide(gcd).longValue();
    }

    public Fraction reduced()
    {
        return new Fraction(_numerator, _denominator, true);
    }

//    public static Fraction reduced(long num, long den)
//    {
//        return new Fraction(num, den, true);
//    }
//
//    public static Fraction reduced(Fraction fraction)
//    {
//        return fraction.getReduced();
//    }
    
    public Fraction add(long num)
    {
        return new Fraction(_numerator + num * _denominator, _denominator);
    }

    public Fraction add(Fraction fract)
    {
        return new Fraction(_numerator * fract._denominator + fract._numerator * _denominator, _denominator * fract._denominator);
    }
    
    public Fraction multiply(long num)
    {
        return new Fraction(_numerator * num, _denominator);
    }
    
    public Fraction multiply(Fraction fract)
    {
        return new Fraction(_numerator * fract._numerator, _denominator * fract._denominator);
    }
    
    public Fraction divide(long num)
    {
        return new Fraction(_numerator, _denominator * num);
    }

    public Fraction divide(Fraction fract)
    {
        return new Fraction(_numerator * fract._denominator, _denominator * fract._numerator);
    }

    public double toDouble()
    {
        return (double) _numerator / (double) _denominator;
    }

    public Fraction getEquivalent(long factor)
    {
        return new Fraction(_numerator * factor, _denominator * factor);
    }
    
    public boolean isOne()
    {
        Fraction fr = reduced();
        return fr.numerator() == 1 && fr.denominator() == 1;
    }

    @Override
    public String toString()
    {
        return new StringBuilder()
            .append(_numerator)
            .append(" / ")
            .append(_denominator)
            .toString();
    }

    @Override
    public int compareTo(Fraction fraction)
    {
        Fraction f1 = reduced();
        Fraction f2 = fraction.reduced();
        
        if (f1._denominator == f2._denominator)
            return Long.compare(f1._numerator, f2._numerator);

        if (f1._numerator == f2._numerator)
            return Long.compare(f2._denominator, f1._denominator);

        BigInteger bi1 = BigInteger.valueOf(f1._numerator).multiply(BigInteger.valueOf(f2._denominator));
        BigInteger bi2 = BigInteger.valueOf(f1._denominator).multiply(BigInteger.valueOf(f2._numerator));
        return bi1.compareTo(bi2);
    }

    @Override
    public boolean equals(Object obj)
    {
        Fraction fraction = (Fraction)obj;
        return compareTo(fraction) == 0;
    }
}
