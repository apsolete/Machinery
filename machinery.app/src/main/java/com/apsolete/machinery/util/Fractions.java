package com.apsolete.machinery.util;

import java.util.*;

public class Fractions
{
    public static Fraction product(List<Fraction> fractions)
    {
        long num = 1;
        long den = 1;
        for (Fraction fraction: fractions)
        {
            num *= fraction.getNumerator();
            den *= fraction.getDenominator();
        }
        return new Fraction(num, den);
    }
    
    public static Fraction product(Fraction[] fractions)
    {
        return product(Arrays.asList(fractions));
    }
    
    public static CompoundFraction compoundOfTwo(Fraction fraction)
    {
        ArrayList<Fraction> fractions = new ArrayList<>();
        return new CompoundFraction(fractions);
    }
    
    public static CompoundFraction compoundOfThree(Fraction fraction)
    {
        ArrayList<Fraction> fractions = new ArrayList<>();
        return new CompoundFraction(fractions);
    }
}
