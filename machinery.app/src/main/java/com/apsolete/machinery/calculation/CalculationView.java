package com.apsolete.machinery.calculation;
import com.apsolete.machinery.common.*;

public class CalculationView extends ContentBase implements Calculation.Contract.View
{
    public CalculationView(int type, int layout, int title)
    {
        super(layout, title);
        //_type = type;
    }

    @Override
    public void setPresenter(Calculation.Contract.Presenter presenter)
    {
        // TODO: Implement this method
    }

}
