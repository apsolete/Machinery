package com.apsolete.machinery.utils;

import java.util.*;

public class Numbers
{
    public static int[] getNumbers(String strNumbers)
    {
        if (strNumbers == null || strNumbers.isEmpty())
            return null;

        List<Integer> numList = getNumbersList(strNumbers);
        int[] numbers = ArrayUtils.toArrayInt(numList);
        return numbers;
    }

    public static ArrayList<Integer> getNumbersList(String strNumbers)
    {
        String str = strNumbers.replaceAll(" +", " ")
            .replaceAll(" -", "-").replaceAll("- ", "-").replaceAll("-+", "-");
        String[] strs = str.split(" ");
        ArrayList<Integer> numbers = new ArrayList<>();

        for (String s : strs)
        {
            if (!s.isEmpty())
            {
                if (s.contains("-"))
                {
                    String[] range = s.split("-");
                    int start = Integer.parseInt(range[0]);
                    int end = Integer.parseInt(range[0]);
                    for (int i = 1; i < range.length; i++)
                    {
                        int r = Integer.parseInt(range[i]);
                        start = Math.min(start, r);
                        end = Math.max(end, r);
                    }
                    
                    while (start <= end)
                    {
                        numbers.add(start);
                        start++;
                    }
                }
                else
                {
                    int n = Integer.parseInt(s);
                    numbers.add(Math.abs(n));
                }
            }
        }
        return numbers;
    }

    public static String getString(ArrayList<Integer> numbers)
    {
        if (numbers == null || numbers.size() == 0)
            return null;

        ArrayList<Integer> nums = new ArrayList<>(numbers);
        Collections.sort(nums);

        String text = "";
        int n1 = 0;
        int n2 = 0;

        for (int n : nums)
        {
            if (n1 != 0)
            {
                if ((n2 == 0 && n == n1 + 1) || (n2 != 0 && n == n2 + 1))
                {
                    n2 = n;
                    continue;
                }
                if (n2 != 0)
                {
                    text += (n2 - n1 > 1) ? (n1 + "-" + n2 + " ") : (n1 + " " + n2 + " ");
                    n2 = 0;
                }
                else if (n2 == 0)
                {
                    text += n1 + " ";
                }
            }

            if (n2 == 0)
                n1 = n;
        }

        if (n2 == 0)
            text += n1;
        else if (n2 - n1 > 1)
            text += n1 + "-" + n2;
        else
            text += n1 + " " + n2;

        return text;
    }

    public static String getString(int[] numbers)
    {
        if (numbers == null)
            return null;

        ArrayList<Integer> nums = new ArrayList<>();
        for (int n: numbers) nums.add(n);
        return getString(nums);
    }
    
    public static List<Long> getFactorsList(long number)
    {
        long n = number;
        ArrayList<Long> factors = new ArrayList<>();
        for (long i = 2; i <= (long)Math.sqrt(n); i++)
        {
            while (n % i == 0)
            {
                factors.add(i);
                n /= i;
            }
        }

        if (n != 1)
            factors.add(n);

        return factors;
    }

    public static long[] getFactors(long number)
    {
        List<Long> factors = getFactorsList(number);
        long[] factorsArray = ArrayUtils.toArrayLong(factors);
        return factorsArray;
    }

    public static ArrayList<Long> getDividersList(long number)
    {
        ArrayList<Long> dividers = new ArrayList<>();

        for (long i = 1; i <= (long)Math.sqrt(number); i++)
        {
            if (number % i == 0)
            {
                dividers.add(i);
                //для корня из x не существует парного делителя
                if (i * i != number)
                    dividers.add(number / i);
            }
        }

        Collections.sort(dividers);

        return dividers;
    }

    public static long[] getDividers(long number)
    {
        ArrayList<Long> dividersList = getDividersList(number);
        long[] dividers = ArrayUtils.toArrayLong(dividersList);
        return dividers;
    }

