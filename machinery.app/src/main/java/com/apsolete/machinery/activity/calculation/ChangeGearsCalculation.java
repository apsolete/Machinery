package com.apsolete.machinery.activity.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.apsolete.machinery.activity.*;

import java.util.ArrayList;

public class ChangeGearsCalculation extends CalculationContent
{
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

    private View.OnClickListener _clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int id = view.getId();
            switch (id)
            {
                case R.id.z1Set:
                    setGearsSet(Z1, _z1Gears);
                    break;
                case R.id.z2Set:
                    setGearsSet(Z2, _z2Gears);
                    break;
                case R.id.z3Set:
                    setGearsSet(Z3, _z3Gears);
                    break;
                case R.id.z4Set:
                    setGearsSet(Z4, _z4Gears);
                    break;
                case R.id.z5Set:
                    setGearsSet(Z5, _z5Gears);
                    break;
                case R.id.z6Set:
                    setGearsSet(Z6, _z6Gears);
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
        _z2Gears = (EditText) v.findViewById(R.id.z2Gears);
        _z3Gears = (EditText) v.findViewById(R.id.z3Gears);
        _z4Gears = (EditText) v.findViewById(R.id.z4Gears);
        _z5Gears = (EditText) v.findViewById(R.id.z5Gears);
        _z6Gears = (EditText) v.findViewById(R.id.z6Gears);

        View edit = v.findViewById(R.id.z1Set);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z2Set);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z3Set);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z4Set);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z5Set);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z6Set);
        edit.setOnClickListener(_clickListener);

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

    private void setGearsSet(int gearset, final EditText view)
    {
        FragmentManager fragmentManager = _activity.getSupportFragmentManager();
        final TeethNumbersDialog dialog = new TeethNumbersDialog();
        dialog.setSelection(view.getText().toString());
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
                    view.setText(text);
                }
            }

            @Override
            public void onNegative(){}
        });
        dialog.show(fragmentManager, "dialog");

    }
}
