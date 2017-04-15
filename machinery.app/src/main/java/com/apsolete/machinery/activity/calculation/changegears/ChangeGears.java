package com.apsolete.machinery.activity.calculation.changegears;

import com.apsolete.machinery.activity.*;
import android.content.Context;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import android.text.*;

import com.apsolete.machinery.activity.calculation.CalculationContent;
import com.apsolete.machinery.activity.util.*;

public class ChangeGears extends CalculationContent
{

    private class Result
    {
        public double Ratio;
        public int[] Gears = new int[6];
    }

    private static final int Z1 = 1;
    private static final int Z2 = 2;
    private static final int Z3 = 3;
    private static final int Z4 = 4;
    private static final int Z5 = 5;
    private static final int Z6 = 6;
    private final double _accuracy = 0.0001;
    private double _ratio = 0;
    private boolean _showResults = false;
    private boolean _watchResults = true;

    private View _view;
    private EditText _ratioEdText;
    private ViewGroup _resultView;
    private ProgressBar _pb;

    private final HashMap<Integer, GearSetControl> _gearsControls = new HashMap<>(6);
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
                    _gearsControls.get(Z3).setEnabled(!empty);
                    break;
                case Z3:
                    _gearsControls.get(Z4).setEnabled(!empty);
                    break;
                case Z4:
                    _gearsControls.get(Z5).setEnabled(!empty);
                    break;
                case Z5:
                    _gearsControls.get(Z6).setEnabled(!empty);
                    break;
                case Z6:
                    break;
            }
            //calculate();
        }
    };

    public ChangeGears()
    {
        super(R.layout.content_calc_change_gears, R.string.title_calc_change_gears);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _view = super.onCreateView(inflater, container, savedInstanceState);
        assert _view != null;

        _ratioEdText = (EditText)_view.findViewById(R.id.gearRatio);
//        _ratioEdText.addTextChangedListener(new TextChangedListener()
//            {
//                @Override
//                public void onTextChanged(Editable editable)
//                {
//                    calculate();
//                }
//            });
        _resultView = (ViewGroup)_view.findViewById(R.id.resultLayout);

        EditText z1Gears = (EditText) _view.findViewById(R.id.z1Gears);
        Button z1Button = (Button)_view.findViewById(R.id.z1Set);
        _gearsControls.put(Z1, new GearSetControl(Z1, z1Button, z1Gears, _gearSetListener));

        EditText z2Gears = (EditText) _view.findViewById(R.id.z2Gears);
        Button z2Button = (Button)_view.findViewById(R.id.z2Set);
        _gearsControls.put(Z2, new GearSetControl(Z2, z2Button, z2Gears, _gearSetListener));

        EditText z3Gears = (EditText) _view.findViewById(R.id.z3Gears);
        Button z3Button = (Button)_view.findViewById(R.id.z3Set);
        _gearsControls.put(Z3, new GearSetControl(Z3, z3Button, z3Gears, _gearSetListener));

        EditText z4Gears = (EditText) _view.findViewById(R.id.z4Gears);
        Button z4Button = (Button)_view.findViewById(R.id.z4Set);
        _gearsControls.put(Z4, new GearSetControl(Z4, z4Button, z4Gears, _gearSetListener));

        EditText z5Gears = (EditText) _view.findViewById(R.id.z5Gears);
        Button z5Button = (Button)_view.findViewById(R.id.z5Set);
        _gearsControls.put(Z5, new GearSetControl(Z5, z5Button, z5Gears, _gearSetListener));

        EditText z6Gears = (EditText) _view.findViewById(R.id.z6Gears);
        Button z6Button = (Button)_view.findViewById(R.id.z6Set);
        _gearsControls.put(Z6, new GearSetControl(Z6, z6Button, z6Gears, _gearSetListener));

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

//        Thread watchResultsThread = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                while (_watchResults)
//                {
//                    if (_showResults)
//                    {
//                        showResults();
//                    }
//                }
//            }
//        });
//        watchResultsThread.start();

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
        // TODO: Implement this method
    }

    @Override
    protected void calculate()
    {
        String ratioStr = _ratioEdText.getText().toString();
        _ratio = (ratioStr != null && !ratioStr.isEmpty()) ? Double.parseDouble(ratioStr) : 0;

        //if (_ratio > 0)
        {
            calculateInternal();
            //_showResults = true;
            showResults();
        }
    }

    private void calculateInternal()
    {
        ArrayList<Integer> z1Gears = _gearsControls.get(Z1).getGears();
        ArrayList<Integer> z2Gears = _gearsControls.get(Z2).getGears();
        ArrayList<Integer> z3Gears = _gearsControls.get(Z3).getGears();
        ArrayList<Integer> z4Gears = _gearsControls.get(Z4).getGears();
        ArrayList<Integer> z5Gears = _gearsControls.get(Z5).getGears();
        ArrayList<Integer> z6Gears = _gearsControls.get(Z6).getGears();

        if (z1Gears.isEmpty() || z2Gears.isEmpty())
            return;
        else if (!z1Gears.isEmpty() && !z2Gears.isEmpty())
        {
            if (z3Gears.isEmpty() || z4Gears.isEmpty())
            {
                _pb.setMax(z1Gears.size() * z2Gears.size());
                clear();
                int p = 1;
                // calculate by z1, z2
                for (Integer z1: z1Gears)
                {
                    for (Integer z2: z2Gears)
                    {
                        _pb.setProgress(p++);
                        if (_ratio == 1 && z1 == z2)
                        {
                            if (!setResult(1, z1, z2, 0, 0, 0, 0))
                                break;
                        }
                        else
                        {
                            double ratio = (double)z1 / (double)z2;
                            if (checkRatio(ratio))
                            {
                                if (!setResult(ratio, z1, z2, 0, 0, 0, 0))
                                    break;
                            }
                        }
                    }
                }
            }
            else if (!z3Gears.isEmpty() && !z4Gears.isEmpty())
            {
                if (z5Gears.isEmpty() || z6Gears.isEmpty())
                {
                    clear();
                    // calculate by z1, z2, z3, z4
                    for (Integer z1: z1Gears)
                    {
                        for (Integer z2: z2Gears)
                        {
                            for (Integer z3: z3Gears)
                            {
                                for (Integer z4: z4Gears)
                                {
                                    double ratio = (double)(z1 * z3) / (double)(z2 * z4);
                                    if (checkRatio(ratio))
                                    {
                                        if (!setResult(ratio, z1, z2, z3, z4, 0, 0))
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
                else if (!z5Gears.isEmpty() && !z6Gears.isEmpty())
                {
                    clear();
                    // calculate by z1, z2, z3, z4, z6
                    for (Integer z1: z1Gears)
                    {
                        for (Integer z2: z2Gears)
                        {
                            for (Integer z3: z3Gears)
                            {
                                for (Integer z4: z4Gears)
                                {
                                    for (Integer z5: z5Gears)
                                    {
                                        for (Integer z6: z6Gears)
                                        {
                                            double ratio = (double)(z1 * z3 * z5) / (double)(z2 * z4 * z6);
                                            if (checkRatio(ratio))
                                            {
                                                if (!setResult(ratio, z1, z2, z3, z4, z5, z6))
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean checkRatio(double ratio)
    {
        if (_ratio == 0)
            return true;
        return (Math.abs(ratio - _ratio) <= _accuracy) ? true : false;
    }

    private void selectGears(int zId)
    {
        final GearSetControl control = _gearsControls.get(zId);

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

    private boolean setResult(double ratio, int z1, int z2, int z3, int z4, int z5, int z6)
    {
        if (_results.size() < 100)
        {
            Result res = new Result();
            res.Ratio = ratio;
            res.Gears[0] = z1;
            res.Gears[1] = z2;
            res.Gears[2] = z3;
            res.Gears[3] = z4;
            res.Gears[4] = z5;
            res.Gears[5] = z6;
            _results.add(res);

            return true;
        }

        Snackbar.make(_view, "Too much results. Only 100 are shown.", Snackbar.LENGTH_SHORT).show();
        return false;
    }

    private void showResults()
    {
        int count = 0;
        for (Result res: _results)
        {
            count++;
            if (count > 100)
                break;
            setResultItem(res.Gears, res.Ratio);
        }
        _showResults = false;
    }

    private void setResultItem(int[] gears, double ratio)
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
