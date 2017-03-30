package com.apsolete.machinery.activity.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.apsolete.machinery.activity.*;

public class ChangeGearsCalculation extends CalculationContent
{
    public ChangeGearsCalculation()
    {
        super(R.layout.content_calc_change_gears, R.string.title_calc_change_gears);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: Implement this method
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ImageButton b = (ImageButton)v.findViewById(R.id.gearssetButton00);
        b.setOnClickListener(new OnClickListener()
            {
                public void onClick(View v)
                {
                    FragmentManager fragmentManager = _activity.getSupportFragmentManager();
                    TeethNumbersDialog newFragment = new TeethNumbersDialog();
                    newFragment.show(fragmentManager, "dialog");
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

}
