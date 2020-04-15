package com.apsolete.machinery.calculation.gearing.gearwheel;

import com.apsolete.machinery.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.calculation.*;
import com.apsolete.machinery.common.mvp.BaseContract;

public class GearWheelFragment extends CalculationFragment<GearWheelViewModel>
{
    public GearWheelFragment()
    {
        super(G.GEARWHEEL, R.layout.view_gearwheel, R.string.title_gearwheel, GearWheelViewModel.class);
    }
}
