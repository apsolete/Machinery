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
        }

        return compareValue;
    }
}
