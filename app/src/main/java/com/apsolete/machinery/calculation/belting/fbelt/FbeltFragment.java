package com.apsolete.machinery.calculation.belting.fbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.G;

public final class FbeltFragment extends CalculationFragment<FbeltViewModel>
{
    public FbeltFragment()
    {
        super(G.FBELT, R.layout.view_fbelt, R.string.title_fbelt, FbeltViewModel.class);
    }
}
