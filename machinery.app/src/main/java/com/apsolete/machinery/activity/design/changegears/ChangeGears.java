package com.apsolete.machinery.activity.design.changegears;

import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.common.*;
import com.apsolete.machinery.activity.design.*;

import android.content.Context;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

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
    private final double _accuracy = 0.0001;
    private double _ratio = 0;
    private boolean _showResults = false;
    private boolean _watchResults = true;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;
    private boolean _oneSetForAll = false;

    private View _view;
    private CheckBox _oneSetCheckBox;
    private EditText _ratioEdText;
    private ViewGroup _resultView;
    private ProgressBar _pb;

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
        public void onCompleted()
        {
            _activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Snackbar.make(_view, "Completed.", Snackbar.LENGTH_SHORT).show();
                    }
                });

        }
    };

    public ChangeGears()
    {
        super(DesignType.ChangeGears, R.layout.content_changegears_design, R.string.title_change_gears_design);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _view = super.onCreateView(inflater, container, savedInstanceState);
        assert _view != null;

        _oneSetCheckBox = (CheckBox)_view.findViewById(R.id.oneSetForAllGears);
        _oneSetCheckBox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    setOneSetForAllGears();
                }
            });

        _ratioEdText = (EditText)_view.findViewById(R.id.gearRatio);

        _resultView = (ViewGroup)_view.findViewById(R.id.resultLayout);

        EditText z0Gears = (EditText) _view.findViewById(R.id.z0Gears);
        Button z0Button = (Button)_view.findViewById(R.id.z0Set);
        //CheckBox z1Select = (CheckBox)_view.findViewById(R.id.z1Select);
        _gearsCtrls[Z0] = new GearSetControl(Z0, z0Button, z0Gears, null, _gearSetListener);
        _gearsCtrls[Z0].setEnabled(_oneSetCheckBox.isChecked());

        EditText z1Gears = (EditText) _view.findViewById(R.id.z1Gears);
        Button z1Button = (Button)_view.findViewById(R.id.z1Set);
        CheckBox z1Select = (CheckBox)_view.findViewById(R.id.z1Select);
        _gearsCtrls[Z1] = new GearSetControl(Z1, z1Button, z1Gears, z1Select, _gearSetListener);

        EditText z2Gears = (EditText) _view.findViewById(R.id.z2Gears);
        Button z2Button = (Button)_view.findViewById(R.id.z2Set);
        CheckBox z2Select = (CheckBox)_view.findViewById(R.id.z2Select);
        _gearsCtrls[Z2] = new GearSetControl(Z2, z2Button, z2Gears, z2Select, _gearSetListener);

        EditText z3Gears = (EditText) _view.findViewById(R.id.z3Gears);
        Button z3Button = (Button)_view.findViewById(R.id.z3Set);
        CheckBox z3Select = (CheckBox)_view.findViewById(R.id.z3Select);
        _gearsCtrls[Z3] = new GearSetControl(Z3, z3Button, z3Gears, z3Select, _gearSetListener);

        EditText z4Gears = (EditText) _view.findViewById(R.id.z4Gears);
        Button z4Button = (Button)_view.findViewById(R.id.z4Set);
        CheckBox z4Select = (CheckBox)_view.findViewById(R.id.z4Select);
        _gearsCtrls[Z4] = new GearSetControl(Z4, z4Button, z4Gears, z4Select, _gearSetListener);

        EditText z5Gears = (EditText) _view.findViewById(R.id.z5Gears);
        Button z5Button = (Button)_view.findViewById(R.id.z5Set);
        CheckBox z5Select = (CheckBox)_view.findViewById(R.id.z5Select);
        _gearsCtrls[Z5] = new GearSetControl(Z5, z5Button, z5Gears, z5Select, _gearSetListener);

        EditText z6Gears = (EditText) _view.findViewById(R.id.z6Gears);
        Button z6Button = (Button)_view.findViewById(R.id.z6Set);
        CheckBox z6Select = (CheckBox)_view.findViewById(R.id.z6Select);
        _gearsCtrls[Z6] = new GearSetControl(Z6, z6Button, z6Gears, z6Select, _gearSetListener);

        _pb = (ProgressBar)_view.findViewById(R.id.progressBar);
        ImageButton calc = (ImageButton)_view.findViewById(R.id.calculate);
        calc.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    calculate();
                }
            });

        _gearsCtrls[Z1].setEnabled(true);
        _gearsCtrls[Z2].setEnabled(true);
        _gearsCtrls[Z3].setEnabled(false);
        _gearsCtrls[Z4].setEnabled(false);
        _gearsCtrls[Z5].setEnabled(false);
        _gearsCtrls[Z6].setEnabled(false);

        return _view;
    }

    @Override
    public void onDetach()
    {
        _watchResults = false;
        super.onDetach();
    }

    @Override
    public SettingsFragment getSettings()
    {
        return new ChangeGearsSettings();
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

        if (_oneSetForAll)
        {
            int[] set = _gearsCtrls[Z0].getGears();
            int[] gears = new int[]{2};
            if (_gearsCtrls[Z6].isChecked())
                gears[0] = 6;
            else if (_gearsCtrls[Z4].isChecked())
                gears[0] = 4;

            CgCalculator calc = new CgCalculator(_ratio, _accuracy, _diffTeethGearing,
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

            CgCalculator calc = new CgCalculator(_ratio, _accuracy, _diffTeethGearing,
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
                        String text = "";
                        for (Integer n : teethNumbers)
                        {
                            text += n.toString();
                            text += " ";
                        }
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
            text.setText(Double.toString(ratio));

            _resultView.addView(view);
        }
        catch (Exception e)
        {
            //
        }
    }

    private void setOneSetForAllGears()
    {
        _oneSetForAll = !_oneSetForAll;

        _gearsCtrls[Z0].enableOwnSet(_oneSetForAll);
        _gearsCtrls[Z0].setEnabled(_oneSetForAll);

        _gearsCtrls[Z1].enableOwnSet(!_oneSetForAll);
        _gearsCtrls[Z2].enableOwnSet(!_oneSetForAll);
        _gearsCtrls[Z3].enableOwnSet(!_oneSetForAll);
        _gearsCtrls[Z4].enableOwnSet(!_oneSetForAll);
        _gearsCtrls[Z5].enableOwnSet(!_oneSetForAll);
        _gearsCtrls[Z6].enableOwnSet(!_oneSetForAll);

        if (_oneSetForAll)
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
}
