package com.apsolete.machinery.calculation.belting.pbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.G;

public final class PbeltFragment extends CalculationFragment<PbeltViewModel>
{
    public PbeltFragment()
    {
        super(G.PBELT, R.layout.view_pbelt, R.string.title_pbelt, PbeltViewModel.class);
    }
}
