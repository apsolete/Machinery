package com.apsolete.machinery.calculation.changegears;

import java.util.Arrays;

public class CgResult
{
    public int Id;
    public double Ratio;
    //public double Pitch;
    public int[] Gears = new int[6];

    public CgResult(int id, double ratio, int[] gears)
    {
        Id = id;
        Ratio = ratio;
        //Pitch = pitch;
        Gears = Arrays.copyOf(gears, 6);
    }
}
