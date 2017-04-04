package com.apsolete.machinery.activity.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
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

    private View.OnClickListener _clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int id = view.getId();
            switch (id)
            {
                case R.id.z1Gears:
                    setGearsSet((EditText)view, Z1);
                    break;
                case R.id.z2Gears:
                    setGearsSet((EditText)view, Z2);
                    break;
                case R.id.z3Gears:
                    setGearsSet((EditText)view, Z3);
                    break;
                case R.id.z4Gears:
                    setGearsSet((EditText)view, Z4);
                    break;
                case R.id.z5Gears:
                    setGearsSet((EditText)view, Z5);
                    break;
                case R.id.z6Gears:
                    setGearsSet((EditText)view, Z6);
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

        View edit = v.findViewById(R.id.z1Gears);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z2Gears);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z3Gears);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z4Gears);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z5Gears);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.z6Gears);
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

    private void setGearsSet(final EditText view, int gearset)
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
