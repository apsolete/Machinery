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
    private static final int GEARSET00 = 0;
    private static final int GEARSET01 = 1;
    private static final int GEARSET02 = 2;
    private static final int GEARSET03 = 3;
    private static final int GEARSET10 = 10;
    private static final int GEARSET11 = 11;
    private static final int GEARSET12 = 12;
    private static final int GEARSET13 = 13;
    private static final int GEARSET20 = 20;
    private static final int GEARSET21 = 21;
    private static final int GEARSET22 = 22;
    private static final int GEARSET23 = 23;

    private View.OnClickListener _clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int id = view.getId();
            switch (id)
            {
                case R.id.gearset00:
                    setGearsSet(view, GEARSET00);
                    break;
                case R.id.gearset01:
                    setGearsSet(view, GEARSET01);
                    break;
                case R.id.gearset02:
                    setGearsSet(view, GEARSET02);
                    break;
                case R.id.gearset03:
                    setGearsSet(view, GEARSET03);
                    break;

                case R.id.gearset10:
                    setGearsSet(view, GEARSET10);
                    break;
                case R.id.gearset11:
                    setGearsSet(view, GEARSET11);
                    break;
                case R.id.gearset12:
                    setGearsSet(view, GEARSET12);
                    break;
                case R.id.gearset13:
                    setGearsSet(view, GEARSET13);
                    break;

                case R.id.gearset20:
                    setGearsSet(view, GEARSET20);
                    break;
                case R.id.gearset21:
                    setGearsSet(view, GEARSET21);
                    break;
                case R.id.gearset22:
                    setGearsSet(view, GEARSET22);
                    break;
                case R.id.gearset23:
                    setGearsSet(view, GEARSET23);
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

        View edit = v.findViewById(R.id.gearset00);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.gearset01);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.gearset02);
        edit.setOnClickListener(_clickListener);
        edit = v.findViewById(R.id.gearset03);
        edit.setOnClickListener(_clickListener);

//        ImageButton b = (ImageButton)v.findViewById(R.id.gearssetButton00);
//        b.setOnClickListener(new OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    FragmentManager fragmentManager = _activity.getSupportFragmentManager();
//                    TeethNumbersDialog newFragment = new TeethNumbersDialog();
//                    newFragment.show(fragmentManager, "dialog");
//                }
//            });

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

    private void setGearsSet(final View view, int gearset)
    {
        FragmentManager fragmentManager = _activity.getSupportFragmentManager();
        final TeethNumbersDialog dialog = new TeethNumbersDialog();
        dialog.setResultListener(new DialogBase.ResultListener()
        {
            @Override
            public void onPositive()
            {
                ArrayList<Integer> teethNumbers = dialog.getTeethNumbers();
                if (teethNumbers != null)
                {
                    EditText edit = (EditText)view;
                    String text = new String();
                    for (Integer n : teethNumbers)
                    {
                        text += n.toString();
                        text += " ";
                    }
                    edit.setText(text);
                }
            }

            @Override
            public void onNegative(){}
        });
        dialog.show(fragmentManager, "dialog");

    }
}
