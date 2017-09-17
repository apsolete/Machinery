package com.apsolete.machinery.activity.design.changegears;

import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.common.*;
import com.apsolete.machinery.activity.design.*;
import com.apsolete.machinery.util.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v7.preference.PreferenceManager;
import android.view.*;
import android.widget.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class ChangeGears extends DesignContent
{
    private class Result
    {
        public double Ratio;
        public int[] Gears = new int[6];
    }

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
    private CalculationType _calcType;
    private boolean _isInchUnits;
    private boolean _isRatioAsFraction;

    private View _view;
    private Switch _oneSetSwitch;
    private Spinner _calcTypeSpinner;
    private EditText _ratioEdText;

    LinearLayout _threadPitchLayout;
    EditText _threadPitchValue;
    Spinner _threadUnitSpinner;
    LinearLayout _screwPitchLayout;
    EditText _screwPitchValue;
    Spinner _screwUnitSpinner;
    LinearLayout _gearRatioLayout;
    Switch _ratioAsFractionSwitch;
    EditText _gearRatioValue;
    EditText _gearRatioDenominator;
    LinearLayout _gearRatioDenominatorLayout;
    TextView _ratioResultText;

    private ViewGroup _resultView;
    private ProgressBar _pb;
    private ChangeGearsSettings _settings;

    private final GearSetControl[] _gearsCtrls = new GearSetControl[7];
    private final ArrayList<Result> _results = new ArrayList<>();

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

    private CgCalculator.OnResultListener _resultListener = new CgCalculator.OnResultListener()
    {
        @Override
        public void onResult(final double ratio, final int[] gears)
        {
            _activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setResultItem(ratio, gears);
                    }
                });
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

    private ChangeGearsSettings.OnChangeListener _settingsChangeListener = new ChangeGearsSettings.OnChangeListener()
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

    private AdapterView.OnItemSelectedListener _calcTypeSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            //Object obj = parent.getItemAtPosition(pos);
            switch (pos)
            {
                case 0:
                    _calcType = CalculationType.RatiosByGears;
                    showRatio(false);
                    break;
                case 1:
                    _calcType = CalculationType.ThreadByGears;
                    showPitches(false);
                    break;
                case 2:
                    _calcType = CalculationType.GearsByRatio;
                    showRatio(true);
                    break;
                case 3:
                    _calcType = CalculationType.GearsByThread;
                    showPitches(true);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
            // TODO: Implement this method
        }

        private void showPitches(boolean both)
        {
            _gearRatioLayout.setVisibility(View.GONE);
            _threadPitchLayout.setVisibility(both ? View.VISIBLE : View.GONE);
            _screwPitchLayout.setVisibility(View.VISIBLE);
            _ratioResultText.setVisibility(both ? View.VISIBLE : View.GONE);
        }

        private void showRatio(boolean enable)
        {
            _threadPitchLayout.setVisibility(View.GONE);
            _screwPitchLayout.setVisibility(View.GONE);
            if (enable)
            {
                _gearRatioLayout.setVisibility(View.VISIBLE);
                _ratioResultText.setVisibility(View.VISIBLE);
                if (_ratioAsFractionSwitch.isChecked())
                    _gearRatioDenominatorLayout.setVisibility(View.VISIBLE);
                else
                    _gearRatioDenominatorLayout.setVisibility(View.GONE);
            }
            else
            {
                _gearRatioLayout.setVisibility(View.GONE);
                _ratioResultText.setVisibility(View.GONE);
            }
        }
    };
    
    private AdapterView.OnItemSelectedListener _thrPitchUnitSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            // TODO: Implement this method
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
            // TODO: Implement this method
        }
    };
    private AdapterView.OnItemSelectedListener _scrPitchUnitSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            // TODO: Implement this method
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
            // TODO: Implement this method
        }
    };

    public ChangeGears()
    {
        super(DesignType.ChangeGears, R.layout.content_changegears_design, R.string.title_change_gears_design);
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

        _ratioEdText = (EditText)_view.findViewById(R.id.gearRatioValue);

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

        _isOneSet = _oneSetSwitch.isChecked();
        setOneSetForAllGears(_isOneSet);

        _calcTypeSpinner = (Spinner)_view.findViewById(R.id.calcTypeSpinner);
        initSpinner(_calcTypeSpinner, R.array.cg_calctype_array, _calcTypeSelectedListener);

        _threadPitchLayout = (LinearLayout)_view.findViewById(R.id.threadPitchLayout);
        _threadPitchValue = (EditText)_view.findViewById(R.id.threadPitchValue);
        _threadUnitSpinner = (Spinner)_view.findViewById(R.id.threadUnitSpinner);
        initSpinner(_threadUnitSpinner, R.array.cg_pitchunit_array, _thrPitchUnitSelectedListener);
        _screwPitchLayout = (LinearLayout)_view.findViewById(R.id.screwPitchLayout);
        _screwPitchValue = (EditText)_view.findViewById(R.id.screwPitchValue);
        _screwUnitSpinner = (Spinner)_view.findViewById(R.id.screwUnitSpinner);
        initSpinner(_screwUnitSpinner, R.array.cg_pitchunit_array, _scrPitchUnitSelectedListener);
        
        _gearRatioLayout = (LinearLayout)_view.findViewById(R.id.gearRatioLayout);
        _ratioAsFractionSwitch = (Switch)_view.findViewById(R.id.ratioAsFractionSwitch);
        _gearRatioValue = (EditText)_view.findViewById(R.id.gearRatioValue);
        _gearRatioDenominator = (EditText)_view.findViewById(R.id.gearRatioDenominator);
        _gearRatioDenominatorLayout = (LinearLayout)_view.findViewById(R.id.gearRatioDenominatorLayout);
        _ratioResultText = (TextView)_view.findViewById(R.id.ratioResultText);
        
        _ratioAsFractionSwitch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    _isRatioAsFraction = ((Switch)view).isChecked();
                    _gearRatioDenominatorLayout.setVisibility(_isRatioAsFraction ? View.VISIBLE : View.GONE);
                }
            });

        _settings = new ChangeGearsSettings(_activity);
        _settings.setListener(_settingsChangeListener);

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
    }

    @Override
    public void close()
    {
        // TODO: Implement this method
    }

    @Override
    public void setOptions()
    {

    }

    @Override
    protected void calculate()
    {
        String ratioStr = _ratioEdText.getText().toString();
        _ratio = (ratioStr != null && !ratioStr.isEmpty()) ? Double.parseDouble(ratioStr) : 0;

        // read settings
        setRatioFormat(_settings.getRatioPrecision());
        _diffTeethGearing = _settings.getDiffTeethGearing();
        _diffTeethDoubleGear = _settings.getDiffTeethDoubleGear();

        if (_isOneSet)
        {
            int[] set = _gearsCtrls[Z0].getGears();
            int[] gears = new int[]{2};
            if (_gearsCtrls[Z6].isChecked())
                gears[0] = 6;
            else if (_gearsCtrls[Z4].isChecked())
                gears[0] = 4;

            CgCalculator calc = new CgCalculator(_ratio, _ratioPrecision, _diffTeethGearing,
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

            CgCalculator calc = new CgCalculator(_ratio, _ratioPrecision, _diffTeethGearing,
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
                    if (teethNumbers != null)
                    {
                        String text = NumbersParser.getString(teethNumbers);
                        control.setText(text);
                    }
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
        String format = "#0.";
        for (int i = 0; i < precision; i++)
            format += "0";
        _ratioFormat = new DecimalFormat(format);
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
}
