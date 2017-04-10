package com.apsolete.machinery.activity.calculation;

import android.content.Context;
import android.os.*;
import android.support.v4.app.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import com.apsolete.machinery.activity.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeGearsCalculation extends CalculationContent
{
    public interface OnGearSetListener
    {
        void onSelectGears(int id);
        void onGearsChanged(int id, boolean empty);
    }

    public class GearSetControl implements View.OnClickListener, TextWatcher
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
        }

        @Override
        public void onClick(View view)
        {
            _gearSetListener.onSelectGears(_gearId);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            _gearSetListener.onGearsChanged(_gearId, editable.length() == 0);
        }

        public void setEnabled(Boolean enabled)
        {
            _gearsButton.setEnabled(enabled);
            _gearsText.setEnabled(enabled);
        }

        public String getText()
        {
            return _gearsText.getText().toString();
        }

        public void setText(String text)
        {
            _gearsText.setText(text);
        }
    }

    private static final int Z1 = 1;
    private static final int Z2 = 2;
    private static final int Z3 = 3;
    private static final int Z4 = 4;
    private static final int Z5 = 5;
    private static final int Z6 = 6;

    private ViewGroup _resultView;

    private final HashMap<Integer, GearSetControl> _gearsMap = new HashMap<>(6);

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
                    _gearsMap.get(Z3).setEnabled(!empty);
                    break;
                case Z3:
                    _gearsMap.get(Z4).setEnabled(!empty);
                    break;
                case Z4:
                    _gearsMap.get(Z5).setEnabled(!empty);
                    break;
                case Z5:
                    _gearsMap.get(Z6).setEnabled(!empty);
                    break;
                case Z6:
                    break;
            }
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

        _resultView = (ViewGroup)v.findViewById(R.id.resultLayout);

        EditText z1Gears = (EditText) v.findViewById(R.id.z1Gears);
        Button z1Button = (Button)v.findViewById(R.id.z1Set);
        _gearsMap.put(Z1, new GearSetControl(Z1, z1Button, z1Gears, _gearSetListener));

        EditText z2Gears = (EditText) v.findViewById(R.id.z2Gears);
        Button z2Button = (Button)v.findViewById(R.id.z2Set);
        _gearsMap.put(Z2, new GearSetControl(Z2, z2Button, z2Gears, _gearSetListener));

        EditText z3Gears = (EditText) v.findViewById(R.id.z3Gears);
        Button z3Button = (Button)v.findViewById(R.id.z3Set);
        _gearsMap.put(Z3, new GearSetControl(Z3, z3Button, z3Gears, _gearSetListener));

        EditText z4Gears = (EditText) v.findViewById(R.id.z4Gears);
        Button z4Button = (Button)v.findViewById(R.id.z4Set);
        _gearsMap.put(Z4, new GearSetControl(Z4, z4Button, z4Gears, _gearSetListener));

        EditText z5Gears = (EditText) v.findViewById(R.id.z5Gears);
        Button z5Button = (Button)v.findViewById(R.id.z5Set);
        _gearsMap.put(Z5, new GearSetControl(Z5, z5Button, z5Gears, _gearSetListener));

        EditText z6Gears = (EditText) v.findViewById(R.id.z6Gears);
        Button z6Button = (Button)v.findViewById(R.id.z6Set);
        _gearsMap.put(Z6, new GearSetControl(Z6, z6Button, z6Gears, _gearSetListener));

        Button button = (Button)v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setResultItem(new int[]{12,23,34,45,56,67}, 1.234);
            }
        });

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
        // TODO: Implement this method
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

    private void selectGears(int zId)
    {
        final GearSetControl control = _gearsMap.get(zId);

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
            public void onNegative(){}
        });
        dialog.show(fragmentManager, "dialog");
    }

    private void setResultItem(int[] gears, double ratio)
    {
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
