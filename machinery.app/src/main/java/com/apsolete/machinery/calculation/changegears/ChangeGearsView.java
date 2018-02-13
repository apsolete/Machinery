package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.calculation.*;
import com.apsolete.machinery.utils.*;

import android.content.Context;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.text.*;

import java.math.RoundingMode;
import java.text.*;
import java.util.*;
import android.support.v7.widget.*;


public class ChangeGearsView extends DesignContent
{
    private int _ratioPrecision = 1;
    private double _ratio = 0;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;
    private boolean _isOneSet = false;
    private DecimalFormat _ratioFormat;
    private int _calcType;
    private boolean _isRatioFraction;
    private ThreadPitchUnit _thrPitchUnit;
    private ThreadPitchUnit _scrPitchUnit;

    private View _view;
    private SwitchCompat _oneSetSwitch;
    private Spinner _calcTypeSpinner;
    private LinearLayout _threadPitchLayout;
    private EditText _threadPitchValue;
    private Spinner _threadUnitSpinner;
    private LinearLayout _screwPitchLayout;
    private EditText _screwPitchValue;
    private Spinner _screwUnitSpinner;
    private LinearLayout _gearRatioLayout;
    private Switch _ratioAsFractionSwitch;
    private EditText _gearRatioValue;
    private EditText _gearRatioDenominator;
    private LinearLayout _gearRatioDenominatorLayout;
    private TextView _ratioResultText;
    private TextView _resFromNumberText;
    private TextView _resToNumberText;

    private ViewGroup _resultView;
    private CgSettings _settings;
    private CgCalculator _calculator;

    private final GearSetControl[] _gearsCtrls = new GearSetControl[7];
    private final ArrayList<CgResult> _results = new ArrayList<>();
    private int _resFromNumber = 1;
    private int _resToNumber = 1;

    private final GearSetControl.OnGearSetListener _gearSetListener = new GearSetControl.OnGearSetListener()
    {
        @Override
        public void onSet(GearSetControl gearSetCtrl)
        {
            defineGearSet(gearSetCtrl);
        }

        @Override
        public void onSetChanged(GearSetControl gearSetCtrl)
        {
            boolean empty = gearSetCtrl.isEmpty();
            switch (gearSetCtrl.getId())
            {
                case G.Z1:
                    break;
                case G.Z2:
                    _gearsCtrls[G.Z3].setEnabled(!empty);
                    break;
                case G.Z3:
                    _gearsCtrls[G.Z4].setEnabled(!empty);
                    break;
                case G.Z4:
                    _gearsCtrls[G.Z5].setEnabled(!empty);
                    break;
                case G.Z5:
                    _gearsCtrls[G.Z6].setEnabled(!empty);
                    break;
                case G.Z6:
                    break;
            }
        }

        @Override
        public void onGearChecked(GearSetControl gearSetCtrl)
        {
            boolean checked = gearSetCtrl.isChecked();
            switch (gearSetCtrl.getId())
            {
                    //case Z1:
                    //case Z2:
                case G.Z3:
                    _gearsCtrls[G.Z4].setEnabled(checked);
                    _gearsCtrls[G.Z4].setChecked(false);
                    _gearsCtrls[G.Z5].setEnabled(false);
                    _gearsCtrls[G.Z5].setChecked(false);
                    _gearsCtrls[G.Z6].setEnabled(false);
                    _gearsCtrls[G.Z6].setChecked(false);
                    break;
                case G.Z4:
                    _gearsCtrls[G.Z5].setEnabled(checked);
                    _gearsCtrls[G.Z5].setChecked(false);
                    _gearsCtrls[G.Z6].setEnabled(false);
                    _gearsCtrls[G.Z6].setChecked(false);
                    break;
                case G.Z5:
                    _gearsCtrls[G.Z6].setEnabled(checked);
                    _gearsCtrls[G.Z6].setChecked(false);
                    break;
                case G.Z6:
            }
        }
    };

