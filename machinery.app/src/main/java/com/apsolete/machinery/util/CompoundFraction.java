package com.apsolete.machinery.util;

public class CompoundFraction extends Fraction
{
    private int _count;
    private Fraction[] _fractions;
    
    public CompoundFraction(Fraction fraction)
    {
        super(fraction.getNumerator(), fraction.getDenominator());
    }
    
    public void calculate(int count)
    {
        if (count != _count)
        {
            _count = count;
            _fractions = new Fraction[count];
            for (int i = 0; i < _count; i++)
            {
                _fractions[i] = null;
            }
        }
    }
}
