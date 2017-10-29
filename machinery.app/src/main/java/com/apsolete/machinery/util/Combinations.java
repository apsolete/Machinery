package com.apsolete.machinery.util;

import java.util.*;

public class Combinations<T>
{
    private static boolean nextSet(List<Integer> set, int n, int kk)
    {
        int edge, j, i;
        edge = kk - 1;

        // find j in (k…n-1) where set[j] > a[edge]
        j = kk;
        while ((j < n) && (set.get(edge) >= set.get(j))) ++j;

        if (j < n)
            Collections.swap(set, edge, j);
        else
        {
            // reverse set[k] to set[n-1]
            for (i = kk, j = n - 1; i < j; i++, j--)
                Collections.swap(set, i, j);

            // find rightmost ascent to left of edge
            i = edge - 1;
            while (i >= 0 && set.get(i) >= set.get(i + 1)) --i;

            if (i < 0) return false; // no more permutations

            // find j in (n-1…i+1) where set[j] > set[i]
            j = n - 1;
            while (j > i && set.get(i) >= set.get(j)) --j;

            Collections.swap(set, i, j);

            // reverse set[i+1] to set[n-1]
            for (i = i + 1, j = n - 1; i < j; i++, j--)
                Collections.swap(set, i, j);
        }
        return true;
    }
    
    public static <T> List<List<T>> permutations(List<T> set, int k)
    {
        List<List<T>> result = new ArrayList<List<T>>();

        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < set.size(); i++)
            indexes.add(i);

        do
        {
            List<Integer> index = indexes.subList(0, k);
            List<T> perm = new ArrayList<T>();
            for (Integer i: index)
            {
                perm.add(set.get(i));
            }
            result.add(perm);
        } while (nextSet(indexes, set.size(), k));

        return result;
    }
    
    public static <T> List<List<T>> combinations(List<T> set, int k)
    {
        List<List<T>> results = new ArrayList<List<T>>();
        if (k == 0)
        {
            results.add(new ArrayList<T>());
            return results;
        }
        
        for (int i = 0; i < set.size(); i++)
        {
            T element = set.get(i);
            List<T> rest = set.subList(i+1, set.size());
            for (List<T> previous : combinations(rest, k-1))
            {
                previous.add(element);
                results.add(previous);
            }
        }
        
        return results;
    }
}
