package com.apsolete.machinery.util;

import java.util.*;

public class CompoundFraction extends Fraction
{
    protected List<Fraction> _fractions = new ArrayList<>();

    private CompoundFraction(Fraction fraction)
    {
        super(fraction.getNumerator(), fraction.getDenominator());
    }

    public CompoundFraction(List<Fraction> fractions)
    {
        this(Fractions.product(fractions));
        Collections.copy(_fractions, fractions);
    }
    
    public CompoundFraction(Fraction[] fractions)
    {
        this(Fractions.product(fractions));
        _fractions = Arrays.asList(fractions);
    }

    @Override
    public int compareTo(Fraction fraction)
    {
        if (fraction.getClass() != CompoundFraction.class)
            return super.compareTo(fraction);
        
        int comp = super.compareTo(fraction);
        if (comp == 0)
        {
            CompoundFraction compound = (CompoundFraction)fraction;
            if (_fractions.size() == compound._fractions.size())
            {
                for (int i = 0; i < _fractions.size(); i++)
                {
                    int c = _fractions.get(i).compareTo(compound._fractions.get(i));
                    if (c != 0)
                        return c;
                }
            }
            else
                return Integer.compare(_fractions.size(), compound._fractions.size());
        }
        return comp;
    }

    @Override
    public boolean equals(Object obj)
    {
        CompoundFraction fraction = (CompoundFraction)obj;
        return compareTo(fraction) == 0;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Fraction f = _fractions.get(0);
        sb.append(f.getNumerator());
        sb2.append(f.getDenominator());
        for (int i = 1; i < _fractions.size(); i++)
        {
            f = _fractions.get(i);
            sb.append(" * ").append(f.getNumerator());
            sb2.append(" * ").append(f.getDenominator());
        }
        sb.append(" / ").append(sb2).append(" = ")
            .append(getNumerator()).append(" / ").append(getDenominator());
        return sb.toString();
    }
    
}
