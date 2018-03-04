package com.apsolete.machinery.calculation.changegears;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.calculation.Calculation;
import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.TextChangedListener;

import java.util.ArrayList;

public final class ChangeGearsViewExt extends CalculationView implements ChangeGearsContract.View
{
    private ChangeGearsContract.Presenter _presenter;

    private View _view;
    private CompoundButton _oneSetSwitch;
    private Spinner _calculationModeSpinner;
    private LinearLayout _threadPitchLayout;
    private EditText _threadPitchValue;
    private Spinner _threadPitchUnitSpinner;
    private LinearLayout _screwPitchLayout;
    private EditText _screwPitchValue;
    private Spinner _leadscrewPitchUnitSpinner;
    private LinearLayout _gearRatioLayout;
    private CompoundButton _ratioAsFractionSwitch;
    private EditText _gearRatio;
    private EditText _gearRatioNumerator;
    private EditText _gearRatioDenominator;
    private LinearLayout _gearRatioFractionLayout;
    private TextView _ratioFormattedText;
    //private TextView _resFromNumberText;
    //private TextView _resToNumberText;

    //private ViewGroup _resultView;
    //private CgSettings _settings;
    //private CgCalculator _calculator;
    private CgSettings _settings;

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
    public SettingsBase getSettings()
    {
        return _settings;
    }

    @Override
    public void setOneGearsSet(boolean oneSet)
    {
        _oneSetSwitch.setChecked(oneSet);
    }

