package com.apsolete.machinery.utils;

public class Permutation
{
    private int[] array;
    private boolean firstReady = false;

    public Permutation(int n)
    {
        array = new int[n];
        reset();
    }

    public void reset()
    {
        firstReady = false;
        for (int i = 0; i < array.length; i++)
            array[i] = i;
    }

    public boolean hasNext()
    {
        boolean end = firstReady;
        for (int i = 1; i < array.length; i++)
        {
            end = end && array[i] < array[i - 1];
        }
        return !end;
    }

    public int[] next()
    {
        if (!firstReady)
        {
            firstReady = true;
            return array;
        }

        int temp;
        int j = array.length - 2;
        int k = array.length - 1;

        // Find largest index j with a[j] < a[j+1]

        for (;array[j] > array[j + 1];) j--;

        // Find index k such that a[k] is smallest integer
        // greater than a[j] to the right of a[j]

        for (;array[j] > array[k];) k--;

        // Interchange a[j] and a[k]

        temp = array[k];
        array[k] = array[j];
        array[j] = temp;

        // Put tail end of permutation after jth position in increasing order

        int r = array.length - 1;
        int s = j + 1;

        while (r > s)
        {
            temp = array[s];
            array[s++] = array[r];
            array[r--] = temp;
        }

        return array;
    }
}
