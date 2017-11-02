package com.apsolete.machinery.util;

import java.util.*;

public class CompoundNumber implements Comparable<CompoundNumber>
{
    private Long _value;
    private long[] _factors;

    public CompoundNumber(long value)
    {
        _value = value;
        _factors = Numbers.getFactors(value);
    }

    public CompoundNumber(long[] factors)
    {
        _value = Numbers.product(factors);
        _factors = Arrays.copyOf(factors, factors.length);
    }
    
    public long getValue()
    {
        return _value;
    }

    public long[] getFactors()
    {
        return _factors;
    }
    
    @Override
    public int compareTo(CompoundNumber number)
    {
        int compareValue = Long.compare(_value, number._value);

        if (compareValue == 0)
        {
            if (_factors.length == number._factors.length)
            {
                for (int i = 0; i < _factors.length; i++)
                {
                    int c = Long.compare(_factors[i], number._factors[i]);
                    if (c != 0)
                        return c;
                }
            }
            else
                return Integer.compare(_factors.length, number._factors.length);
        }

        return compareValue;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        CompoundNumber number = (CompoundNumber)obj;
        return compareTo(number) == 0;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(_factors[0]);
        for (int i = 1; i < _factors.length; i++)
        {
            sb.append(" * ").append(_factors[i]);
        }
        sb.append(" = ").append(_value);
        return sb.toString();
    }
}
