package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.CalculationPresenter;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.Fraction;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class ChangeGearsPresenter extends CalculationPresenter implements ChangeGearsContract.Presenter
{
    private final ChangeGearsContract.View _view;
    private boolean _oneSet;
    private String[] _gsValue = new String[7];
    private boolean[] _gsChecked = new boolean[7];
    private int _calcType;
    private double _ratio;
    private boolean _ratioAsFraction;
    private ThreadPitchUnit _threadPitchUnit;
    private ThreadPitchUnit _leadscrewPitchUnit;
    private double _leadscrewPitch;
    private double _threadPitch;
    private double _ratioNumerator;
    private double _ratioDenominator;
    private DecimalFormat _ratioFormat;

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
        //super.start();
        _view.setOneGearsSet(_oneSet);
        if (_oneSet)
        {
            _view.setGearsSetEditable(G.Z1, false);
            _view.setGearsSetEditable(G.Z2, false);
            _view.setGearsSetEditable(G.Z3, false);
            _view.setGearsSetEditable(G.Z4, false);
            _view.setGearsSetEditable(G.Z5, false);
            _view.setGearsSetEditable(G.Z6, false);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEnabled(G.Z3, false);
            _view.setGearsSetEnabled(G.Z4, false);
            _view.setGearsSetEnabled(G.Z5, false);
            _view.setGearsSetEnabled(G.Z6, false);
        }
        else
        {
            _view.setGearsSetEditable(G.Z1, true);
            _view.setGearsSetEditable(G.Z2, true);
            _view.setGearsSetEditable(G.Z3, true);
            _view.setGearsSetEditable(G.Z4, true);
            _view.setGearsSetEditable(G.Z5, true);
            _view.setGearsSetEditable(G.Z6, true);

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
        int lastChecked = -1;
        for (boolean bool : _gsChecked)
        {
            if (_gsChecked[set])
                lastChecked = set;
            _view.setGearsSetChecked(set, _gsChecked[set]);
            set++;
        }
        if (lastChecked < G.Z6)
            _view.setGearsSetEnabled(lastChecked+1, true);

        _view.setCalculationMode(0);
        setCalculationMode(G.RATIOS_BY_GEARS);
    }

    @Override
    public void stop()
    {
    }

    @Override
    public void save()
    {

    }

    @Override
    public void clear()
    {

    }

    @Override
    public void calculate()
    {

    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public void setOneGearsSet(boolean oneSet)
    {
        _oneSet = oneSet;
        if (oneSet)
        {
            _view.setGearsSetEnabled(G.Z0, true);
            _view.setGearsSetEditable(G.Z0, true);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEditable(G.Z1, false);
            _view.setGearsSetChecked(G.Z1, true);

            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEditable(G.Z2, false);
            _view.setGearsSetChecked(G.Z2, true);

            _view.setGearsSetEnabled(G.Z3, true);
            _view.setGearsSetEditable(G.Z3, false);
            _view.setGearsSetChecked(G.Z3, false);

            _view.setGearsSetEnabled(G.Z4, true);
            _view.setGearsSetEditable(G.Z4, false);
            _view.setGearsSetChecked(G.Z4, false);

            _view.setGearsSetEnabled(G.Z5, true);
            _view.setGearsSetEditable(G.Z5, false);
            _view.setGearsSetChecked(G.Z5, false);

            _view.setGearsSetEnabled(G.Z6, true);
            _view.setGearsSetEditable(G.Z6, false);
            _view.setGearsSetChecked(G.Z6, false);
        }
        else
        {
            _view.setGearsSetEnabled(G.Z0, false);
            _view.setGearsSetEditable(G.Z0, false);

            _view.setGearsSetEnabled(G.Z1, true);
            _view.setGearsSetEditable(G.Z1, true);
            //_view.setGearsSetChecked(G.Z1, true);

            _view.setGearsSetEnabled(G.Z2, true);
            _view.setGearsSetEditable(G.Z2, true);
            //_view.setGearsSetChecked(G.Z2, true);

            _view.setGearsSetEnabled(G.Z3, false);
            _view.setGearsSetEditable(G.Z3, true);
            //_view.setGearsSetChecked(G.Z3, false);

            _view.setGearsSetEnabled(G.Z4, false);
            _view.setGearsSetEditable(G.Z4, true);
            //_view.setGearsSetChecked(G.Z4, false);

            _view.setGearsSetEnabled(G.Z5, false);
            _view.setGearsSetEditable(G.Z5, true);
            //_view.setGearsSetChecked(G.Z5, false);

            _view.setGearsSetEnabled(G.Z6, false);
            _view.setGearsSetEditable(G.Z6, true);
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
        set++;
        if (set > G.Z6)
            return;
        if (checked)
            _view.setGearsSetEnabled(set, true);
        else
            for (; set <= G.Z6; set++)
            {
                _view.setGearsSetChecked(set, false);
                _view.setGearsSetEnabled(set, false);
            }
    }

    @Override
    public void setCalculationMode(int calcType)
    {
        _calcType = calcType;
        switch (_calcType)
        {
            case G.RATIOS_BY_GEARS:
                _view.showRatio(false);
                _view.showLeadscrewPitch(false);
                _view.showThreadPitch(false);
                _view.showFormattedRatio(false);
                break;
            case G.THREAD_BY_GEARS:
                _view.showRatio(false);
                _view.showLeadscrewPitch(true);
                _view.showThreadPitch(false);
                _view.showFormattedRatio(false);
                break;
            case G.GEARS_BY_RATIO:
                _view.showRatio(true);
                _view.showRatioAsFration(_ratioAsFraction);
                _view.showLeadscrewPitch(false);
                _view.showThreadPitch(false);
                _view.showFormattedRatio(true);
                break;
            case G.GEARS_BY_THREAD:
                _view.showRatio(false);
                _view.showLeadscrewPitch(true);
                _view.showThreadPitch(true);
                _view.showFormattedRatio(true);
                break;
        }
    }

    @Override
    public void setLeadscrewPitch(String valueStr)
    {
        try
        {
            _leadscrewPitch = valueStr != null && !valueStr.isEmpty() ? Double.parseDouble(valueStr) : 0.0;
            recalculateRatio();
        }
        catch (Exception ex)
        {
            _view.showError(ex.getMessage());
        }
    }

    @Override
    public void setLeadscrewPitchUnit(ThreadPitchUnit unit)
    {
        _threadPitchUnit = unit;
    }

    @Override
    public void setThreadPitch(String valueStr)
    {
        try
        {
            _threadPitch = valueStr != null && !valueStr.isEmpty() ? Double.parseDouble(valueStr) : 0.0;
            recalculateRatio();
        }
        catch (Exception ex)
        {
            _view.showError(ex.getMessage());
        }
    }

    @Override
    public void setThreadPitchUnit(ThreadPitchUnit unit)
    {
        _leadscrewPitchUnit = unit;
    }

    @Override
    public void setRatio(String valueStr)
    {
        try
        {
            _ratio = valueStr != null && !valueStr.isEmpty() ? Double.parseDouble(valueStr) : 0.0;
        }
        catch (Exception ex)
        {
            _view.showError(ex.getMessage());
        }
    }

    @Override
    public void setRatio(String numStr, String denStr)
    {
        try
        {
            _ratioNumerator = numStr != null && !numStr.isEmpty() ? Double.parseDouble(numStr) : 0.0;
            _ratioDenominator = denStr != null && !denStr.isEmpty() ? Double.parseDouble(denStr) : 0.0;
            recalculateRatio();
        }
        catch (Exception ex)
        {
            _view.showError(ex.getMessage());
        }
    }

    @Override
    public void setRatioFormat(int precision)
    {
        StringBuilder pattern = new StringBuilder("#0.0");
        for (int i = 0; i < precision-1; i++)
            pattern.append("#");
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        _ratioFormat = new DecimalFormat(pattern.toString(), formatSymbols);
        _ratioFormat.setRoundingMode(RoundingMode.CEILING);
    }

    @Override
    public void setRatioAsFraction(boolean asFraction)
    {
        _ratioAsFraction = asFraction;
        _view.showRatioAsFration(_ratioAsFraction);
    }

    private void recalculateRatio()
    {
        String ratioInfo = "R = <Undefined>";

        _ratio = 0.0;

        if (_calcType == G.GEARS_BY_THREAD)
        {
            if (_threadPitch == 0.0)
            {
                _ratio = 0.0;
            }
            else if (_leadscrewPitch == 0.0)
            {
                _ratio = _threadPitch;
                ratioInfo = "R = " + _ratio + " " + _threadPitchUnit;
            }
            else
            {
                Fraction tpf = _threadPitchUnit.toMmFraction(_threadPitch);
                Fraction spf = _leadscrewPitchUnit.toMmFraction(_leadscrewPitch);
                Fraction fract = tpf.divide(spf);
                _ratio = fract.toDouble();
                ratioInfo = "R = " + _threadPitch + " " + _threadPitchUnit + " / " +
                        _leadscrewPitch + " " + _leadscrewPitchUnit + " = " + fract.toString() +
                        " = " + _ratioFormat.format(_ratio);
            }
        }
        else if (_calcType == G.GEARS_BY_RATIO)
        {
            if (_ratioNumerator == 0.0)
            {
                _ratio = 0.0;
            }
            else if (_ratioDenominator == 0.0)
            {
                _ratio = _ratioNumerator;
                ratioInfo = "R = " + _ratio;
            }
            else
            {
                Fraction fract = new Fraction(_ratioNumerator, _ratioDenominator);
                _ratio = fract.toDouble();
                ratioInfo = "R = " + _ratioNumerator + " / " + _ratioDenominator + " = " +
                        fract.toString() + " = " + _ratioFormat.format(_ratio);
            }
        }

        _view.setFormattedRatio(ratioInfo);
    }

}
