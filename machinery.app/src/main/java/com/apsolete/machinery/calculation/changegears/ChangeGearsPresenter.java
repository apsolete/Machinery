package com.apsolete.machinery.calculation.changegears;

import android.util.SparseArray;

import com.apsolete.machinery.calculation.CalculationPresenter;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.ArrayUtils;
import com.apsolete.machinery.utils.Fraction;
import com.apsolete.machinery.utils.Numbers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ChangeGearsPresenter extends CalculationPresenter implements ChangeGearsContract.Presenter
{
    private class Result implements ChangeGearsContract.Result
    {
        public int Number = 0;
        public String Z1 = null;
        public String Z2 = null;
        public String Z3 = null;
        public String Z4 = null;
        public String Z5 = null;
        public String Z6 = null;
        public String Ratio = null;
        public String ThreadPitch = null;

        public Result(ChangeGears.Result result)
        {
            Number = result.Id;
            Z1 = Integer.toString(result.Gears[0]);
            Z2 = Integer.toString(result.Gears[1]);
            if (result.Gears.length > 2)
                Z3 = Integer.toString(result.Gears[2]);
            if (result.Gears.length > 3)
                Z4 = Integer.toString(result.Gears[3]);
            if (result.Gears.length > 4)
                Z5 = Integer.toString(result.Gears[4]);
            if (result.Gears.length > 5)
                Z6 = Integer.toString(result.Gears[5]);
            Ratio = _ratioFormat.format(result.Ratio);
            ThreadPitch = _ratioFormat.format(result.Ratio * _leadscrewPitch);
        }
    }

    private final ChangeGearsContract.View _view;
    private boolean _oneSet;
    private SparseArray<List<Integer>> _gearsSets = new SparseArray<>(G.Z6 + 1);
    private boolean[] _gsChecked = new boolean[G.Z6 + 1];
    private int _oneSetGearsCount = 2;
    //private Map<Integer, int[]> _gearsSets = new HashMap<>(G.Z6 + 1);
    private int _calculationMode;

    private double _ratio = 1.25;
    private double _ratioNumerator = 34;
    private double _ratioDenominator = 56;
    private boolean _ratioAsFraction = true;
    private ThreadPitchUnit _threadPitchUnit = ThreadPitchUnit.mm;
    private ThreadPitchUnit _leadscrewPitchUnit = ThreadPitchUnit.mm;
    private double _leadscrewPitch = 4;
    private double _threadPitch = 0.75;
    private DecimalFormat _ratioFormat;
    private double _calculatedRatio;
    private int _firstResultNumber = 1;
    private int _lastResultNumber = 1;
    private ArrayList<ChangeGearsContract.Result> _results = new ArrayList<>();
    private ChangeGears _calculator;

    /*settings*/
    private int _ratioPrecision = 2;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;


    private OnResultListener<ChangeGears.Result> _resultListener = new OnResultListener<ChangeGears.Result>()
    {
        @Override
        public void onResult(ChangeGears.Result result)
        {
            Result r = new Result(result);
            _results.add(r);
        }

        @Override
        public void onProgress(int percent)
        {
            _view.showProgress(percent);
        }

        @Override
        public void onCompleted(int count)
        {
            _view.showProgress(0);
            int shown = getNextResults();
            _view.showMessage("Calculated " + count + " ratios. Shown " + shown + " results.");
        }
    };

    public ChangeGearsPresenter(ChangeGearsContract.View view)
    {
        for (int set = G.Z0; set < G.Z6; set++)
        {
            _gearsSets.put(set, new ArrayList<Integer>());
        }

        _view = view;
        _oneSet = false;

        _gearsSets.put(G.Z0, new ArrayList<>(Arrays.asList(20, 21, 22, 23, 24)));
        _gearsSets.put(G.Z1, new ArrayList<>(Arrays.asList(30, 31, 32, 33, 34)));
        _gearsSets.put(G.Z2, new ArrayList<>(Arrays.asList(40, 41, 42, 43, 44)));
        _gearsSets.put(G.Z3, new ArrayList<>(Arrays.asList(50, 51, 52, 53, 54)));

        _gsChecked[G.Z1] = true;
        _gsChecked[G.Z2] = true;

        _view.setPresenter(this);
        _calculator = new ChangeGears();
        _calculator.setResultListener(_resultListener);
    }

    @Override
    public void start()
    {
        _view.setOneGearsSet(_oneSet);

        int set = G.Z0;
        for (; set < G.Z6; set++)
        {
            String valuesStr = Numbers.getString(_gearsSets.get(set));
            _view.setGearsSet(set, valuesStr);
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

        _view.setRatios(Double.toString(_ratio), Double.toString(_ratioNumerator), Double.toString(_ratioDenominator));
        _view.setRatioAsFration(_ratioAsFraction);
        _view.showRatioAsFration(_ratioAsFraction);

        _view.setLeadscrewPitch(Double.toString(_leadscrewPitch));
        _view.setLeadscrewPitchUnit(_leadscrewPitchUnit.ordinal());
        _view.setThreadPitch(Double.toString(_threadPitch));
        _view.setThreadPitchUnit(_threadPitchUnit.ordinal());

        setRatioFormat(3);
        recalculateRatio();
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
        _firstResultNumber = 1;
        _lastResultNumber = 1;

        _view.clearResults();
        _view.setFirstResultNumber(Integer.toString(_firstResultNumber));
        _view.setLastResultNumber(Integer.toString(_lastResultNumber));
    }

    @Override
    public void calculate()
    {
        _view.clearResults();

        _calculator.setAccuracy(Math.pow(10, -_ratioPrecision));
        _calculator.setDiffTeethGearing(_diffTeethGearing);
        _calculator.setDiffTeethDoubleGear(_diffTeethDoubleGear);
        //_calculator.setRatio(_ratio);
        if (_oneSet)
        {
            List<Integer> gears = _gearsSets.get(G.Z0);
            _calculator.setGearsSet(_oneSetGearsCount, ArrayUtils.toArrayInt(gears));
            _calculator.calculate();
        }
        else
        {

        }
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
        ArrayList<Integer> values = Numbers.getNumbersList(valuesStr);
        _gearsSets.put(set, values);
        if (set < G.Z6)
            _view.setGearsSetEnabled(set + 1, valuesStr != null && !valuesStr.isEmpty());
    }

    @Override
    public void setGearsSet(int set, List<Integer> values)
    {
        _gearsSets.put(set, values);
        String valuesStr = Numbers.getString(values);
        _view.setGearsSet(set, valuesStr);
        if (set < G.Z6)
            _view.setGearsSetEnabled(set + 1, valuesStr != null && !valuesStr.isEmpty());
    }

    @Override
    public void setGearsSetChecked(int set, boolean checked)
    {
        if (set < G.Z1 || set > G.Z6)
            return;
        _gsChecked[set] = checked;
        if (checked)
        {
            _oneSetGearsCount = (set % 2) == 0 ? set : set - 1;
            if (set < G.Z6)
                _view.setGearsSetEnabled(set + 1, true);
        }
        else
        {
            int s = set - 1;
            _oneSetGearsCount = (s % 2) == 0 ? s : s - 1;
            for (set++; set <= G.Z6; set++)
            {
                _gsChecked[set] = false;
                _view.setGearsSetChecked(set, false);
                _view.setGearsSetEnabled(set, false);
            }
        }
    }

    @Override
    public void setCalculationMode(int calcType)
    {
        _calculationMode = calcType;
        switch (_calculationMode)
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
                recalculateRatio();
                break;
            case G.GEARS_BY_THREAD:
                _view.showRatio(false);
                _view.showLeadscrewPitch(true);
                _view.showThreadPitch(true);
                _view.showFormattedRatio(true);
                recalculateRatio();
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
            _view.showMessage(ex.getMessage());
        }
    }

    @Override
    public void setLeadscrewPitchUnit(ThreadPitchUnit unit)
    {
        _leadscrewPitchUnit = unit;
        recalculateRatio();
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
            _view.showMessage(ex.getMessage());
        }
    }

    @Override
    public void setThreadPitchUnit(ThreadPitchUnit unit)
    {
        _threadPitchUnit = unit;
        recalculateRatio();
    }

    @Override
    public void setRatio(String valueStr)
    {
        try
        {
            _ratio = valueStr != null && !valueStr.isEmpty() ? Double.parseDouble(valueStr) : 0.0;
            recalculateRatio();
        }
        catch (Exception ex)
        {
            _view.showMessage(ex.getMessage());
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
            _view.showMessage(ex.getMessage());
        }
    }

    @Override
    public void setRatioNumerator(String valueStr)
    {
        try
        {
            _ratioNumerator = valueStr != null && !valueStr.isEmpty() ? Double.parseDouble(valueStr) : 0.0;
            recalculateRatio();
        }
        catch (Exception ex)
        {
            _view.showMessage(ex.getMessage());
        }
    }

    @Override
    public void setRatioDenominator(String valueStr)
    {
        try
        {
            _ratioDenominator = valueStr != null && !valueStr.isEmpty() ? Double.parseDouble(valueStr) : 0.0;
            recalculateRatio();
        }
        catch (Exception ex)
        {
            _view.showMessage(ex.getMessage());
        }
    }

    @Override
    public void setRatioFormat(int precision)
    {
        _ratioPrecision = precision;
        StringBuilder pattern = new StringBuilder("#0.0");
        for (int i = 0; i < precision-1; i++)
            pattern.append("#");
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        _ratioFormat = new DecimalFormat(pattern.toString(), formatSymbols);
        _ratioFormat.setRoundingMode(RoundingMode.CEILING);
        recalculateRatio();
    }

    @Override
    public void setRatioAsFraction(boolean asFraction)
    {
        _ratioAsFraction = asFraction;
        _view.showRatioAsFration(_ratioAsFraction);
        recalculateRatio();
    }

    @Override
    public int getNextResults()
    {
        int fi = _lastResultNumber + 1;
        if (fi >= _results.size())
            return 0;
        int li = fi + 99;
        if (li > _results.size())
            li = _results.size();
        _firstResultNumber = fi;
        _lastResultNumber = li;
        List<ChangeGearsContract.Result> next = _results.subList(_firstResultNumber-1, _lastResultNumber);
        _view.showResults(next);
        return next.size();
    }

    @Override
    public int getPrevResults()
    {
        int fi = _firstResultNumber - 100;
        if (fi < 0)
            return 0;
        int ti = fi + 99;
        if (ti > _results.size())
            ti = _results.size();
        _firstResultNumber = fi;
        _lastResultNumber = ti;
        List<ChangeGearsContract.Result> prev = _results.subList(_firstResultNumber-1, _lastResultNumber);
        _view.showResults(prev);
        return prev.size();
    }

    private void recalculateRatio()
    {
        String ratioInfo = "R = <Undefined>";

        _calculatedRatio = 0.0;

        if (_calculationMode == G.GEARS_BY_THREAD)
        {
            if (_threadPitch == 0.0)
            {
                _calculatedRatio = 0.0;
            }
            else if (_leadscrewPitch == 0.0)
            {
                _calculatedRatio = _threadPitchUnit.toMm(_threadPitch);
                ratioInfo = "R = " + _ratioFormat.format(_calculatedRatio) + " (" + _threadPitchUnit + ")";
            }
            else
            {
                Fraction tpf = _threadPitchUnit.toMmFraction(_threadPitch);
                Fraction spf = _leadscrewPitchUnit.toMmFraction(_leadscrewPitch);
                Fraction fract = tpf.divide(spf);
                _calculatedRatio = fract.toDouble();
                ratioInfo = "R = " + _threadPitch + " (" + _threadPitchUnit + ") / " +
                        _leadscrewPitch + " (" + _leadscrewPitchUnit + ") = " + fract.toString() +
                        " = " + _ratioFormat.format(_calculatedRatio);
            }
        }
        else if (_calculationMode == G.GEARS_BY_RATIO)
        {
            if (_ratioAsFraction)
            {
                if (_ratioNumerator == 0.0)
                {
                    _calculatedRatio = 0.0;
                }
                else if (_ratioDenominator == 0.0)
                {
                    _calculatedRatio = _ratioNumerator;
                    ratioInfo = "R = " + _ratioFormat.format(_calculatedRatio);
                }
                else
                {
                    Fraction fract = new Fraction(_ratioNumerator, _ratioDenominator);
                    _calculatedRatio = fract.toDouble();
                    ratioInfo = "R = " + _ratioNumerator + " / " + _ratioDenominator + " = " +
                            fract.toString() + " = " + _ratioFormat.format(_calculatedRatio);
                }
            }
            else
            {
                _calculatedRatio = _ratio;
                ratioInfo = "R = " + _ratioFormat.format(_calculatedRatio);
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

            _view.setGearsSetEnabled(G.Z0, true);
            _view.setGearsSetEnabled(G.Z1, false);
            _view.setGearsSetEnabled(G.Z2, false);
            _view.setGearsSetEnabled(G.Z3, true);

            int set = G.Z4;
            for (; set <= G.Z6; set++)
            {
                _view.setGearsSetEnabled(set, _gsChecked[set - 1]);
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

            _view.setGearsSetEnabled(G.Z0, false);
            _view.setGearsSetEnabled(G.Z1, true);

            int set = G.Z2;
            for (; set <= G.Z6; set++)
            {
                List<?> values = _gearsSets.get(set - 1);
                _view.setGearsSetEnabled(set, (values != null && !values.isEmpty()));
            }
        }
    }
}