    public static long product(long[] factors)
    {
        long result = 1;
        for (long f: factors)
            result *= f;
        return result;
    }
    
    public static long product(List<Long> factors)
    {
        long result = 1;
        for (long f: factors)
            result *= f;
        return result;
    }
    
    public static CompoundNumber compound(long f1, long f2)
    {
        return new CompoundNumber(new long[] {f1, f2});
    }
    
    public static CompoundNumber compound(long f1, long f2, long f3)
    {
        return new CompoundNumber(new long[] {f1, f2, f3});
    }
    
    public static List<CompoundNumber> getCompoundsOfTwo(long number)
    {
        List<CompoundNumber> compoundNumbers = new ArrayList<>();
        long[] dividers = getDividers(number);
        for (int i = 0; i < dividers.length; i++)
        {
            compoundNumbers.add(compound(dividers[i], number / dividers[i]));
        }
        return compoundNumbers;
    }
    
    public static List<CompoundNumber> getCompoundsOfThree(long number)
    {
        List<CompoundNumber> numbers = new ArrayList<>();
        long[] factors = getFactors(number);
        if (factors.length == 1)
        {
            numbers.add(compound(1L, 1L, factors[0]));
            numbers.add(compound(1L, factors[0], 1L));
            numbers.add(compound(factors[0], 1L, 1L));
        }
        else if (factors.length == 2)
        {
            numbers.add(compound(1L, factors[0], factors[1]));
            numbers.add(compound(factors[0], 1L, factors[1]));
            numbers.add(compound(factors[0], factors[1], 1L));
            numbers.add(compound(1L, factors[1], factors[0]));
            numbers.add(compound(factors[1], 1L, factors[0]));
            numbers.add(compound(factors[1], factors[0], 1L));
            numbers.add(compound(1L, 1L, factors[0] * factors[1]));
            numbers.add(compound(1L, factors[0] * factors[1], 1L));
            numbers.add(compound(factors[0] * factors[1], 1L, 1L));
        }
        else
        {
            long[] values = {1L, 1L, number};
            List<List<Integer>> indexes = permutations(3);
            for (List<Integer> index: indexes)
            {
                CompoundNumber item = compound(values[index.get(0)], values[index.get(1)], values[index.get(2)]);
                if (numbers.size() > 0)
                {
                    if (!numbers.contains(item))
                        numbers.add(item);
                }
                else
                    numbers.add(item);
            }
            List<Long> factorsList = ArrayUtils.toList(factors);
            int fcount = factorsList.size();
            for (int i = 1; i <= fcount; i++)
            {
                List<Long> n1s = factorsList.subList(0, i);
                long n1 = product(n1s);

                List<Long> rest = factorsList.subList(i, fcount);
                for (int j = 1; j <= rest.size(); j++)
                {
                    List<Long> n2s = rest.subList(0, j);
                    long n2 = product(n2s);
                    long n3 = number / (n1 * n2);
                    values = new long[]{ n1, n2, n3 };
                    for (List<Integer> index: indexes)
                    {
                        CompoundNumber item = compound(values[index.get(0)], values[index.get(1)], values[index.get(2)]);
                        if (!numbers.isEmpty())
                        {
                            if (!numbers.contains(item))
                                numbers.add(item);
                        }
                        else
                            numbers.add(item);
                    }
                }
            }
        }

        return numbers;
    }

    public static List<List<Integer>> combinations(int n, int k)
    {
        Combination c = new Combination(n,k);
        List<List<Integer>> combinations = new ArrayList<List<Integer>>();
        while (c.hasNext())
        {
            int[] comb = c.next();
            combinations.add(ArrayUtils.toList(comb));
        }
        return combinations;
    }

    public static List<List<Integer>> permutations(int n)
    {
        Permutation p = new Permutation(n);
        List<List<Integer>> permutations = new ArrayList<List<Integer>>();
        while (p.hasNext())
        {
            int[] comb = p.next();
            permutations.add(ArrayUtils.toList(comb));
        }
        return permutations;
    }
}
