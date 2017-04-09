package com.apsolete.machinery.activity.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import com.apsolete.machinery.activity.*;

import java.util.ArrayList;

public class ChangeGearsCalculation extends CalculationContent
{
    public interface OnGearSetListener
    {
        void onSelectGears(int id);
        void onGearsChanged(int id);
    }

    public class GearSetControl implements View.OnClickListener, TextWatcher
    {
        private OnGearSetListener _gearSetListener;
        private int _gearId;
        private Button _gearsButton;
        private EditText _gearsText;

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
            _gearSetListener.onGearsChanged(_gearId);
        }

        public void setEnabled(Boolean enabled)
        {
            _gearsButton.setEnabled(enabled);
            _gearsText.setEnabled(enabled);
        }
    }

    private static final int Z1 = 1;
    private static final int Z2 = 2;
    private static final int Z3 = 3;
    private static final int Z4 = 4;
    private static final int Z5 = 5;
    private static final int Z6 = 6;
    private EditText _z1Gears;
    private EditText _z2Gears;
    private EditText _z3Gears;
    private EditText _z4Gears;
    private EditText _z5Gears;
    private EditText _z6Gears;

    private GearSetControl _z1Control;
    private GearSetControl _z2Control;
    private GearSetControl _z3Control;
    private GearSetControl _z4Control;
    private GearSetControl _z5Control;
    private GearSetControl _z6Control;

    private OnGearSetListener _gearSetListener = new OnGearSetListener()
    {
        @Override
        public void onSelectGears(int id)
        {
            switch (id)
            {
                case Z1:
                    setGearsSet(Z1, _z1Gears);
                    break;
                case Z2:
                    setGearsSet(Z2, _z2Gears);
                    break;
                case Z3:
                    setGearsSet(Z3, _z3Gears);
                    break;
                case Z4:
                    setGearsSet(Z4, _z4Gears);
                    break;
                case Z5:
                    setGearsSet(Z5, _z5Gears);
                    break;
                case Z6:
                    setGearsSet(Z6, _z6Gears);
                    break;
            }
        }

        @Override
        public void onGearsChanged(int id)
        {
            switch (id)
            {
                case Z1:
                    break;
                case Z2:
                    _z3Control.setEnabled(true);
                    break;
                case Z3:
                    _z4Control.setEnabled(true);
                    break;
                case Z4:
                    _z5Control.setEnabled(true);
                    break;
                case Z5:
                    _z6Control.setEnabled(true);
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

        _z1Gears = (EditText) v.findViewById(R.id.z1Gears);
        Button z1Button = (Button)v.findViewById(R.id.z1Set);
        _z1Control = new GearSetControl(Z1, z1Button, _z1Gears, _gearSetListener);

        _z2Gears = (EditText) v.findViewById(R.id.z2Gears);
        Button z2Button = (Button)v.findViewById(R.id.z2Set);
        _z2Control = new GearSetControl(Z2, z2Button, _z2Gears, _gearSetListener);

        _z3Gears = (EditText) v.findViewById(R.id.z3Gears);
        Button z3Button = (Button)v.findViewById(R.id.z3Set);
        _z3Control = new GearSetControl(Z3, z3Button, _z3Gears, _gearSetListener);

        _z4Gears = (EditText) v.findViewById(R.id.z4Gears);
        Button z4Button = (Button)v.findViewById(R.id.z4Set);
        _z4Control = new GearSetControl(Z4, z4Button, _z4Gears, _gearSetListener);

        _z5Gears = (EditText) v.findViewById(R.id.z5Gears);
        Button z5Button = (Button)v.findViewById(R.id.z5Set);
        _z5Control = new GearSetControl(Z5, z5Button, _z5Gears, _gearSetListener);

        _z6Gears = (EditText) v.findViewById(R.id.z6Gears);
        Button z6Button = (Button)v.findViewById(R.id.z6Set);
        _z6Control = new GearSetControl(Z6, z6Button, _z6Gears, _gearSetListener);

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

    private void setGearsSet(int gearset, final EditText gears)
    {
        FragmentManager fragmentManager = _activity.getSupportFragmentManager();
        final TeethNumbersDialog dialog = new TeethNumbersDialog();
        dialog.setSelection(gears.getText().toString());
        dialog.setResultListener(new DialogBase.ResultListener()
        {
            @Override
            public void onPositive()
            {
                ArrayList<Integer> teethNumbers = dialog.getTeethNumbers();
                if (teethNumbers != null)
                {
                    String text = new String();
                    for (Integer n : teethNumbers)
                    {
                        text += n.toString();
                        text += " ";
                    }
                    gears.setText(text);
                }
            }

            @Override
            public void onNegative(){}
        });
        dialog.show(fragmentManager, "dialog");
    }
}
