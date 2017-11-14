package com.apsolete.machinery.utils;

class Combination
{
    private int _n;
    private int _k;
    private int[] _index;
    private boolean _hasNext;

    public Combination(int n, int k)
    {
        _n = n;
        _k = k;
        _index = new int[k];
        reset();
    }
    
    private void reset()
    {
        _hasNext = true;
        for (int i = 0; i < _k; i++)
            _index[i] = i;
    }

    public boolean hasNext()
    {
        return _hasNext;
    }

    public int[] next()
    {
        if (!_hasNext)
            return null;
        int[] result = new int[_k];
        for (int i = 0; i < _k; i++)
            result[i] = _index[i];
        moveIndex();
        return result;
    }

    private void moveIndex()
    {
        int i = rightmostIndexBelowMax();
        if (i >= 0)
        {
            _index[i] = _index[i] + 1; 
            for (int j = i + 1; j < _k; j++)
                _index[j] = _index[j - 1] + 1;
        }
        else
            _hasNext = false;
    }

    private int rightmostIndexBelowMax()
    {
        for (int i = _k - 1; i >= 0; i--)
            if (_index[i] < _n - _k + i)
                return i;
        return -1;
    }
}
