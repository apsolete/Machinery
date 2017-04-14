package com.apsolete.machinery.activity.calculation;

import com.apsolete.machinery.activity.*;
import android.content.Context;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import android.text.*;
import com.apsolete.machinery.activity.util.*;

public class ChangeGearsCalculation extends CalculationContent
{
    public interface OnGearSetListener
    {
        void onSelectGears(int id);
        void onGearsChanged(int id, boolean empty);
    }

    public class GearSetControl extends TextChangedListener implements View.OnClickListener, InputFilter
    {
        private final OnGearSetListener _gearSetListener;
        private final int _gearId;
        private final Button _gearsButton;
        private final EditText _gearsText;

        public GearSetControl(int id, Button button, EditText text, OnGearSetListener listener)
        {
            _gearId = id;
            _gearSetListener = listener;

            _gearsButton = button;
            _gearsButton.setOnClickListener(this);

            _gearsText = text;
            _gearsText.addTextChangedListener(this);
            _gearsText.setFilters(new InputFilter[] { this });
            setError();
        }

        @Override
        public void onClick(View view)
        {
            _gearSetListener.onSelectGears(_gearId);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend)
        {
            StringBuilder filteredStringBuilder = new StringBuilder();
            for (int i = start; i < end; i++)
            { 
                char currentChar = source.charAt(i);
                if (Character.isDigit(currentChar) || Character.isSpaceChar(currentChar))
                {    
                    filteredStringBuilder.append(currentChar);
                }     
            }
            return filteredStringBuilder.toString();
        }

        @Override
        public void onTextChanged(Editable editable)
        {
            setError();
            _gearSetListener.onGearsChanged(_gearId, editable.length() == 0);
        }

        public void setEnabled(Boolean enabled)
        {
            if (!enabled && _gearsText.length() > 0)
                return;
            _gearsButton.setEnabled(enabled);
            _gearsText.setEnabled(enabled);
            setError();
        }

        public String getText()
        {
            return _gearsText.getText().toString();
        }

        public void setText(String text)
        {
            _gearsText.setText(text);
        }
        
        public ArrayList<Integer> getGears()
        {
            ArrayList<Integer> gears = new ArrayList<>();
            String text = getText();
            String[] strs = text.split(" ");
            for (String s: strs)
            {
                if (!s.isEmpty())
                {
                    int n = Integer.parseInt(s);
                    gears.add(n);
                }
            }
            return gears;
        }
        
        private void setError()
        {
            //if (_gearsText.isEnabled() && _gearsText.length() == 0)
            //    _gearsText.setError("Set gears");
        }
    }

    private static final int Z1 = 1;
    private static final int Z2 = 2;
    private static final int Z3 = 3;
    private static final int Z4 = 4;
    private static final int Z5 = 5;
    private static final int Z6 = 6;
    private final double _accuracy = 0.0001;
    private double _ratio = 0;

    private EditText _ratioEdText;
    private ViewGroup _resultView;
    private ProgressBar _pb;

