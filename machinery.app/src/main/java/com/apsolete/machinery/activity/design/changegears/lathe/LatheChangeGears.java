package com.apsolete.machinery.activity.design.changegears.lathe;

import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.common.*;
import com.apsolete.machinery.activity.design.*;
import com.apsolete.machinery.activity.design.changegears.*;
import com.apsolete.machinery.util.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v7.preference.PreferenceManager;
import android.view.*;
import android.widget.*;
import android.text.*;

import java.math.RoundingMode;
import java.text.*;
import java.util.*;


public class LatheChangeGears extends DesignContent
{
    private class Result
    {
        public double Ratio;
        public int[] Gears = new int[6];
        public Result(double ratio, int[] gears)
        {
            Ratio = ratio;
            Gears = Arrays.copyOf(gears, 6);
        }
    }
    
    public static final int RATIOS_BY_GEARS = 0;
    public static final int THREAD_BY_GEARS = 1;
    public static final int GEARS_BY_RATIO = 2;
    public static final int GEARS_BY_THREAD = 3;

    private static final int Z0 = 0;//one set for all;
    private static final int Z1 = 1;//1;
    private static final int Z2 = 2;//2;
    private static final int Z3 = 3;//3;
    private static final int Z4 = 4;//4;
    private static final int Z5 = 5;//5;
    private static final int Z6 = 6;//6;
    
    private int _ratioPrecision = 1;
    private double _ratio = 0;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;
    private boolean _isOneSet = false;
    private DecimalFormat _ratioFormat;
    private int _calcType;
    private boolean _isRatioFraction;
    private PitchUnit _thrPitchUnit;
    private PitchUnit _scrPitchUnit;

    private View _view;
    private Switch _oneSetSwitch;
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
    private ProgressBar _pb;
    private LatheChangeGearsSettings _settings;
    private ListView _resultList;

