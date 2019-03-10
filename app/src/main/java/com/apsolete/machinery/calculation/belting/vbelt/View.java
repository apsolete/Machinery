package com.apsolete.machinery.calculation.belting.vbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.SettingsBase;

public final class View extends CalculationView implements Contract.View
{
    public View()
    {
        super(G.VBELT, R.layout.view_vbelt, R.string.title_vbelt);
    }

    @Override
    public SettingsBase getSettings()
    {
        return null;
    }

    @Override
    public void showProgress(int percent)
    {

    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {

    }
}
