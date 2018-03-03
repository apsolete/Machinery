package com.apsolete.machinery.calculation.changegears;

import android.util.SparseArray;

import com.apsolete.machinery.calculation.CalculationPresenter;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.Fraction;
import com.apsolete.machinery.utils.Numbers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public final class ChangeGearsPresenter extends CalculationPresenter implements ChangeGearsContract.Presenter
{
    private final ChangeGearsContract.View _view;
    private boolean _oneSet;
    private SparseArray<ArrayList<Integer>> _gearsSets = new SparseArray<>(G.Z6 + 1);
    private String[] _gsValue = new String[G.Z6 + 1];
    private boolean[] _gsChecked = new boolean[G.Z6 + 1];
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
        for (int set = G.Z0; set < G.Z6; set++)
        {
            _gearsSets.put(set, new ArrayList<Integer>());
        }

        _view = view;
        _oneSet = false;

        _gsValue[G.Z0] = "20-30";
        _gsValue[G.Z1] = "30-40";
        _gsValue[G.Z2] = "50-60";
        _gsValue[G.Z3] = "70-80";
        //_gsValue[G.Z4] = "";
        //_gsValue[G.Z5] = "";
        //_gsValue[G.Z6] = "";

        _gsChecked[G.Z1] = true;
        _gsChecked[G.Z2] = true;

        _view.setPresenter(this);
    }

    @Override
    public void start()
    {
        _view.setOneGearsSet(_oneSet);

        int set = G.Z0;
        for (String val : _gsValue)
        {
            _view.setGearsSet(set, val);
            set++;
        }
        set = G.Z0;
        for (boolean checked : _gsChecked)
        {
            _view.setGearsSetChecked(set, checked);
            set++;
        }

        updateViewByOneGearsSet(_oneSet);

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
        updateViewByOneGearsSet(_oneSet);
    }

    @Override
    public void setGearsSet(int set, String valuesStr)
    {
        _gsValue[set] = valuesStr;
        ArrayList<Integer> values = Numbers.getNumbersList(valuesStr);
        _gearsSets.put(set, values);
        if (set < G.Z6)
            _view.setGearsSetEnabled(set + 1, valuesStr != null && !valuesStr.isEmpty());
    }

    @Override
    public void setGearsSet(int set, ArrayList<Integer> values)
    {
        _gearsSets.put(set, values);
        String valuesStr = Numbers.getString(values);
        _gsValue[set] = valuesStr;
        _view.setGearsSet(set, valuesStr);
        if (set < G.Z6)
            _view.setGearsSetEnabled(set + 1, valuesStr != null && !valuesStr.isEmpty());
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

    private void updateViewByOneGearsSet(boolean oneSet)
    {
        if (oneSet)
        {
            _view.setGearsSetEditable(G.Z0, true);
            _view.setGearsSetEditable(G.Z1, false);
            _view.setGearsSetEditable(G.Z2, false);
            _view.setGearsSetEditable(G.Z3, false);
            _view.setGearsSetEditable(G.Z4, false);
            _view.setGearsSetEditable(G.Z5, false);
            _view.setGearsSetEditable(G.Z6, false);

            _view.setGearsSetEnabled(G.Z1, false);
            _view.setGearsSetEnabled(G.Z2, false);

            int prev = G.Z1, set = G.Z1;
            for (; set <= G.Z6; set++)
            {
                if (prev <= G.Z2)
                    _view.setGearsSetEnabled(set, true);
                else
                    _view.setGearsSetEnabled(set, _gsChecked[prev]);

                prev = set;
            }

        }
        else
        {
            _view.setGearsSetEditable(G.Z0, false);
            _view.setGearsSetEditable(G.Z1, true);
            _view.setGearsSetEditable(G.Z2, true);
            _view.setGearsSetEditable(G.Z3, true);
            _view.setGearsSetEditable(G.Z4, true);
            _view.setGearsSetEditable(G.Z5, true);
            _view.setGearsSetEditable(G.Z6, true);

            int prev = G.Z1, set = G.Z1;
            for (; set <= G.Z6; set++)
            {
                if (prev <= G.Z2)
                    _view.setGearsSetEnabled(set, true);
                else
                    _view.setGearsSetEnabled(set, (_gsValue[prev] != null && !_gsValue[prev].isEmpty()));

                prev = set;
            }
        }
    }
}
