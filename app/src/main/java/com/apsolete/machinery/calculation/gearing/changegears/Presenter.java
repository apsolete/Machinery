package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.calculation.CalculationPresenter;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.ArrayUtils;
import com.apsolete.machinery.utils.Fraction;
import com.apsolete.machinery.utils.Numbers;

import java.text.*;
import java.util.ArrayList;
import java.util.List;

public final class Presenter extends CalculationPresenter implements Contract.Presenter
{
    private final Contract.View _view;
    private boolean _oneSet;
    private GearKits _gearKits = new GearKits();
    private int _oneSetGearsCount = 2;
    private int _calculationMode;

    private double _ratio = 1.25;
    private double _ratioNumerator = 34;
    private double _ratioDenominator = 56;
    private boolean _ratioAsFraction = true;
    private ThreadPitchUnit _threadPitchUnit = ThreadPitchUnit.mm;
    private ThreadPitchUnit _leadscrewPitchUnit = ThreadPitchUnit.mm;
    private double _leadscrewPitch = 4;
    private double _threadPitch = 0.75;
    private NumberFormat _ratioFormat;
    private double _calculatedRatio;
    private int _firstResultNumber = 1;
    private int _lastResultNumber = 1;
    private ArrayList<Contract.Result> _results = new ArrayList<>();
    private ChangeGears _calculator;

    /*settings*/
    private int _ratioPrecision = 2;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;


