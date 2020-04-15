package com.apsolete.machinery.calculation.gearing.geardrive;

import com.apsolete.machinery.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.calculation.*;

public class GearDriveFragment extends CalculationFragment<GearDriveViewModel>
{
    public GearDriveFragment()
    {
        super(G.GEARDRIVE, R.layout.view_geardrive, R.string.title_geardrive, GearDriveViewModel.class);
    }
}
