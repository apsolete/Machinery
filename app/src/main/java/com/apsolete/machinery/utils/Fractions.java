package com.apsolete.machinery.utils;

import java.util.*;

public class Fractions
{
    public static Fraction product(List<Fraction> fractions)
    {
        long num = 1;
        long den = 1;
        for (Fraction fraction: fractions)
        {
            num *= fraction.numerator();
            den *= fraction.denominator();
        }
        return Fraction.fromLong(num, den);
    }
    
    public static Fraction product(Fraction[] fractions)
    {
        return product(Arrays.asList(fractions));
    }
    
    public static List<CompoundFraction> getCompoundsOfTwo(Fraction fraction)
    {
        ArrayList<CompoundFraction> fractions = new ArrayList<>();
        List<CompoundNumber> nums = Numbers.getCompoundsOfTwo(fraction.numerator());
        List<CompoundNumber> dens = Numbers.getCompoundsOfTwo(fraction.denominator());
        for (CompoundNumber n: nums)
        {
            long[] nf = n.getFactors();
            for (CompoundNumber d: dens)
            {
                long[] df = d.getFactors();
                if (nf[0] == df[0] || nf[1] == df[1])
                    continue;
                Fraction f1 = Fraction.fromLong(nf[0], df[0]);
                Fraction f2 = Fraction.fromLong(nf[1], df[1]);
                CompoundFraction cf = new CompoundFraction(new Fraction[]{f1,f2});
                fractions.add(cf);
            }
        }
        return fractions;
    }
    
    public static List<CompoundFraction> getCompoundsOfThree(Fraction fraction)
    {
        ArrayList<CompoundFraction> fractions = new ArrayList<>();
        List<CompoundNumber> nums = Numbers.getCompoundsOfThree(fraction.numerator());
        List<CompoundNumber> dens = Numbers.getCompoundsOfThree(fraction.denominator());
        for (CompoundNumber n: nums)
        {
            long[] nf = n.getFactors();
            for (CompoundNumber d: dens)
            {
                long[] df = d.getFactors();
                if (nf[0] == df[0] || nf[1] == df[1] || nf[2] == df[2])
                    continue;
                Fraction f1 = Fraction.fromLong(nf[0], df[0]);
                Fraction f2 = Fraction.fromLong(nf[1], df[1]);
                Fraction f3 = Fraction.fromLong(nf[2], df[2]);
                CompoundFraction cf = new CompoundFraction(new Fraction[]{f1,f2,f3});
                fractions.add(cf);
            }
        }
        return fractions;
    }
    
    public static List<Fraction> getEquivalentFractions(Fraction initial, long[] numbers)
    {
        List<Fraction> fractions = new ArrayList<>();
        Arrays.sort(numbers);
        //long min=numbers[0];
        long max=numbers[numbers.length-1];
        long i = 1;
        boolean findfirst = true;
        while (true)
        {
            if (findfirst)
            {
                Fraction f = initial.getEquivalent(i);
                if (f.numerator() > max || f.denominator() > max)
                    break;
                else if (Arrays.binarySearch(numbers, f.numerator()) < 0 || Arrays.binarySearch(numbers, f.denominator()) < 0)
                    i++;
                else
                    findfirst = false;
            }
            else
            {
                Fraction f = initial.getEquivalent(i);
                if (Arrays.binarySearch(numbers, f.numerator()) >= 0 && Arrays.binarySearch(numbers, f.denominator()) >= 0)
                    fractions.add(f);
                else if (f.numerator() > max || f.denominator() > max)
                    break;
                i++;
            }
        }

        return fractions;
    }
    
    public static List<CompoundFraction> getCompoundFractions(long[] numbers, double ratio, double error)
    {
        List<List<Integer>> ns = Numbers.combinations(numbers.length, 2);
        List<CompoundFraction> compounds = new ArrayList<>();
        double loRatio = ratio - error;
        double hiRatio = ratio + error;
        
        for (int i = 0; i < ns.size() - 1; i++)
        {
            long n1 = numbers[ns.get(i).get(0)];
            long n2 = numbers[ns.get(i).get(1)];
            if (n1 == n2)
                continue;
            for (int j = i + 1; j < ns.size(); j++)
            {
                long d1 = numbers[ns.get(j).get(0)];
                long d2 = numbers[ns.get(j).get(1)];
                if (n1 == d1 || n1 == d2 || n2 == d1 || n2 == d2 || d1 == d2)
                    continue;
                if ((n1 + d1 - n2) < 15 || (n2 + d2 - d1) < 15)
                    continue;
                CompoundFraction cf = new CompoundFraction(new Fraction[]{new Fraction(n1, d1),new Fraction(n2, d2)});
                if (cf.toDouble() < loRatio || cf.toDouble() > hiRatio)
                    continue;
                compounds.add(cf);
            }
        }
        
        return compounds;
    }
}