    private OnResultListener<Result> _resultListener = new OnResultListener<Result>()
    {
        @Override
        public void onResult(Result result)
        {
            if (_calculationMode == G.THREAD_BY_GEARS)
                result.setLeadscrewPitch(_leadscrewPitch);
            result.setFormat(_ratioFormat);
            _results.add(result);
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

    public Presenter(Contract.View view)
    {
        super(view);
        _view = view;
        _oneSet = true;

        _gearKits.putZ0(new int[]{20, 21, 22, 23, 24});
        _gearKits.putZ1(new int[]{30, 31, 32, 33, 34});
        _gearKits.putZ2(new int[]{40, 41, 42, 43, 44});
        _gearKits.putZ3(new int[]{50, 51, 52, 53, 54});
        _gearKits.putZ4(new int[]{});
        _gearKits.putZ5(new int[]{});
        _gearKits.putZ6(new int[]{});

        _gearKits.setChecked(G.Z1, true);
        _gearKits.setChecked(G.Z2, true);

        _view.setPresenter(this);
        _calculator = new ChangeGears();
        _calculator.setResultListener(_resultListener);
    }

    @Override
    public void start()
    {
        _view.setOneGearKit(_oneSet);

        for (int kit = G.Z0; kit < G.Z6; kit++)
        {
            String valuesStr = Numbers.getString(_gearKits.get(kit));
            _view.setGearKit(kit, valuesStr);
        }

        for (int kit = G.Z0; kit < G.Z6; kit++)
        {
            _view.setGearKitChecked(kit, _gearKits.isChecked(kit));
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
        _results.clear();

        _view.clearResults();
        _view.setFirstResultNumber("1");
        _view.setLastResultNumber("1");
    }

    @Override
    public void calculate()
    {
        clear();

        _calculator.setAccuracy(Math.pow(10, -_ratioPrecision));
        _calculator.setDiffTeethGearing(_diffTeethGearing);
        _calculator.setDiffTeethDoubleGear(_diffTeethDoubleGear);
        
        double r = _calculationMode == G.GEARS_BY_RATIO || _calculationMode == G.GEARS_BY_THREAD
                    ? _calculatedRatio : 0.0;
        _calculator.setRatio(r);
        
        if (_oneSet)
        {
            _calculator.setGearKit(_oneSetGearsCount, _gearKits.getZ0());
        }
        else
        {
            int[] zs1 = _gearKits.getZ1();//ArrayUtils.toArrayInt(_gearsSets.get(G.Z1));
            int[] zs2 = _gearKits.getZ2();//ArrayUtils.toArrayInt(_gearsSets.get(G.Z2));
            int[] zs3 = _gearKits.getZ3();//ArrayUtils.toArrayInt(_gearsSets.get(G.Z3));
            int[] zs4 = _gearKits.getZ4();//ArrayUtils.toArrayInt(_gearsSets.get(G.Z4));
            int[] zs5 = _gearKits.getZ5();//ArrayUtils.toArrayInt(_gearsSets.get(G.Z5));
            int[] zs6 = _gearKits.getZ6();//ArrayUtils.toArrayInt(_gearsSets.get(G.Z6));
            int total = zs1.length > 0 ? zs1.length : 1;
            total *= zs2.length > 0 ? zs2.length : 1;
            total *= zs3.length > 0 ? zs3.length : 1;
            total *= zs4.length > 0 ? zs4.length : 1;
            total *= zs5.length > 0 ? zs5.length : 1;
            total *= zs6.length > 0 ? zs6.length : 1;
            if (total > 20000)
            {
                _view.showMessage("Too much gears!");
                return;
            }
            _calculator.setGearKit(zs1, zs2, zs3, zs4, zs5, zs6);
        }
        _calculator.calculate();
    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public void setOneGearKit(boolean oneKit)
    {
        _oneSet = oneKit;
        updateViewByOneGearsSet(_oneSet);
    }

    @Override
    public void setGearKit(int kit, String valuesStr)
    {
        int[] values = Numbers.getNumbers(valuesStr);
        _gearKits.put(kit, values);
        if (kit > G.Z1 && kit < G.Z6)
            _view.setGearKitEnabled(kit + 1, valuesStr != null && !valuesStr.isEmpty());
    }

    @Override
    public void setGearKit(int kit, List<Integer> values)
    {
        _gearKits.put(kit, ArrayUtils.toArrayInt(values));
        String valuesStr = Numbers.getString(values);
        _view.setGearKit(kit, valuesStr);
        if (kit > G.Z1 && kit < G.Z6)
            _view.setGearKitEnabled(kit + 1, valuesStr != null && !valuesStr.isEmpty());
    }

    @Override
    public void setGearKitChecked(int kit, boolean checked)
    {
        if (kit < G.Z1 || kit > G.Z6)
            return;
        _gearKits.setChecked(kit, checked);
        if (checked)
        {
            _oneSetGearsCount = (kit % 2) == 0 ? kit : kit - 1;
            if (kit < G.Z6)
                _view.setGearKitEnabled(kit + 1, true);
        }
        else
        {
            int s = kit - 1;
            _oneSetGearsCount = (s % 2) == 0 ? s : s - 1;
            for (kit++; kit <= G.Z6; kit++)
            {
                _gearKits.setChecked(kit, false);
                _view.setGearKitChecked(kit, false);
                _view.setGearKitEnabled(kit, false);
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
        _ratioFormat = CalculationPresenter.getNumberFormat(pattern.toString());
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
        int fi = _lastResultNumber > 1 ? _lastResultNumber + 1 : 1;
        if (fi > _results.size())
            return 0;
        int li = fi + 99;
        if (li > _results.size())
            li = _results.size();
        _firstResultNumber = fi;
        _lastResultNumber = li;
        List<Contract.Result> next = _results.subList(_firstResultNumber-1, _lastResultNumber);
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
        List<Contract.Result> prev = _results.subList(_firstResultNumber-1, _lastResultNumber);
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
            _view.setGearKitEditable(G.Z0, true);
            _view.setGearKitEditable(G.Z1, false);
            _view.setGearKitEditable(G.Z2, false);
            _view.setGearKitEditable(G.Z3, false);
            _view.setGearKitEditable(G.Z4, false);
            _view.setGearKitEditable(G.Z5, false);
            _view.setGearKitEditable(G.Z6, false);

            _view.setGearKitEnabled(G.Z0, true);
            _view.setGearKitEnabled(G.Z1, false);
            _view.setGearKitEnabled(G.Z2, false);
            _view.setGearKitEnabled(G.Z3, true);

            for (int kit = G.Z4; kit <= G.Z6; kit++)
            {
                _view.setGearKitEnabled(kit, _gearKits.isChecked(kit - 1));
            }
        }
        else
        {
            _view.setGearKitEditable(G.Z0, false);
            _view.setGearKitEditable(G.Z1, true);
            _view.setGearKitEditable(G.Z2, true);
            _view.setGearKitEditable(G.Z3, true);
            _view.setGearKitEditable(G.Z4, true);
            _view.setGearKitEditable(G.Z5, true);
            _view.setGearKitEditable(G.Z6, true);

            _view.setGearKitEnabled(G.Z0, false);
            _view.setGearKitEnabled(G.Z1, true);

            
            for (int set = G.Z2; set <= G.Z6; set++)
            {
                int[] values = _gearKits.get(set - 1);
                _view.setGearKitEnabled(set, (values != null && values.length > 0));
            }
        }
    }
}
