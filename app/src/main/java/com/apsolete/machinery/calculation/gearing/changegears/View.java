package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.TextChangedListener;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class View extends CalculationView implements Contract.View
{
    private Contract.Presenter _presenter;

    private android.view.View _view;
    private CompoundButton _oneSetSwitch;
    private Spinner _calculationModeSpinner;
    private LinearLayout _threadPitchLayout;
    private EditText _threadPitchEdit;
    private Spinner _threadPitchUnitSpinner;
    private LinearLayout _leadscrewPitchLayout;
    private EditText _leadscrewPitchEdit;
    private Spinner _leadscrewPitchUnitSpinner;
    //private LinearLayout _gearRatioLayout;
    private CompoundButton _ratioAsFractionSwitch;
    private EditText _gearRatio;
    private EditText _gearRatioNumerator;
    private EditText _gearRatioDenominator;
    private LinearLayout _gearRatioFractionLayout;
    private TextView _ratioFormattedText;
    private TextView _resultFirstNumberText;
    private TextView _resultLastNumberText;
    private ViewGroup _resultView;

    private ChangeGearsSettings _settings = new ChangeGearsSettings();

    private final GearSetView[] _gkViews = new GearSetView[7];

    public View()
    {
        super(G.CHANGEGEARS, R.layout.view_changegears, R.string.title_changegears);
    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {
        _presenter = presenter;
    }

    @Override
    public SettingsBase getSettings()
    {
        return _settings;
    }

    @Override
    public void showProgress(final int percent)
    {
        Activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (percent > 0)
                    {
                        if (ProgressBar.getVisibility() == android.view.View.GONE)
                            ProgressBar.setVisibility(android.view.View.VISIBLE);
                        ProgressBar.setProgress(percent);
                        return;
                    }
                    ProgressBar.setProgress(0);
                    ProgressBar.setVisibility(android.view.View.GONE);
                }
            });
    }

    @Override
    public void setOneGearKit(boolean oneKit)
    {
        _oneSetSwitch.setChecked(oneKit);
    }

    @Override
    public void setGearKit(int kit, String gearsStr)
    {
        _gkViews[kit].setGears(gearsStr);
    }

    @Override
    public void setGearKitChecked(int kit, boolean checked)
    {
        _gkViews[kit].setSwitched(checked);
    }

    @Override
    public void setGearKitEnabled(int kit, boolean enabled)
    {
        _gkViews[kit].setEnabled(enabled);
    }

    @Override
    public void setGearKitEditable(int kit, boolean enable)
    {
        _gkViews[kit].enableEdit(enable);
    }

    @Override
    public void setDiffLockedZ2Z3(boolean diff)
    {

    }

    @Override
    public void setDiffLockedZ4Z5(boolean diff)
    {

    }

    @Override
    public void setDiffGearingZ1Z2(boolean diff)
    {

    }

    @Override
    public void setDiffGearingZ3Z4(boolean diff)
    {

    }

    @Override
    public void setDiffGearingZ5Z6(boolean diff)
    {

    }

    @Override
    public void setCalculationMode(int mode)
    {
        _calculationModeSpinner.setSelection(mode);
    }

    @Override
    public void setThreadPitch(String valueStr)
    {
        try
        {
            _threadPitchTextChangedListener.stop();
            _threadPitchEdit.setText(valueStr);
        }
        finally
        {
            _threadPitchTextChangedListener.start();
        }
    }

    @Override
    public void setThreadPitchUnit(int unit)
    {
        _threadPitchUnitSpinner.setSelection(unit);
    }

    @Override
    public void showThreadPitch(boolean visible)
    {
        //_threadPitchLayout.setVisibility(G.toVisibility(visible));
    }

    @Override
    public void setLeadscrewPitch(String valueStr)
    {
        try
        {
            _leadscrewPitchTextChangedListener.stop();
            _leadscrewPitchEdit.setText(valueStr);
        }
        finally
        {
            _leadscrewPitchTextChangedListener.start();
        }
    }

    @Override
    public void setLeadscrewPitchUnit(int unit)
    {
        _leadscrewPitchUnitSpinner.setSelection(unit);
    }

    @Override
    public void showLeadscrewPitch(boolean visible)
    {
        //_leadscrewPitchLayout.setVisibility(G.toVisibility(visible));
    }

    @Override
    public void setRatios(String ratioStr, String ratioNumStr, String ratioDenStr)
    {
        try
        {
            _ratioTextChangedListener.stop();
            _gearRatio.setText(ratioStr);
            _ratioNumeratorTextChangedListener.stop();
            _gearRatioNumerator.setText(ratioNumStr);
            _ratioDenominatorTextChangedListener.stop();
            _gearRatioDenominator.setText(ratioDenStr);
        }
        finally
        {
            _ratioTextChangedListener.start();
            _ratioNumeratorTextChangedListener.start();
            _ratioDenominatorTextChangedListener.start();
        }
    }

    @Override
    public void setRatioAsFration(boolean visible)
    {
        _ratioAsFractionSwitch.setChecked(visible);
    }

    @Override
    public void showRatio(boolean visible)
    {
        //_gearRatioLayout.setVisibility(G.toVisibility(visible));
    }

    @Override
    public void showRatioAsFration(boolean visible)
    {
        _gearRatio.setVisibility(G.toVisibility(visible));
        _gearRatioFractionLayout.setVisibility(G.toVisibility(visible));
    }

    @Override
    public void setFormattedRatio(String ratioStr)
    {
        _ratioFormattedText.setText(ratioStr);
    }

    @Override
    public void showFormattedRatio(boolean visible)
    {
        _ratioFormattedText.setVisibility(G.toVisibility(visible));
    }

    @Override
    public void setRatioPrecision(int precision)
    {

    }

    @Override
    public void setFirstResultNumber(String valuestr)
    {
        _resultFirstNumberText.setText(valuestr);
    }

    @Override
    public void setLastResultNumber(String valueStr)
    {
        _resultLastNumberText.setText(valueStr);
    }

    @Override
    public void showResults(final List<Contract.Result> results)
    {
        Activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Contract.Result first = results.get(0);
                    Contract.Result last = results.get(results.size()-1);
                    _resultFirstNumberText.setText(first.id());
                    _resultLastNumberText.setText(last.id());

                    _resultView.removeAllViews();
                    for (Contract.Result r: results)
                    {
                        setResultItem(r);
                    }
                }
            });
    }

    @Override
    public void clearResults()
    {
        _resultView.removeAllViews();
        _resultFirstNumberText.setText("0");
        _resultLastNumberText.setText("0");
    }

    @Override
    public void showMessage(String message)
    {
        Snackbar.make(_view, message, Snackbar.LENGTH_SHORT).show();
    }

    private ChangeGearsSettings.OnChangeListener _settingsChangeListener = new ChangeGearsSettings.OnChangeListener()
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
            _presenter.setRatioPrecision(newValue);
        }
    };

    private GearSetView.OnGearSetViewListener _gearKitViewListener = new GearSetView.OnGearSetViewListener()
    {
        @Override
        public void onRequest(GearSetView gearKit)
        {
            requestGearKit(gearKit);
        }

        @Override
        public void onChanged(GearSetView gearKit)
        {
            _presenter.setGearKit(gearKit.id(), gearKit.gears());
        }

        @Override
        public void onChecked(GearSetView gearKit)
        {
            _presenter.setGearKitChecked(gearKit.id(), gearKit.switched());
        }
    };

    private AdapterView.OnItemSelectedListener _calcModeListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id)
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
        public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id)
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
        public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id)
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

    private TextChangedListener _leadscrewPitchTextChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            _presenter.setLeadscrewPitch(editable.toString());
        }
    };

    private TextChangedListener _threadPitchTextChangedListener = new TextChangedListener()
    {
        @Override
        public void onTextChanged(Editable editable)
        {
            _presenter.setThreadPitch(editable.toString());
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
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (_view != null)
            return _view;

        _view = super.onCreateView(inflater, container, savedInstanceState);
        assert _view != null;

        _oneSetSwitch = (CompoundButton)_view.findViewById(R.id.oneSetOfGears);
        _oneSetSwitch.setOnClickListener(new android.view.View.OnClickListener()
            {
                @Override
                public void onClick(android.view.View view)
                {
                    boolean _isOneSet = ((CompoundButton)view).isChecked();
                    _presenter.setOneGearKit(_isOneSet);
                }
            });

        _gkViews[G.Z0] = new GearSetView(G.Z0, _view, R.id.z0Set, R.id.z0Gears, 0, _gearKitViewListener);
        _gkViews[G.Z1] = new GearSetView(G.Z1, _view, R.id.z1Set, R.id.z1Gears, R.id.z1Switch, _gearKitViewListener);
        _gkViews[G.Z2] = new GearSetView(G.Z2, _view, R.id.z2Set, R.id.z2Gears, R.id.z2Switch, _gearKitViewListener);
        _gkViews[G.Z3] = new GearSetView(G.Z3, _view, R.id.z3Set, R.id.z3Gears, R.id.z3Switch, _gearKitViewListener);
        _gkViews[G.Z4] = new GearSetView(G.Z4, _view, R.id.z4Set, R.id.z4Gears, R.id.z4Switch, _gearKitViewListener);
        _gkViews[G.Z5] = new GearSetView(G.Z5, _view, R.id.z5Set, R.id.z5Gears, R.id.z5Switch, _gearKitViewListener);
        _gkViews[G.Z6] = new GearSetView(G.Z6, _view, R.id.z6Set, R.id.z6Gears, R.id.z6Switch, _gearKitViewListener);

        _calculationModeSpinner = (Spinner)_view.findViewById(R.id.calcTypeSpinner);
        initSpinner(_calculationModeSpinner, R.array.cg_calctype_array, _calcModeListener);

        _threadPitchLayout = (LinearLayout)_view.findViewById(R.id.threadPitchLayout);
        _threadPitchEdit = (EditText)_view.findViewById(R.id.threadPitchValue);
        _threadPitchEdit.addTextChangedListener(_threadPitchTextChangedListener);
        _threadPitchUnitSpinner = (Spinner)_view.findViewById(R.id.threadPitchUnit);
        initSpinner(_threadPitchUnitSpinner, R.array.cg_pitchunit_array, _thrPitchUnitListener);

        _leadscrewPitchLayout = (LinearLayout)_view.findViewById(R.id.screwPitchLayout);
        _leadscrewPitchEdit = (EditText)_view.findViewById(R.id.leadScrewPitchValue);
        _leadscrewPitchEdit.addTextChangedListener(_leadscrewPitchTextChangedListener);
        _leadscrewPitchUnitSpinner = (Spinner)_view.findViewById(R.id.leadScrewPitchUnit);
        initSpinner(_leadscrewPitchUnitSpinner, R.array.cg_pitchunit_array, _scrPitchUnitListener);

        //_gearRatioLayout = (LinearLayout)_view.findViewById(R.id.gearRatioLayout);
        _gearRatio = (EditText)_view.findViewById(R.id.ratioValue);
        _gearRatio.addTextChangedListener(_ratioTextChangedListener);

        _gearRatioNumerator = (EditText)_view.findViewById(R.id.ratioNumeratorValue);
        _gearRatioNumerator.addTextChangedListener(_ratioNumeratorTextChangedListener);

        _gearRatioDenominator = (EditText)_view.findViewById(R.id.ratioDenominatorValue);
        _gearRatioDenominator.addTextChangedListener(_ratioDenominatorTextChangedListener);

        _gearRatioFractionLayout = (LinearLayout)_view.findViewById(R.id.ratioFractionLayout);
        _ratioFormattedText = (TextView)_view.findViewById(R.id.ratioCalculated);

        _ratioAsFractionSwitch = (CompoundButton)_view.findViewById(R.id.ratioFractionSwitch);
        _ratioAsFractionSwitch.setOnClickListener(new android.view.View.OnClickListener()
            {
                @Override
                public void onClick(android.view.View view)
                {
                    boolean isChecked = ((CompoundButton)view).isChecked();
                    _presenter.setRatioAsFraction(isChecked);
                }
            });

        _resultFirstNumberText = (TextView)_view.findViewById(R.id.resultFirstNumberText);
        _resultLastNumberText = (TextView)_view.findViewById(R.id.resultLastNumberText);
        _resultView = (ViewGroup)_view.findViewById(R.id.resultsLayout);

        ImageButton showNextButton = (ImageButton)_view.findViewById(R.id.showNext);
        showNextButton.setOnClickListener(new android.view.View.OnClickListener()
            {
                @Override
                public void onClick(android.view.View p1)
                {
                    _presenter.getNextResults();
                }
            });

        ImageButton showPrevButton = (ImageButton)_view.findViewById(R.id.showPrev);
        showPrevButton.setOnClickListener(new android.view.View.OnClickListener()
            {
                @Override
                public void onClick(android.view.View p1)
                {
                    _presenter.getPrevResults();
                }
            });

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

    private void requestGearKit(GearSetView gsView)
    {
        final GearSetView gsv = gsView;

        FragmentManager fragmentManager = Activity.getSupportFragmentManager();
        final GearPickerDialog dialog = new GearPickerDialog();
        dialog.setGears(gsv.gears());
        dialog.setResultListener(new DialogBase.ResultListener()
            {
                @Override
                public void onPositive()
                {
                    ArrayList<Integer> teethNumbers = dialog.getGearsList();
                    _presenter.setGearKit(gsv.id(), teethNumbers);
                }

                @Override
                public void onNegative()
                {}
            });
        dialog.show(fragmentManager, "teethnumbersdialog");
    }

    private void setResultItem(final Contract.Result result)
    {
        try
        {
            LayoutInflater layoutInflater = (LayoutInflater)Activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            android.view.View view = layoutInflater.inflate(R.layout.view_changegears_result2, null);

            TextView text = (TextView)view.findViewById(R.id.resultNumberText);
            text.setText(result.id());

            int visibility;
            text = (TextView)view.findViewById(R.id.z1Text);
            text.setText(result.z1());
            text = (TextView)view.findViewById(R.id.z2Text);
            text.setText(result.z2());

            visibility = result.z3() != null ? android.view.View.VISIBLE : android.view.View.GONE;
            view.findViewById(R.id.gears34Layout).setVisibility(visibility);
            if (visibility == android.view.View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.z3Text);
                text.setText(result.z3());
                text = (TextView)view.findViewById(R.id.z4Text);
                text.setText(result.z4());
            }

            visibility = result.z5() != null ? android.view.View.VISIBLE : android.view.View.GONE;
            view.findViewById(R.id.gears56Layout).setVisibility(visibility);
            if (visibility == android.view.View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.z5Text);
                text.setText(result.z5());
                text = (TextView)view.findViewById(R.id.z6Text);
                text.setText(result.z6());
            }

            text = (TextView)view.findViewById(R.id.ratioText);
            text.setText(result.ratio());

            visibility = result.threadPitch() != null ? android.view.View.VISIBLE : android.view.View.GONE;
            view.findViewById(R.id.threadPitchLayout).setVisibility(visibility);
            if (visibility == android.view.View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.threadPitchText);
                text.setText(result.threadPitch());
            }

            _resultView.addView(view);
        }
        catch (Exception e)
        {
            //
        }
    }
}
