package com.apsolete.machinery.calculation.belting.tbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.G;

public final class TbeltFragment extends CalculationFragment<TbeltViewModel>
{
    public TbeltFragment()
    {
        super(G.TBELT, R.layout.view_tbelt, R.string.title_tbelt, TbeltViewModel.class);
    }
}
