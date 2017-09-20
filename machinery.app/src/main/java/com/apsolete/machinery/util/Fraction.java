package com.apsolete.machinery.util;

import java.math.*;

public class Fraction
{
    private long numerator;
    private long denomenator;
    
    public Fraction(double decimal)
    {
        String[] parts = Double.toString(decimal).split("\\.");
        // denominator
        BigInteger den = BigInteger.TEN.pow(parts[1].length());
        // numerator
        BigInteger num = (new BigInteger(parts[0]).multiply(den)).add(new BigInteger(parts[1]));
        
        reduce(num, den);
    }
    
    public Fraction(long num, long den)
    {
        reduce(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }
    
    public Fraction(double num, double den)
    {
        String[] numParts = Double.toString(num).split("\\.");
        String[] denParts = Double.toString(den).split("\\.");
        int p = java.lang.Math.max(numParts[1].length(), denParts[1].length());
        BigInteger m = BigInteger.TEN.pow(p);
        BigInteger binum = (new BigInteger(numParts[0]).multiply(m)).add(new BigInteger(numParts[1]));
        BigInteger biden = (new BigInteger(denParts[0]).multiply(m)).add(new BigInteger(denParts[1]));
        reduce(binum, biden);
    }
    
    private void reduce(BigInteger num, BigInteger den)
    {
        BigInteger gcd = num.gcd(den);
        numerator = num.divide(gcd).longValue();
        denomenator = den.divide(gcd).longValue();
    }
    
    public Fraction multiply(long num)
    {
        return new Fraction(numerator*num, denomenator);
    }
    
    public Fraction multiply(Fraction fract)
    {
        return new Fraction(numerator*fract.numerator, denomenator*fract.denomenator);
    }
    
    public Fraction divide(long num)
    {
        return new Fraction(numerator, denomenator*num);
    }

    public Fraction divide(Fraction fract)
    {
        return new Fraction(numerator*fract.denomenator, denomenator*fract.numerator);
    }
    
    public double toDecimal()
    {
        return (double)numerator / (double)denomenator;
    }

    @Override
    public String toString()
    {
        return Long.toString(numerator) + " / " + Long.toString(denomenator);
    }
}
