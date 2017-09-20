package com.apsolete.machinery.util;

import java.util.*;

public class NumbersParser
{
    public static int[] getNumbers(String strNumbers)
    {
        if (strNumbers == null || strNumbers.isEmpty())
            return null;

        List<Integer> numList = getNumbersList(strNumbers);
        int[] numbers = new int[numList.size()];
        int i = 0;
        for (int n : numList)
        {
            numbers[i] = n;
            i++;
        }
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
}
