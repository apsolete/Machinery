package com.apsolete.machinery.common;

import android.view.View;

public final class G
{
    public static final double INCH = 25.4;
    public static final int CHANGEGEARS = 1;
    public static final int GEARWHEEL   = 2;
    public static final int GEARDRIVE   = 3;
    public static final int FBELT       = 4;
    public static final int VBELT       = 5;
    public static final int PBELT       = 6;
    public static final int TBELT       = 7;
    public static final int CHAINDRIVE  = 8;

    public static final int Z0 = 0;
    public static final int Z1 = 1;
    public static final int Z2 = 2;
    public static final int Z3 = 3;
    public static final int Z4 = 4;
    public static final int Z5 = 5;
    public static final int Z6 = 6;
    //public static final int Z7 = 7;
    //public static final int Z8 = 8;
    //public static final int Z9 = 9;
    public static final int ZMIN = Z0;
    public static final int ZMAX = Z6;
    
    public static final int CHG_RATIOS_BY_GEARS = 0;
    public static final int CHG_THREAD_BY_GEARS = 1;
    public static final int CHG_GEARS_BY_RATIO = 2;
    public static final int CHG_GEARS_BY_THREAD = 3;

    public static final String ChangeGearsId = "ChangeGearsId";

    public static final int toVisibility(boolean visible)
    {
        return visible ? View.VISIBLE : View.GONE;
    }
}