    private OnResultListener<CgResult> _resultListener = new OnResultListener<CgResult>()
    {
        @Override
        public void onResult(CgResult result)
        {
            //int id = _results.size() + 1;
            //ChangeGearsResult res = new ChangeGearsResult(result.Id, ratio, 0.0, gears);
            _results.add(result);
        }

        @Override
        public void onProgress(final int percent)
        {
            Activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showProgress(percent);
                    }
                });
        }

        @Override
        public void onCompleted(final int count)
        {
            Activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int shown = 0;
                        for (CgResult res: _results)
                        {
                            if (res.Id > 100)
                                break;
                            setResultItem(res);
                            shown++;
                            _resToNumber = res.Id;
                            _resToNumberText.setText(Integer.toString(_resToNumber));
                        }
                        Snackbar.make(_view, "Calculated " + count + " ratios. Shown " + shown + " results.", Snackbar.LENGTH_SHORT).show();
                        resetProgress();
                    }
                });
        }
    };

    private CgSettings.OnChangeListener _settingsChangeListener = new CgSettings.OnChangeListener()
    {
        @Override
        public void onDiffTeethGearingChanged(boolean newValue)
        {
            _diffTeethGearing = newValue;
        }

        @Override
        public void onDiffTeethDoubleGearChanged(boolean newValue)
        {
            _diffTeethDoubleGear = newValue;
        }

        @Override
        public void onRatioPrecisionChanged(int newValue)
        {
            _ratioPrecision = newValue;
            setRatioFormat(_ratioPrecision);
        }
    };

    private AdapterView.OnItemSelectedListener _calcTypeListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            switch (pos)
            {
                case 0:
                    _calcType = G.RATIOS_BY_GEARS;
                    showRatio(false);
                    break;
                case 1:
                    _calcType = G.THREAD_BY_GEARS;
                    showPitches(false);
                    break;
                case 2:
                    _calcType = G.GEARS_BY_RATIO;
                    showRatio(true);
                    break;
                case 3:
                    _calcType = G.GEARS_BY_THREAD;
                    showPitches(true);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }

        private void showPitches(boolean both)
        {
            _gearRatioLayout.setVisibility(View.GONE);
            _threadPitchLayout.setVisibility(both ? View.VISIBLE : View.GONE);
            _screwPitchLayout.setVisibility(View.VISIBLE);
            _ratioResultText.setVisibility(both ? View.VISIBLE : View.GONE);
            if (both)
                recalculateRatio();
        }

        private void showRatio(boolean enable)
        {
            _threadPitchLayout.setVisibility(View.GONE);
            _screwPitchLayout.setVisibility(View.GONE);
            _gearRatioLayout.setVisibility(enable ? View.VISIBLE : View.GONE);
            
            if (enable && _ratioAsFractionSwitch.isChecked())
            {
                _gearRatioDenominatorLayout.setVisibility(View.VISIBLE);
                _ratioResultText.setVisibility(View.VISIBLE);
                //recalculateRatio();
            }
            else
            {
                _gearRatioDenominatorLayout.setVisibility(View.GONE);
                _ratioResultText.setVisibility(View.GONE);
            }
            
            recalculateRatio();
        }
    };
    
    private AdapterView.OnItemSelectedListener _thrPitchUnitListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            if (pos == 0)
                _thrPitchUnit = ThreadPitchUnit.mm;
            else
                _thrPitchUnit = ThreadPitchUnit.TPI;
                
            recalculateRatio();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };
    
    private AdapterView.OnItemSelectedListener _scrPitchUnitListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            if (pos == 0)
                _scrPitchUnit = ThreadPitchUnit.mm;
            else
                _scrPitchUnit = ThreadPitchUnit.TPI;
                
            recalculateRatio();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };

    private TextChangedListener _thrPitchChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            recalculateRatio();
        }
    };

    private TextChangedListener _scrPitchChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            recalculateRatio();
        }
    };
    
    private TextChangedListener _gearRatioChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            recalculateRatio();
        }
    };

    private TextChangedListener _gearRatioDenomChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            recalculateRatio();
        }
    };

    public ChangeGearsView()
    {
        super(DesignContent.CHANGEGEARS, R.layout.content_changegears, R.string.title_change_gears_design);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (_view != null)
            return _view;

        _view = super.onCreateView(inflater, container, savedInstanceState);
        assert _view != null;

        _oneSetSwitch = (SwitchCompat)_view.findViewById(R.id.oneSetForAllGears);
        _oneSetSwitch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    _isOneSet = ((Switch)view).isChecked();
                    setOneSetForAllGears(_isOneSet);
                }
            });

        _resultView = (ViewGroup)_view.findViewById(R.id.resultsLayout);

        _gearsCtrls[G.Z0] = new GearSetControl(G.Z0, _view, R.id.z0Set, R.id.z0Gears, 0, _gearSetListener);
        _gearsCtrls[G.Z0].setEnabled(_oneSetSwitch.isChecked());

        _gearsCtrls[G.Z1] = new GearSetControl(G.Z1, _view, R.id.z1Set, R.id.z1Gears, R.id.z1Select, _gearSetListener);

        _gearsCtrls[G.Z2] = new GearSetControl(G.Z2, _view, R.id.z2Set, R.id.z2Gears, R.id.z2Select, _gearSetListener);

        _gearsCtrls[G.Z3] = new GearSetControl(G.Z3, _view, R.id.z3Set, R.id.z3Gears, R.id.z3Select, _gearSetListener);

        _gearsCtrls[G.Z4] = new GearSetControl(G.Z4, _view, R.id.z4Set, R.id.z4Gears, R.id.z4Select, _gearSetListener);

        _gearsCtrls[G.Z5] = new GearSetControl(G.Z5, _view, R.id.z5Set, R.id.z5Gears, R.id.z5Select, _gearSetListener);

        _gearsCtrls[G.Z6] = new GearSetControl(G.Z6, _view, R.id.z6Set, R.id.z6Gears, R.id.z6Select, _gearSetListener);

        ImageButton showNextButton = (ImageButton)_view.findViewById(R.id.showNext);
        showNextButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    int fi = _resToNumber + 1;
                    if (fi >= _results.size())
                        return;
                    int ti = fi + 99;
                    if (ti > _results.size())
                        ti = _results.size();
                    _resFromNumber = fi;
                    _resToNumber = ti;
                    showResults();
                }
            });
            
        ImageButton showPrevButton = (ImageButton)_view.findViewById(R.id.showPrev);
        showPrevButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    int fi = _resFromNumber - 100;
                    if (fi < 0)
                        return;
                    int ti = fi + 99;
                    if (ti > _results.size())
                        ti = _results.size();
                    _resFromNumber = fi;
                    _resToNumber = ti;
                    showResults();
                }
            });

        _isOneSet = _oneSetSwitch.isChecked();
        setOneSetForAllGears(_isOneSet);

        _calcTypeSpinner = (Spinner)_view.findViewById(R.id.calcTypeSpinner);
        initSpinner(_calcTypeSpinner, R.array.cg_calctype_array, _calcTypeListener);

        _threadPitchLayout = (LinearLayout)_view.findViewById(R.id.threadPitchLayout);
        _threadPitchValue = (EditText)_view.findViewById(R.id.threadPitchValue);
        _threadPitchValue.addTextChangedListener(_thrPitchChangedListener);
        _threadUnitSpinner = (Spinner)_view.findViewById(R.id.threadUnitSpinner);
        initSpinner(_threadUnitSpinner, R.array.cg_pitchunit_array, _thrPitchUnitListener);
        _screwPitchLayout = (LinearLayout)_view.findViewById(R.id.screwPitchLayout);
        _screwPitchValue = (EditText)_view.findViewById(R.id.screwPitchValue);
        _screwPitchValue.addTextChangedListener(_scrPitchChangedListener);
        _screwUnitSpinner = (Spinner)_view.findViewById(R.id.screwUnitSpinner);
        initSpinner(_screwUnitSpinner, R.array.cg_pitchunit_array, _scrPitchUnitListener);
        
        _gearRatioLayout = (LinearLayout)_view.findViewById(R.id.gearRatioLayout);
        _ratioAsFractionSwitch = (Switch)_view.findViewById(R.id.ratioAsFractionSwitch);
        _gearRatioValue = (EditText)_view.findViewById(R.id.gearRatioValue);
        _gearRatioValue.addTextChangedListener(_gearRatioChangedListener);
        _gearRatioDenominator = (EditText)_view.findViewById(R.id.gearRatioDenominator);
        _gearRatioDenominator.addTextChangedListener(_gearRatioDenomChangedListener);
        _gearRatioDenominatorLayout = (LinearLayout)_view.findViewById(R.id.gearRatioDenominatorLayout);
        _ratioResultText = (TextView)_view.findViewById(R.id.ratioResultText);
        _resFromNumberText = (TextView)_view.findViewById(R.id.fromNumberText);
        _resToNumberText = (TextView)_view.findViewById(R.id.toNumberText);
        
        _ratioAsFractionSwitch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    _isRatioFraction = ((Switch)view).isChecked();
                    _gearRatioDenominatorLayout.setVisibility(_isRatioFraction ? View.VISIBLE : View.GONE);
                    _ratioResultText.setVisibility(_isRatioFraction ? View.VISIBLE : View.GONE);
                    if (_isRatioFraction)
                        recalculateRatio();
                }
            });

        _settings = new CgSettings(Activity);
        _settings.setListener(_settingsChangeListener);
        setRatioFormat(_settings.getRatioPrecision());

        _calculator = new CgCalculator(_resultListener);

        return _view;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public SettingsBase getSettings()
    {
        return _settings;
    }

    @Override
    public void save()
    {
        // TODO: Implement this method
    }

    @Override
    public void clear()
    {
        _results.clear();
        _resultView.removeAllViews();
        _resFromNumber = 1;
        _resToNumber = 1;
        _resFromNumberText.setText(Integer.toString(_resFromNumber));
        _resToNumberText.setText(Integer.toString(_resToNumber));
    }

    @Override
    public boolean close()
    {
        return true;
    }

    @Override
    public void setOptions()
    {

    }

    @Override
    protected void calculate()
    {
        // read settings
        _ratioPrecision = _settings.getRatioPrecision();
        _diffTeethGearing = _settings.getDiffTeethGearing();
        _diffTeethDoubleGear = _settings.getDiffTeethDoubleGear();
        setRatioFormat(_ratioPrecision);
        double accuracy = Math.pow(10, -_ratioPrecision);
        
        _calculator.setAccuracy(accuracy);
        _calculator.setDiffTeethGearing(_diffTeethGearing);
        _calculator.setDiffTeethDoubleGear(_diffTeethDoubleGear);

        if (_isOneSet)
        {
            int[] set = _gearsCtrls[G.Z0].getGears();
            int gears = 2;
            if (_gearsCtrls[G.Z6].isChecked())
                gears = 6;
            else if (_gearsCtrls[G.Z4].isChecked())
                gears = 4;
            if (gears > set.length)
            {
                Snackbar.make(_view, "Gears set has less than " + gears + " wheels.", Snackbar.LENGTH_SHORT).show();
                return;
            }
                
            _calculator.setGearsSet(gears, set);
        }
        else
        {
            int[] gs1 = _gearsCtrls[G.Z1].getGears();
            int[] gs2 = _gearsCtrls[G.Z2].getGears();
            int[] gs3 = _gearsCtrls[G.Z3].getGears();
            int[] gs4 = _gearsCtrls[G.Z4].getGears();
            int[] gs5 = _gearsCtrls[G.Z5].getGears();
            int[] gs6 = _gearsCtrls[G.Z6].getGears();
            
            if (gs1 == null || gs1.length == 0 || gs2 == null || gs2.length == 0)
            {
                Snackbar.make(_view, "Z1 and Z2 should have at least one wheel.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            
            _calculator.setGearsSet(gs1, gs2, gs3, gs4, gs5, gs6);
        }
        
        clear();
        //_activity.setProgressBarIndeterminateVisibility(true);
        
        _calculator.setRatio(_ratio);
        _calculator.calculate();
    }

    private void defineGearSet(GearSetControl gearSetCtrl)
    {
        final GearSetControl control = gearSetCtrl;

        FragmentManager fragmentManager = Activity.getSupportFragmentManager();
        final TeethNumbersDialog dialog = new TeethNumbersDialog();
        dialog.setSelection(control.getText());
        dialog.setResultListener(new DialogBase.ResultListener()
            {
                @Override
                public void onPositive()
                {
                    ArrayList<Integer> teethNumbers = dialog.getTeethNumbers();
                    if (teethNumbers != null && teethNumbers.size() > 0)
                    {
                        String text = Numbers.getString(teethNumbers);
                        control.setText(text);
                    }
                    else
                        control.setText("");
                }

                @Override
                public void onNegative()
                {}
            });
        dialog.show(fragmentManager, "dialog");
    }

    private void setResultItem(CgResult result)
    {
        try
        {
            //_pb.setProgress(1);
            LayoutInflater layoutInflater = (LayoutInflater)Activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.change_gears_result, null);

            TextView text = (TextView)view.findViewById(R.id.resultNumberText);
            text.setText(Integer.toString(result.Id));

            int visibility;
            text = (TextView)view.findViewById(R.id.z1Text);
            text.setText(Integer.toString(result.Gears[0]));
            text = (TextView)view.findViewById(R.id.z2Text);
            text.setText(Integer.toString(result.Gears[1]));

            visibility = result.Gears[2] > 0 ? View.VISIBLE : View.GONE;
            view.findViewById(R.id.gears34Layout).setVisibility(visibility);
            if (visibility == View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.z3Text);
                text.setText(Integer.toString(result.Gears[2]));
                text = (TextView)view.findViewById(R.id.z4Text);
                text.setText(Integer.toString(result.Gears[3]));
            }

            visibility = result.Gears[4] > 0 ? View.VISIBLE : View.GONE;
            view.findViewById(R.id.gears56Layout).setVisibility(visibility);
            if (visibility == View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.z5Text);
                text.setText(Integer.toString(result.Gears[4]));
                text = (TextView)view.findViewById(R.id.z6Text);
                text.setText(Integer.toString(result.Gears[5]));
            }

            text = (TextView)view.findViewById(R.id.ratioText);
            text.setText(_ratioFormat.format(result.Ratio));
            
            visibility = (_calcType == G.THREAD_BY_GEARS || _calcType == G.GEARS_BY_THREAD) ? View.VISIBLE : View.GONE;
            view.findViewById(R.id.threadPitchLayout).setVisibility(visibility);
            if (visibility == View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.threadPitchText);
                String pitchStr = _screwPitchValue.getText().toString();
                double scrPitch = !pitchStr.isEmpty() ? Double.parseDouble(pitchStr) : 0;
                text.setText(_ratioFormat.format(result.Ratio * scrPitch));
            }

            _resultView.addView(view);
        }
        catch (Exception e)
        {
            //
        }
    }
    
    private void showResults()
    {
        _resFromNumberText.setText(Integer.toString(_resFromNumber));
        _resToNumberText.setText(Integer.toString(_resToNumber));
        _resultView.removeAllViews();
        List<CgResult> next = _results.subList(_resFromNumber-1, _resToNumber);
        for (CgResult r: next)
        {
            setResultItem(r);
        }
    }

    private void setOneSetForAllGears(boolean isOneSet)
    {
        _gearsCtrls[G.Z0].enableOwnSet(isOneSet);
        _gearsCtrls[G.Z0].setEnabled(isOneSet);

        _gearsCtrls[G.Z1].enableOwnSet(!isOneSet);
        _gearsCtrls[G.Z2].enableOwnSet(!isOneSet);
        _gearsCtrls[G.Z3].enableOwnSet(!isOneSet);
        _gearsCtrls[G.Z4].enableOwnSet(!isOneSet);
        _gearsCtrls[G.Z5].enableOwnSet(!isOneSet);
        _gearsCtrls[G.Z6].enableOwnSet(!isOneSet);

        if (isOneSet)
        {
            _gearsCtrls[G.Z1].setEnabled(false);
            _gearsCtrls[G.Z2].setEnabled(false);

            _gearsCtrls[G.Z3].setEnabled(true);
            _gearsCtrls[G.Z4].setEnabled(false);
            _gearsCtrls[G.Z5].setEnabled(false);
            _gearsCtrls[G.Z6].setEnabled(false);
        }
        else
        {
            _gearsCtrls[G.Z1].setEnabled(true);
            _gearsCtrls[G.Z2].setEnabled(true);

            _gearsCtrls[G.Z3].setEnabled(!_gearsCtrls[G.Z2].isEmpty());
            _gearsCtrls[G.Z4].setEnabled(!_gearsCtrls[G.Z3].isEmpty());
            _gearsCtrls[G.Z5].setEnabled(!_gearsCtrls[G.Z4].isEmpty());
            _gearsCtrls[G.Z6].setEnabled(!_gearsCtrls[G.Z5].isEmpty());
        }
    }

    private void setRatioFormat(int precision)
    {
        StringBuilder pattern = new StringBuilder("#0.0");
        for (int i = 0; i < precision-1; i++)
            pattern.append("#");
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        _ratioFormat = new DecimalFormat(pattern.toString(), formatSymbols);
        _ratioFormat.setRoundingMode(RoundingMode.CEILING);
    }
    
    private void initSpinner(Spinner spinner, int strarrayid, AdapterView.OnItemSelectedListener listener)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity,
                                                                             strarrayid,
                                                                             android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }
    
    private void recalculateRatio()
    {
        String ratioInfo = "R = <Undefined>";
        
        _ratio = 0.0;
        
        if (_calcType == G.GEARS_BY_THREAD)
        {
            String pitchStr = _threadPitchValue.getText().toString();
            double thrPitch = !pitchStr.isEmpty() ? Double.parseDouble(pitchStr) : 0;
            pitchStr = _screwPitchValue.getText().toString();
            double scrPitch = !pitchStr.isEmpty() ? Double.parseDouble(pitchStr) : 0;
            
            if (thrPitch == 0.0)
            {
                _ratio = 0.0;
            }
            else if (scrPitch == 0.0)
            {
                _ratio = thrPitch;
                ratioInfo = "R = " + _ratio + " " + _thrPitchUnit;
            }
            else
            {
                Fraction tpf = _thrPitchUnit.toMmFraction(thrPitch);
                Fraction spf = _scrPitchUnit.toMmFraction(scrPitch);
                Fraction fract = tpf.divide(spf);
                _ratio = fract.toDouble();
                ratioInfo = "R = " + thrPitch + " " + _thrPitchUnit + " / " +
                    scrPitch + " " + _scrPitchUnit + " = " + fract.toString() +
                    " = " + _ratioFormat.format(_ratio);
            }
        }
        else if (_calcType == G.GEARS_BY_RATIO)
        {
            String ratioStr = _gearRatioValue.getText().toString();
            double ratioNum = !ratioStr.isEmpty() ? Double.parseDouble(ratioStr) : 0;
            ratioStr = _gearRatioDenominator.getText().toString();
            double ratioDen = !ratioStr.isEmpty() ? Double.parseDouble(ratioStr) : 0;

            if (ratioNum == 0.0)
            {
                _ratio = 0.0;
            }
            else if (ratioDen == 0.0)
            {
                _ratio = ratioNum;
                ratioInfo = "R = " + _ratio;
            }
            else
            {
                Fraction fract = new Fraction(ratioNum, ratioDen);
                _ratio = fract.toDouble();
                ratioInfo = "R = " + ratioNum + " / " + ratioDen + " = " +
                    fract.toString() + " = " + _ratioFormat.format(_ratio);
            }
        }
        _ratioResultText.setText(ratioInfo);
    }
}