    @Override
    public void setGearsSet(int set, String gearsStr)
    {
        _gsViews[set].setGearsSet(gearsStr);
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
    public void setGearsSetEditable(int set, boolean enable)
    {
        _gsViews[set].setEditable(enable);
    }

    @Override
    public void setCalculationMode(int mode)
    {
        _calculationModeSpinner.setSelection(mode);
    }

    @Override
    public void setThreadPitch(String valueStr)
    {
        _threadPitchValue.setText(valueStr);
    }

    @Override
    public void setThreadPitchUnit(int unit)
    {
        _threadPitchUnitSpinner.setSelection(unit);
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
    public void setLeadscrewPitchUnit(int unit)
    {
        _leadscrewPitchUnitSpinner.setSelection(unit);
    }

    @Override
    public void showLeadscrewPitch(boolean visible)
    {
        _screwPitchLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRatio(String valueStr)
    {
        try
        {
            _ratioTextChangedListener.ignoreTextChange(true);
            _gearRatio.setText(valueStr);
        }
        finally
        {
            _ratioTextChangedListener.ignoreTextChange(false);
        }
    }

    @Override
    public void showRatio(boolean visible)
    {
        _gearRatioLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRatioNumerator(String valueStr)
    {
        try
        {
            _ratioNumeratorTextChangedListener.ignoreTextChange(true);
            _gearRatioNumerator.setText(valueStr);
        }
        finally
        {
            _ratioNumeratorTextChangedListener.ignoreTextChange(false);
        }
    }

    @Override
    public void setRatioDenominator(String valueStr)
    {
        try
        {
            _ratioDenominatorTextChangedListener.ignoreTextChange(true);
            _gearRatioDenominator.setText(valueStr);
        }
        finally
        {
            _ratioDenominatorTextChangedListener.ignoreTextChange(false);
        }
    }

    @Override
    public void setRatioAsFration(boolean visible)
    {
        _ratioAsFractionSwitch.setChecked(visible);
    }

    @Override
    public void showRatioAsFration(boolean visible)
    {
        _gearRatio.setVisibility(visible ? View.GONE : View.VISIBLE);
        _gearRatioFractionLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFormattedRatio(String ratioStr)
    {
        _ratioFormattedText.setText(ratioStr);
    }

    @Override
    public void showFormattedRatio(boolean visible)
    {
        _ratioFormattedText.setVisibility(visible ? View.VISIBLE : View.GONE);
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

    private CgSettings.OnChangeListener _settingsChangeListener = new CgSettings.OnChangeListener()
    {
        @Override
        public void onDiffTeethGearingChanged(boolean newValue)
        {
            /*_diffTeethGearing = newValue;*/
        }

        @Override
        public void onDiffTeethDoubleGearChanged(boolean newValue)
        {
            /*_diffTeethDoubleGear = newValue;*/
        }

        @Override
        public void onRatioPrecisionChanged(int newValue)
        {
            /*_ratioPrecision = newValue;
            setRatioFormat(_ratioPrecision);*/
        }
    };

    private GearsSetView.OnGearsSetViewListener _gearsSetViewListener = new GearsSetView.OnGearsSetViewListener()
    {
        @Override
        public void onRequest(GearsSetView gearsSet)
        {
            requestGearsSet(gearsSet);
        }

        @Override
        public void onChanged(GearsSetView gearsSet)
        {
            _presenter.setGearsSet(gearsSet.getId(), gearsSet.getGearsSet());
        }

        @Override
        public void onChecked(GearsSetView gearsSet)
        {
            _presenter.setGearsSetChecked(gearsSet.getId(), gearsSet.isChecked());
        }
    };

    private AdapterView.OnItemSelectedListener _calcModeListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            switch (pos)
            {
                case 0:
                    _presenter.setCalculationMode(G.RATIOS_BY_GEARS);
                    break;
                case 1:
                    _presenter.setCalculationMode(G.THREAD_BY_GEARS);
                    break;
                case 2:
                    _presenter.setCalculationMode(G.GEARS_BY_RATIO);
                    break;
                case 3:
                    _presenter.setCalculationMode(G.GEARS_BY_THREAD);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };

    private AdapterView.OnItemSelectedListener _thrPitchUnitListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            if (pos == 0)
                _presenter.setThreadPitchUnit(ThreadPitchUnit.mm);
            else
                _presenter.setThreadPitchUnit(ThreadPitchUnit.TPI);
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
                _presenter.setLeadscrewPitchUnit(ThreadPitchUnit.mm);
            else
                _presenter.setLeadscrewPitchUnit(ThreadPitchUnit.TPI);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };

    private TextChangedListener _ratioTextChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            _presenter.setRatio(editable.toString());
        }
    };

    private TextChangedListener _ratioNumeratorTextChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            _presenter.setRatioNumerator(editable.toString());
        }
    };

    private TextChangedListener _ratioDenominatorTextChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            _presenter.setRatioDenominator(editable.toString());
        }
    };

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

        _gsViews[G.Z0] = new GearsSetView(G.Z0, _view, R.id.z0Set, R.id.z0Gears, 0, _gearsSetViewListener);
        _gsViews[G.Z1] = new GearsSetView(G.Z1, _view, R.id.z1Set, R.id.z1Gears, R.id.z1Select, _gearsSetViewListener);
        _gsViews[G.Z2] = new GearsSetView(G.Z2, _view, R.id.z2Set, R.id.z2Gears, R.id.z2Select, _gearsSetViewListener);
        _gsViews[G.Z3] = new GearsSetView(G.Z3, _view, R.id.z3Set, R.id.z3Gears, R.id.z3Select, _gearsSetViewListener);
        _gsViews[G.Z4] = new GearsSetView(G.Z4, _view, R.id.z4Set, R.id.z4Gears, R.id.z4Select, _gearsSetViewListener);
        _gsViews[G.Z5] = new GearsSetView(G.Z5, _view, R.id.z5Set, R.id.z5Gears, R.id.z5Select, _gearsSetViewListener);
        _gsViews[G.Z6] = new GearsSetView(G.Z6, _view, R.id.z6Set, R.id.z6Gears, R.id.z6Select, _gearsSetViewListener);

        _calculationModeSpinner = (Spinner)_view.findViewById(R.id.calcTypeSpinner);
        initSpinner(_calculationModeSpinner, R.array.cg_calctype_array, _calcModeListener);

        _threadPitchLayout = (LinearLayout)_view.findViewById(R.id.threadPitchLayout);
        _threadPitchValue = (EditText)_view.findViewById(R.id.threadPitchValue);
        _threadPitchUnitSpinner = (Spinner)_view.findViewById(R.id.threadUnitSpinner);
        initSpinner(_threadPitchUnitSpinner, R.array.cg_pitchunit_array, _thrPitchUnitListener);

        _screwPitchLayout = (LinearLayout)_view.findViewById(R.id.screwPitchLayout);
        _screwPitchValue = (EditText)_view.findViewById(R.id.screwPitchValue);
        _leadscrewPitchUnitSpinner = (Spinner)_view.findViewById(R.id.screwUnitSpinner);
        initSpinner(_leadscrewPitchUnitSpinner, R.array.cg_pitchunit_array, _scrPitchUnitListener);

        _gearRatioLayout = (LinearLayout)_view.findViewById(R.id.gearRatioLayout);
        _gearRatio = (EditText)_view.findViewById(R.id.gearRatioValue);
        _gearRatio.addTextChangedListener(_ratioTextChangedListener);

        _gearRatioNumerator = (EditText)_view.findViewById(R.id.gearRatioNumerator);
        _gearRatioNumerator.addTextChangedListener(_ratioNumeratorTextChangedListener);

        _gearRatioDenominator = (EditText)_view.findViewById(R.id.gearRatioDenominator);
        _gearRatioDenominator.addTextChangedListener(_ratioDenominatorTextChangedListener);

        _gearRatioFractionLayout = (LinearLayout)_view.findViewById(R.id.gearRatioFractionLayout);
        _ratioFormattedText = (TextView)_view.findViewById(R.id.ratioResultText);

        _ratioAsFractionSwitch = (CompoundButton)_view.findViewById(R.id.ratioAsFractionSwitch);
        _ratioAsFractionSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean isChecked = ((CompoundButton)view).isChecked();
                _presenter.setRatioAsFraction(isChecked);
            }
        });

        _settings = new CgSettings(Activity);
        _settings.setListener(_settingsChangeListener);

        return _view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (_presenter != null)
        {
            _presenter.start();
        }
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

    private void requestGearsSet(GearsSetView gsView)
    {
        final GearsSetView gsv = gsView;

        FragmentManager fragmentManager = Activity.getSupportFragmentManager();
        final TeethNumbersDialog dialog = new TeethNumbersDialog();
        dialog.setSelection(gsv.getGearsSet());
        dialog.setResultListener(new DialogBase.ResultListener()
        {
            @Override
            public void onPositive()
            {
                ArrayList<Integer> teethNumbers = dialog.getTeethNumbers();
                _presenter.setGearsSet(gsv.getId(), teethNumbers);
            }

            @Override
            public void onNegative()
            {}
        });
        dialog.show(fragmentManager, "dialog");
    }
}
