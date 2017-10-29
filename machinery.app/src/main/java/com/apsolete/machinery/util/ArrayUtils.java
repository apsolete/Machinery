package com.apsolete.machinery.util;

import java.util.*;

public class ArrayUtils
{
    public static int[] toArray(List<Integer> list)
    {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);

        return array;
    }

    public static long[] toArray(List<Long> list)
    {
        long[] array = new long[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);

        return array;
    }
    
    public static List<Integer> toList(int[] array)
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int n: array)
            list.add(n);
        return list;
    }
    
    public static List<Long> toList(long[] array)
    {
        ArrayList<Long> list = new ArrayList<>();
        for (long n: array)
            list.add(n);
        return list;
    }
}
