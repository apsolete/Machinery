package com.apsolete.machinery.calculation.belting.vbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.G;

public final class VbeltFragment extends CalculationFragment<VbeltViewModel>
{
    public VbeltFragment()
    {
        super(G.VBELT, R.layout.view_vbelt, R.string.title_vbelt, VbeltViewModel.class);
    }
}
