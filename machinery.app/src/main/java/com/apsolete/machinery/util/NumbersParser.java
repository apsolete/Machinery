package com.apsolete.machinery.util;

import java.util.*;

public class NumbersParser
{
    public static int[] getNumbers(String strNumbers)
    {
        if (strNumbers == null || strNumbers.isEmpty())
            return null;

        List<Integer> numList = getNumbersList(strNumbers);
        int[] numbers = toArray(numList);
        return numbers;
    }
    
    public static ArrayList<Integer> getNumbersList(String strNumbers)
    {
        String[] strs = strNumbers.split(" ");
        ArrayList<Integer> numbers = new ArrayList<>();

        for (String s : strs)
        {
            if (!s.isEmpty())
            {
                if (s.contains("-"))
                {
                    String[] range = s.split("-");
                    int r1 = Integer.parseInt(range[0]);
                    int r2 = Integer.parseInt(range[1]);
                    int start = Math.min(r1, r2);
                    int end = Math.max(r1, r2);
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
    
    public static ArrayList<Long> factorizeList(long number)
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
    
    public static long[] factorize(long number)
    {
        ArrayList<Long> factorsList = factorizeList(number);
        long[] factors = toArray(factorsList);

        return factors;
    }
    
    public static ArrayList<Long> dividersList(long number)
    {
        ArrayList<Long> dividers = new ArrayList<>();

        for (long i = 1; i <= (long)Math.sqrt(number); i++)
        {
            if (number % i == 0)
            {
                dividers.add(i);

                //для корня из x не существует парного делителя
                if (i * i != number)
                {
                    dividers.add(number / i);
                }
            }
        }
        
        Collections.sort(dividers);

        return dividers;
    }
    
    public static long[] dividers(long number)
    {
        ArrayList<Long> divList = dividersList(number);
        long[] div = toArray(divList);

        return div;
    }
    
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
}