    private final GearSetControl[] _gearsCtrls = new GearSetControl[7];
    private final ArrayList<Result> _results = new ArrayList<>();
    private final ArrayList<String> _strResults = new ArrayList<>();
    private ArrayAdapter<String> _adapter;
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
                case Z1:
                    break;
                case Z2:
                    _gearsCtrls[Z3].setEnabled(!empty);
                    break;
                case Z3:
                    _gearsCtrls[Z4].setEnabled(!empty);
                    break;
                case Z4:
                    _gearsCtrls[Z5].setEnabled(!empty);
                    break;
                case Z5:
                    _gearsCtrls[Z6].setEnabled(!empty);
                    break;
                case Z6:
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
                case Z3:
                    _gearsCtrls[Z4].setEnabled(checked);
                    _gearsCtrls[Z4].setChecked(false);
                    _gearsCtrls[Z5].setEnabled(false);
                    _gearsCtrls[Z5].setChecked(false);
                    _gearsCtrls[Z6].setEnabled(false);
                    _gearsCtrls[Z6].setChecked(false);
                    break;
                case Z4:
                    _gearsCtrls[Z5].setEnabled(checked);
                    _gearsCtrls[Z5].setChecked(false);
                    _gearsCtrls[Z6].setEnabled(false);
                    _gearsCtrls[Z6].setChecked(false);
                    break;
                case Z5:
                    _gearsCtrls[Z6].setEnabled(checked);
                    _gearsCtrls[Z6].setChecked(false);
                    break;
                case Z6:
            }
        }
    };

    private LatheChangeGearCalculator.OnResultListener _resultListener = new LatheChangeGearCalculator.OnResultListener()
    {
        @Override
        public void onResult(final double ratio, final int[] gears)
        {
            Result res = new Result(ratio, gears);
            _results.add(res);
            if (_results.size() < 100)
            {
                _resToNumber++;
                _activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            _resToNumberText.setText(Integer.toString(_resToNumber));
                            setResultItem(ratio, gears);
                            //setResultItem2(ratio, gears);
                        }
                    });
            }
        }

        @Override
        public void onProgress(final int percent)
        {
            _activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        _pb.setProgress(percent);
                    }
                });
        }

        @Override
        public void onCompleted(final int count)
        {
            _activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Snackbar.make(_view, "Calculated " + count + " ratios.", Snackbar.LENGTH_SHORT).show();
                    }
                });
        }
    };

    private LatheChangeGearsSettings.OnChangeListener _settingsChangeListener = new LatheChangeGearsSettings.OnChangeListener()
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
                    _calcType = RATIOS_BY_GEARS;
                    showRatio(false);
                    break;
                case 1:
                    _calcType = THREAD_BY_GEARS;
                    showPitches(false);
                    break;
                case 2:
                    _calcType = GEARS_BY_RATIO;
                    showRatio(true);
                    break;
                case 3:
                    _calcType = GEARS_BY_THREAD;
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
                recalculateRatio();
            }
            else
            {
                _gearRatioDenominatorLayout.setVisibility(View.GONE);
                _ratioResultText.setVisibility(View.GONE);
            }
        }
    };
    
    private AdapterView.OnItemSelectedListener _thrPitchUnitListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            if (pos == 0)
                _thrPitchUnit = PitchUnit.mm;
            else
                _thrPitchUnit = PitchUnit.TPI;
                
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
                _scrPitchUnit = PitchUnit.mm;
            else
                _scrPitchUnit = PitchUnit.TPI;
                
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

    public LatheChangeGears()
    {
        super(DesignContent.LATHECHANGEGEARS, R.layout.content_changegears_design, R.string.title_change_gears_design);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (_view != null)
            return _view;

        _view = super.onCreateView(inflater, container, savedInstanceState);
        assert _view != null;

        _oneSetSwitch = (Switch)_view.findViewById(R.id.oneSetForAllGears);
        _oneSetSwitch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    _isOneSet = ((Switch)view).isChecked();
                    setOneSetForAllGears(_isOneSet);
                }
            });

        _resultView = (ViewGroup)_view.findViewById(R.id.resultLayout);

        _gearsCtrls[Z0] = new GearSetControl(Z0, _view, R.id.z0Set, R.id.z0Gears, 0, _gearSetListener);
        _gearsCtrls[Z0].setEnabled(_oneSetSwitch.isChecked());

        _gearsCtrls[Z1] = new GearSetControl(Z1, _view, R.id.z1Set, R.id.z1Gears, R.id.z1Select, _gearSetListener);

        _gearsCtrls[Z2] = new GearSetControl(Z2, _view, R.id.z2Set, R.id.z2Gears, R.id.z2Select, _gearSetListener);

        _gearsCtrls[Z3] = new GearSetControl(Z3, _view, R.id.z3Set, R.id.z3Gears, R.id.z3Select, _gearSetListener);

        _gearsCtrls[Z4] = new GearSetControl(Z4, _view, R.id.z4Set, R.id.z4Gears, R.id.z4Select, _gearSetListener);

        _gearsCtrls[Z5] = new GearSetControl(Z5, _view, R.id.z5Set, R.id.z5Gears, R.id.z5Select, _gearSetListener);

        _gearsCtrls[Z6] = new GearSetControl(Z6, _view, R.id.z6Set, R.id.z6Gears, R.id.z6Select, _gearSetListener);

        _pb = (ProgressBar)_view.findViewById(R.id.progressBar);
        ImageButton calcButton = (ImageButton)_view.findViewById(R.id.calculate);
        calcButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    calculate();
                }
            });
            
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
                    _resFromNumberText.setText(Integer.toString(_resFromNumber));
                    _resToNumberText.setText(Integer.toString(_resToNumber));
                    _resultView.removeAllViews();
                    List<Result> next = _results.subList(_resFromNumber-1, _resToNumber-1);
                    for (Result r: next)
                    {
                        setResultItem(r.Ratio, r.Gears);
                    }
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
                    _resFromNumberText.setText(Integer.toString(_resFromNumber));
                    _resToNumberText.setText(Integer.toString(_resToNumber));
                    _resultView.removeAllViews();
                    List<Result> next = _results.subList(_resFromNumber-1, _resToNumber-1);
                    for (Result r: next)
                    {
                        setResultItem(r.Ratio, r.Gears);
                    }
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

        _settings = new LatheChangeGearsSettings(_activity);
        _settings.setListener(_settingsChangeListener);
        setRatioFormat(_settings.getRatioPrecision());
        
        _resultList = (ListView)_view.findViewById(R.id.resultList);
        _adapter = new ArrayAdapter<>(_activity, android.R.layout.simple_list_item_1, _strResults);
        _resultList.setAdapter(_adapter);

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
        _pb.setProgress(0);
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
        //_ratio = getRatio();
        

        // read settings
        _ratioPrecision = _settings.getRatioPrecision();
        _diffTeethGearing = _settings.getDiffTeethGearing();
        _diffTeethDoubleGear = _settings.getDiffTeethDoubleGear();
        setRatioFormat(_ratioPrecision);
        double accuracy = Math.pow(10, -_ratioPrecision);

        if (_isOneSet)
        {
            int[] set = _gearsCtrls[Z0].getGears();
            int[] gears = new int[]{2};
            if (_gearsCtrls[Z6].isChecked())
                gears[0] = 6;
            else if (_gearsCtrls[Z4].isChecked())
                gears[0] = 4;

            LatheChangeGearCalculator calc = new LatheChangeGearCalculator(_ratio, accuracy, _diffTeethGearing,
                                                 _diffTeethDoubleGear, _resultListener);
            calc.calculate(set, gears);
        }
        else
        {
            int[] gs1 = _gearsCtrls[Z1].getGears();
            int[] gs2 = _gearsCtrls[Z2].getGears();
            int[] gs3 = _gearsCtrls[Z3].getGears();
            int[] gs4 = _gearsCtrls[Z4].getGears();
            int[] gs5 = _gearsCtrls[Z5].getGears();
            int[] gs6 = _gearsCtrls[Z6].getGears();

            LatheChangeGearCalculator calc = new LatheChangeGearCalculator(_ratio, accuracy, _diffTeethGearing,
                                                 _diffTeethDoubleGear, _resultListener);
            calc.calculate(gs1, gs2, gs3, gs4, gs5, gs6);
        }
    }

    private void defineGearSet(GearSetControl gearSetCtrl)
    {
        final GearSetControl control = gearSetCtrl;

        FragmentManager fragmentManager = _activity.getSupportFragmentManager();
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

    private void setResultItem(double ratio, int[] gears)
    {
        try
        {
            //_pb.setProgress(1);
            LayoutInflater layoutInflater = (LayoutInflater)_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.change_gears_result, null);

            int visibility;
            TextView text = (TextView)view.findViewById(R.id.z1Text);
            text.setText(Integer.toString(gears[0]));
            text = (TextView)view.findViewById(R.id.z2Text);
            text.setText(Integer.toString(gears[1]));

            visibility = gears[2] > 0 ? View.VISIBLE : View.INVISIBLE;
            view.findViewById(R.id.mult1).setVisibility(visibility);
            view.findViewById(R.id.z3z4Div).setVisibility(visibility);
            text = (TextView)view.findViewById(R.id.z3Text);
            text.setVisibility(visibility);
            if (visibility == View.VISIBLE) text.setText(Integer.toString(gears[2]));
            text = (TextView)view.findViewById(R.id.z4Text);
            text.setVisibility(visibility);
            if (visibility == View.VISIBLE) text.setText(Integer.toString(gears[3]));

            visibility = gears[4] > 0 ? View.VISIBLE : View.INVISIBLE;
            view.findViewById(R.id.mult2).setVisibility(visibility);
            view.findViewById(R.id.z5z6Div).setVisibility(visibility);
            text = (TextView)view.findViewById(R.id.z5Text);
            text.setVisibility(visibility);
            if (visibility == View.VISIBLE) text.setText(Integer.toString(gears[4]));
            text = (TextView)view.findViewById(R.id.z6Text);
            text.setVisibility(visibility);
            if (visibility == View.VISIBLE) text.setText(Integer.toString(gears[5]));

            text = (TextView)view.findViewById(R.id.ratioText);
            text.setText(_ratioFormat.format(ratio));

            _resultView.addView(view);
        }
        catch (Exception e)
        {
            //
        }
    }
    
    private void setResultItem2(double ratio, int[] gears)
    {
        String res = Arrays.toString(gears) + " = " + ratio;
        _strResults.add(res);
        _adapter.notifyDataSetChanged();
    }

    private void setOneSetForAllGears(boolean isOneSet)
    {
        _gearsCtrls[Z0].enableOwnSet(isOneSet);
        _gearsCtrls[Z0].setEnabled(isOneSet);

        _gearsCtrls[Z1].enableOwnSet(!isOneSet);
        _gearsCtrls[Z2].enableOwnSet(!isOneSet);
        _gearsCtrls[Z3].enableOwnSet(!isOneSet);
        _gearsCtrls[Z4].enableOwnSet(!isOneSet);
        _gearsCtrls[Z5].enableOwnSet(!isOneSet);
        _gearsCtrls[Z6].enableOwnSet(!isOneSet);

        if (isOneSet)
        {
            _gearsCtrls[Z1].setEnabled(false);
            _gearsCtrls[Z2].setEnabled(false);

            _gearsCtrls[Z3].setEnabled(true);
            _gearsCtrls[Z4].setEnabled(false);
            _gearsCtrls[Z5].setEnabled(false);
            _gearsCtrls[Z6].setEnabled(false);
        }
        else
        {
            _gearsCtrls[Z1].setEnabled(true);
            _gearsCtrls[Z2].setEnabled(true);

            _gearsCtrls[Z3].setEnabled(!_gearsCtrls[Z2].isEmpty());
            _gearsCtrls[Z4].setEnabled(!_gearsCtrls[Z3].isEmpty());
            _gearsCtrls[Z5].setEnabled(!_gearsCtrls[Z4].isEmpty());
            _gearsCtrls[Z6].setEnabled(!_gearsCtrls[Z5].isEmpty());
        }
    }

    private void setRatioFormat(int precision)
    {
        String pattern = "#0.0";
        for (int i = 0; i < precision-1; i++)
            pattern += "#";
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        _ratioFormat = new DecimalFormat(pattern, formatSymbols);
        _ratioFormat.setRoundingMode(RoundingMode.CEILING);
    }
    
    private void initSpinner(Spinner spinner, int strarrayid, AdapterView.OnItemSelectedListener listener)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(_activity,
                                                                             strarrayid,
                                                                             android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }
    
    private void recalculateRatio()
    {
        String ratioInfo = "R = <Undefined>";
        
        if (_calcType == GEARS_BY_THREAD)
        {
            String pitchStr = _threadPitchValue.getText().toString();
            double thrPitch = (pitchStr != null && !pitchStr.isEmpty()) ? Double.parseDouble(pitchStr) : 0;
            pitchStr = _screwPitchValue.getText().toString();
            double scrPitch = (pitchStr != null && !pitchStr.isEmpty()) ? Double.parseDouble(pitchStr) : 0;
            
            if (thrPitch == 0.0)
            {
                _ratio = 0;
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
        else if (_calcType == GEARS_BY_RATIO)
        {
            String ratioStr = _gearRatioValue.getText().toString();
            double ratioNum = (ratioStr != null && !ratioStr.isEmpty()) ? Double.parseDouble(ratioStr) : 0;
            ratioStr = _gearRatioDenominator.getText().toString();
            double ratioDen = (ratioStr != null && !ratioStr.isEmpty()) ? Double.parseDouble(ratioStr) : 0;

            if (ratioNum == 0.0)
            {
                _ratio = 0;
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
