package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.CalculationPresenter;
import com.apsolete.machinery.common.G;

public final class ChangeGearsPresenter extends CalculationPresenter implements ChangeGearsContract.Presenter
{
    private final ChangeGearsContract.View _view;
    private boolean _oneSet;
    private String[] _gsValue = new String[7];
    private boolean[] _gsChecked = new boolean[7];

    public ChangeGearsPresenter(ChangeGearsContract.View view)
    {
        _view = view;
        _oneSet = true;
        _gsValue[0] = "20-30";
        _gsChecked[1] = true;
        _gsChecked[2] = true;

        _view.setPresenter(this);
    }

    @Override
    public void start()
    {
        super.start();
        _view.setOneGearsSet(_oneSet);
        if (_oneSet)
        {
            _view.setGearsSetEnableSet(G.Z1, false);
            _view.setGearsSetEnableSet(G.Z2, false);
            _view.setGearsSetEnableSet(G.Z3, false);
            _view.setGearsSetEnableSet(G.Z4, false);
            _view.setGearsSetEnableSet(G.Z5, false);
            _view.setGearsSetEnableSet(G.Z6, false);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEnabled(G.Z3, false);
            _view.setGearsSetEnabled(G.Z4, false);
            _view.setGearsSetEnabled(G.Z5, false);
            _view.setGearsSetEnabled(G.Z6, false);
        }
        else
        {
            _view.setGearsSetEnableSet(G.Z1, true);
            _view.setGearsSetEnableSet(G.Z2, true);
            _view.setGearsSetEnableSet(G.Z3, true);
            _view.setGearsSetEnableSet(G.Z4, true);
            _view.setGearsSetEnableSet(G.Z5, true);
            _view.setGearsSetEnableSet(G.Z6, true);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEnabled(G.Z3, false);
            _view.setGearsSetEnabled(G.Z4, false);
            _view.setGearsSetEnabled(G.Z5, false);
            _view.setGearsSetEnabled(G.Z6, false);
        }

        int set = 0;
        for (String val : _gsValue)
        {
            _view.setGearsSet(set, _gsValue[set]);
            set++;
        }
        set = 0;
        for (boolean bool : _gsChecked)
        {
            _view.setGearsSetChecked(set, _gsChecked[set]);
            set++;
        }
    }

    @Override
    public void setOneGearsSet(boolean oneSet)
    {
        _oneSet = oneSet;
        if (oneSet)
        {
            _view.setGearsSetEnabled(G.Z0, true);
            _view.setGearsSetEnableSet(G.Z0, true);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEnableSet(G.Z1, false);
            _view.setGearsSetChecked(G.Z1, true);

            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEnableSet(G.Z2, false);
            _view.setGearsSetChecked(G.Z2, true);

            _view.setGearsSetEnabled(G.Z3, true);
            _view.setGearsSetEnableSet(G.Z3, false);
            _view.setGearsSetChecked(G.Z3, false);

            _view.setGearsSetEnabled(G.Z4, true);
            _view.setGearsSetEnableSet(G.Z4, false);
            _view.setGearsSetChecked(G.Z4, false);

            _view.setGearsSetEnabled(G.Z5, true);
            _view.setGearsSetEnableSet(G.Z5, false);
            _view.setGearsSetChecked(G.Z5, false);

            _view.setGearsSetEnabled(G.Z6, true);
            _view.setGearsSetEnableSet(G.Z6, false);
            _view.setGearsSetChecked(G.Z6, false);
        }
        else
        {
            _view.setGearsSetEnabled(G.Z0, false);
            _view.setGearsSetEnableSet(G.Z0, false);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEnableSet(G.Z1, true);
            //_view.setGearsSetChecked(G.Z1, true);

            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEnableSet(G.Z2, true);
            //_view.setGearsSetChecked(G.Z2, true);

            _view.setGearsSetEnabled(G.Z3, false);
            _view.setGearsSetEnableSet(G.Z3, true);
            //_view.setGearsSetChecked(G.Z3, false);

            _view.setGearsSetEnabled(G.Z4, false);
            _view.setGearsSetEnableSet(G.Z4, true);
            //_view.setGearsSetChecked(G.Z4, false);

            _view.setGearsSetEnabled(G.Z5, false);
            _view.setGearsSetEnableSet(G.Z5, true);
            //_view.setGearsSetChecked(G.Z5, false);

            _view.setGearsSetEnabled(G.Z6, false);
            _view.setGearsSetEnableSet(G.Z6, true);
            //_view.setGearsSetChecked(G.Z6, false);
        }
    }

    @Override
    public void setGearsSet(int set, String valueStr)
    {
        _gsValue[set] = valueStr;
    }

    @Override
    public void setGearsSetChecked(int set, boolean checked)
    {
        _gsChecked[set] = checked;
    }
}
