package com.apsolete.machinery.activity.design.changegears;

import com.apsolete.machinery.activity.*;
import android.content.Context;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.apsolete.machinery.activity.design.DesignContent;
import com.apsolete.machinery.activity.common.*;
import com.apsolete.machinery.activity.design.*;

public class ChangeGears extends DesignContent
{

    private class Result
    {
        public double Ratio;
        public int[] Gears = new int[6];
    }

    private static final int Z1 = 0;//1;
    private static final int Z2 = 1;//2;
    private static final int Z3 = 2;//3;
    private static final int Z4 = 3;//4;
    private static final int Z5 = 4;//5;
    private static final int Z6 = 5;//6;
    private final double _accuracy = 0.0001;
    private double _ratio = 0;
    private boolean _showResults = false;
    private boolean _watchResults = true;
    private boolean _deffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;

    private View _view;
    private EditText _ratioEdText;
    private ViewGroup _resultView;
    private ProgressBar _pb;

    private final GearSetControl[] _gearsControls = new GearSetControl[6];
    private final ArrayList<Result> _results = new ArrayList<>();

    private final OnGearSetListener _gearSetListener = new OnGearSetListener()
    {
        @Override
        public void onSelectGears(int id)
        {
            selectGears(id);
        }

        @Override
        public void onGearsChanged(int id, boolean empty)
        {
            switch (id)
            {
                case Z1:
                    break;
                case Z2:
                    _gearsControls[Z3].setEnabled(!empty);
                    break;
                case Z3:
                    _gearsControls[Z4].setEnabled(!empty);
                    break;
                case Z4:
                    _gearsControls[Z5].setEnabled(!empty);
                    break;
                case Z5:
                    _gearsControls[Z6].setEnabled(!empty);
                    break;
                case Z6:
                    break;
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

        _ratioEdText = (EditText)_view.findViewById(R.id.gearRatio);

        _resultView = (ViewGroup)_view.findViewById(R.id.resultLayout);

        EditText z1Gears = (EditText) _view.findViewById(R.id.z1Gears);
        Button z1Button = (Button)_view.findViewById(R.id.z1Set);
        _gearsControls[Z1] = new GearSetControl(Z1, z1Button, z1Gears, _gearSetListener);

        EditText z2Gears = (EditText) _view.findViewById(R.id.z2Gears);
        Button z2Button = (Button)_view.findViewById(R.id.z2Set);
        _gearsControls[Z2] = new GearSetControl(Z2, z2Button, z2Gears, _gearSetListener);

        EditText z3Gears = (EditText) _view.findViewById(R.id.z3Gears);
        Button z3Button = (Button)_view.findViewById(R.id.z3Set);
        _gearsControls[Z3] = new GearSetControl(Z3, z3Button, z3Gears, _gearSetListener);

        EditText z4Gears = (EditText) _view.findViewById(R.id.z4Gears);
        Button z4Button = (Button)_view.findViewById(R.id.z4Set);
        _gearsControls[Z4] = new GearSetControl(Z4, z4Button, z4Gears, _gearSetListener);

        EditText z5Gears = (EditText) _view.findViewById(R.id.z5Gears);
        Button z5Button = (Button)_view.findViewById(R.id.z5Set);
        _gearsControls[Z5] = new GearSetControl(Z5, z5Button, z5Gears, _gearSetListener);

        EditText z6Gears = (EditText) _view.findViewById(R.id.z6Gears);
        Button z6Button = (Button)_view.findViewById(R.id.z6Set);
        _gearsControls[Z6] = new GearSetControl(Z6, z6Button, z6Gears, _gearSetListener);

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

        return _view;
    }

    @Override
    public void onDetach()
    {
        _watchResults = false;
        super.onDetach();
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

        int[] gs1 = _gearsControls[Z1].getGears();
        int[] gs2 = _gearsControls[Z2].getGears();
        int[] gs3 = _gearsControls[Z3].getGears();
        int[] gs4 = _gearsControls[Z4].getGears();
        int[] gs5 = _gearsControls[Z5].getGears();
        int[] gs6 = _gearsControls[Z6].getGears();

        CgCalculator calc = new CgCalculator(_ratio, _accuracy, _deffTeethGearing, _diffTeethDoubleGear,
                                             _resultListener);
        calc.execute(gs1, gs2, gs3, gs4, gs5, gs6);
    }

    private void selectGears(int zId)
    {
        final GearSetControl control = _gearsControls[zId];

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
}