    private final HashMap<Integer, GearSetControl> _gearsControls = new HashMap<>(6);
    //private final HashMap<Integer, ArrayList<Integer>> _gears = new HashMap<>(6);

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
            calculate();
        }
    };

    public ChangeGearsCalculation()
    {
        super(R.layout.content_calc_change_gears, R.string.title_calc_change_gears);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        assert v != null;

        _ratioEdText = (EditText)v.findViewById(R.id.gearRatio);
        _ratioEdText.addTextChangedListener(new TextChangedListener()
        {
                @Override
                public void onTextChanged(Editable editable)
                {
                    calculate();
                }
        });
        _resultView = (ViewGroup)v.findViewById(R.id.resultLayout);

        EditText z1Gears = (EditText) v.findViewById(R.id.z1Gears);
        Button z1Button = (Button)v.findViewById(R.id.z1Set);
        _gearsControls.put(Z1, new GearSetControl(Z1, z1Button, z1Gears, _gearSetListener));

        EditText z2Gears = (EditText) v.findViewById(R.id.z2Gears);
        Button z2Button = (Button)v.findViewById(R.id.z2Set);
        _gearsControls.put(Z2, new GearSetControl(Z2, z2Button, z2Gears, _gearSetListener));

        EditText z3Gears = (EditText) v.findViewById(R.id.z3Gears);
        Button z3Button = (Button)v.findViewById(R.id.z3Set);
        _gearsControls.put(Z3, new GearSetControl(Z3, z3Button, z3Gears, _gearSetListener));

        EditText z4Gears = (EditText) v.findViewById(R.id.z4Gears);
        Button z4Button = (Button)v.findViewById(R.id.z4Set);
        _gearsControls.put(Z4, new GearSetControl(Z4, z4Button, z4Gears, _gearSetListener));

        EditText z5Gears = (EditText) v.findViewById(R.id.z5Gears);
        Button z5Button = (Button)v.findViewById(R.id.z5Set);
        _gearsControls.put(Z5, new GearSetControl(Z5, z5Button, z5Gears, _gearSetListener));

        EditText z6Gears = (EditText) v.findViewById(R.id.z6Gears);
        Button z6Button = (Button)v.findViewById(R.id.z6Set);
        _gearsControls.put(Z6, new GearSetControl(Z6, z6Button, z6Gears, _gearSetListener));

        Button button = (Button)v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    setResultItem(new int[]{12,23,34,45,56,67}, 1.234);
                }
            });

        _pb = (ProgressBar)v.findViewById(R.id.progressBar);
        return v;
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
        Editable ratioEd = _ratioEdText.getText();
        String ratioStr = ratioEd != null ? ratioEd.toString() : null;
        _ratio = (ratioStr != null && !ratioStr.isEmpty()) ? Double.parseDouble(ratioStr.toString()) : 0;
        
        if (_ratio > 0)
            calculateInternal();
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
                _pb.setMax(z1Gears.size()*z2Gears.size());
                clear();
                int p = 1;
                // calculate by z1, z2
                for (Integer z1: z1Gears)
                {
                    for (Integer z2: z2Gears)
                    {
                        _pb.setProgress(p++);
                        double ratio = (double)z1 / (double)z2;
                        if (checkRatio(ratio))
                            setResultItem(new int[] {z1,z2,0,0,0,0}, ratio);
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
                                    double ratio = (double)z1 / (double)z2 * (double)z3 / (double)z4;
                                    if (checkRatio(ratio))
                                        setResultItem(new int[] {z1,z2,z3,z4,0,0}, ratio);
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
                                            double ratio = (double)z1 / (double)z2 * (double)z3 / (double)z4 * (double)z5 / (double)z6;
                                            if (checkRatio(ratio))
                                                setResultItem(new int[] {z1,z2,z3,z4,z5,z6}, ratio);
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
        //if (_ratio == 0)
        //    return true;
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

    private void setResultItem(int[] gears, double ratio)
    {
        //_pb.setProgress(1);
        LayoutInflater layoutInflater = (LayoutInflater)_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.change_gears_result, null);

        TextView text = (TextView)view.findViewById(R.id.z1Text);
        text.setText(gears[0] > 0 ? Integer.toString(gears[0]) : "");

        text = (TextView)view.findViewById(R.id.z2Text);
        text.setText(gears[1] > 0 ? Integer.toString(gears[1]) : "");

        text = (TextView)view.findViewById(R.id.z3Text);
        text.setText(gears[2] > 0 ? Integer.toString(gears[2]) : "");

        text = (TextView)view.findViewById(R.id.z4Text);
        text.setText(gears[3] > 0 ? Integer.toString(gears[3]) : "");

        text = (TextView)view.findViewById(R.id.z5Text);
        text.setText(gears[4] > 0 ? Integer.toString(gears[4]) : "");

        text = (TextView)view.findViewById(R.id.z6Text);
        text.setText(gears[5] > 0 ? Integer.toString(gears[5]) : "");

        text = (TextView)view.findViewById(R.id.ratioText);
        text.setText(Double.toString(ratio));

        _resultView.addView(view);
    }
}
