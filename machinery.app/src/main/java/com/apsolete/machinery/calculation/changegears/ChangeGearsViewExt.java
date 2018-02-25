package com.apsolete.machinery.calculation.changegears;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.calculation.Calculation;
import com.apsolete.machinery.calculation.CalculationView;

public final class ChangeGearsViewExt extends CalculationView implements ChangeGearsContract.View
{
    private ChangeGearsContract.Presenter _presenter;

    private View _view;
    private CompoundButton _oneSetSwitch;
    private Spinner _calcTypeSpinner;
    private LinearLayout _threadPitchLayout;
    private EditText _threadPitchValue;
    private Spinner _threadUnitSpinner;
    private LinearLayout _screwPitchLayout;
    private EditText _screwPitchValue;
    private Spinner _screwUnitSpinner;
    private LinearLayout _gearRatioLayout;
    private CompoundButton _ratioAsFractionSwitch;
    private EditText _gearRatioValue;
    private EditText _gearRatioDenominator;
    private LinearLayout _gearRatioDenominatorLayout;
    private TextView _ratioResultText;
    private TextView _resFromNumberText;
    private TextView _resToNumberText;

    private ViewGroup _resultView;
    private CgSettings _settings;
    private CgCalculator _calculator;

    private final GearsSetView[] _gsViews = new GearsSetView[7];

    public ChangeGearsViewExt()
    {
        super(Calculation.CHANGEGEARS, R.layout.content_changegears, R.string.title_change_gears_design);
    }

    @Override
    public void setPresenter(ChangeGearsContract.Presenter presenter)
    {
        _presenter = presenter;
    }

    @Override
    public void setOneGearsSet(boolean oneSet)
    {
        _oneSetSwitch.setChecked(oneSet);
    }

    @Override
    public void setGearsSet(int set, String gearsStr)
    {
        _gsViews[set].setText(gearsStr);
    }

    @Override
    public void setGearsSetChecked(int set, boolean checked)
    {
        _gsViews[set].setChecked(checked);
    }

    @Override
    public void setGearsSetEnabled(int set, boolean enabled)
    {
        _gsViews[set].setEnabled(enabled);
    }

    @Override
    public void setGearsSetEnableSet(int set, boolean enable)
    {
        _gsViews[set].enableSet(enable);
    }

    @Override
    public void setCalculationMode(int mode)
    {
        _calcTypeSpinner.setSelection(mode);
    }

    @Override
    public void setThreadPitch(String valueStr)
    {
        _threadPitchValue.setText(valueStr);
    }

    @Override
    public void showThreadPitch(boolean visible)
    {
        _threadPitchLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLeadscrewPitch(String valueStr)
    {
        _screwPitchValue.setText(valueStr);
    }

    @Override
    public void showLeadscrewPitch(boolean visible)
    {
        _screwPitchLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRatio(String valueStr)
    {

    }

    @Override
    public void showRatio(boolean visible)
    {

    }

    @Override
    public void setRatioNumerator(String valueStr)
    {

    }

    @Override
    public void showRatioNumerator(boolean visible)
    {

    }

    @Override
    public void setRatioDenominator(String valueStr)
    {

    }

    @Override
    public void showRatioDenominator(boolean visible)
    {

    }

    @Override
    public void setFormattedRatio(String ratioStr)
    {

    }

    @Override
    public void showFormattedRatio(boolean visible)
    {

    }

    @Override
    public void setFirstResultNumber(String valuestr)
    {

    }

    @Override
    public void setLastResultNumber(String valueStr)
    {

    }

    @Override
    public void setResultItem(CgResult result)
    {

    }

    @Override
    public void clearResults()
    {

    }

    @Override
    public void showError(String error)
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (_view != null)
            return _view;

        _view = super.onCreateView(inflater, container, savedInstanceState);
        assert _view != null;

        _oneSetSwitch = (CompoundButton)_view.findViewById(R.id.oneSetForAllGears);
        _oneSetSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean _isOneSet = ((CompoundButton)view).isChecked();
                _presenter.setOneGearsSet(_isOneSet);
            }
        });

        _gsViews[G.Z0] = new GearsSetView(G.Z0, _presenter, _view, R.id.z0Set, R.id.z0Gears, 0, null);
        _gsViews[G.Z1] = new GearsSetView(G.Z1, _presenter, _view, R.id.z1Set, R.id.z1Gears, R.id.z1Select, null);
        _gsViews[G.Z2] = new GearsSetView(G.Z2, _presenter, _view, R.id.z2Set, R.id.z2Gears, R.id.z2Select, null);
        _gsViews[G.Z3] = new GearsSetView(G.Z3, _presenter, _view, R.id.z3Set, R.id.z3Gears, R.id.z3Select, null);
        _gsViews[G.Z4] = new GearsSetView(G.Z4, _presenter, _view, R.id.z4Set, R.id.z4Gears, R.id.z4Select, null);
        _gsViews[G.Z5] = new GearsSetView(G.Z5, _presenter, _view, R.id.z5Set, R.id.z5Gears, R.id.z5Select, null);
        _gsViews[G.Z6] = new GearsSetView(G.Z6, _presenter, _view, R.id.z6Set, R.id.z6Gears, R.id.z6Select, null);

        _calcTypeSpinner = (Spinner)_view.findViewById(R.id.calcTypeSpinner);

        _threadPitchLayout = (LinearLayout)_view.findViewById(R.id.threadPitchLayout);
        _threadPitchValue = (EditText)_view.findViewById(R.id.threadPitchValue);
        _threadUnitSpinner = (Spinner)_view.findViewById(R.id.threadUnitSpinner);

        _screwPitchLayout = (LinearLayout)_view.findViewById(R.id.screwPitchLayout);
        _screwPitchValue = (EditText)_view.findViewById(R.id.screwPitchValue);
        _screwUnitSpinner = (Spinner)_view.findViewById(R.id.screwUnitSpinner);

        _gearRatioLayout = (LinearLayout)_view.findViewById(R.id.gearRatioLayout);
        _gearRatioValue = (EditText)_view.findViewById(R.id.gearRatioValue);
        _gearRatioDenominator = (EditText)_view.findViewById(R.id.gearRatioDenominator);
        _gearRatioDenominatorLayout = (LinearLayout)_view.findViewById(R.id.gearRatioDenominatorLayout);
        _ratioResultText = (TextView)_view.findViewById(R.id.ratioResultText);

        return _view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (_presenter != null) _presenter.start();
    }
}
