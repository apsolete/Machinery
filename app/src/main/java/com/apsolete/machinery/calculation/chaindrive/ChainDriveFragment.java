package com.apsolete.machinery.calculation.chaindrive;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.SettingsBase;

public final class ChainDriveFragment extends CalculationFragment<ChainDriveViewModel>
{
    public ChainDriveFragment()
    {
        super(G.CHAINDRIVE, R.layout.view_chaindrive, R.string.title_chaindrive, ChainDriveViewModel.class);
    }
}
