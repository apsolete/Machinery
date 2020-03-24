package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.utils.*;

import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.util.*;

import java.util.ArrayList;

public class GearSetPickerDialog extends DialogBase
{
    private static final int COLUMNS = 5;
    private static final int TEETH_MIN = 13;
    private static final int TEETH_MAX = 132;

    private ArrayList<Integer> mGearSet = null;
    private ArrayMap<Integer, CompoundButton> mButtons = new ArrayMap<Integer, CompoundButton>();

    private GridLayout mGrid;

    private View.OnClickListener _clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.buttonSelectAll:
                    selectAll();
                    break;
                case R.id.buttonReset:
                    reset();
                    break;
                case R.id.buttonOk:
                    apply();
                    break;
                case R.id.buttonCancel:
                    cancel();
                    break;
            }
        }
    };

    public GearSetPickerDialog()
    {
        super(R.layout.gearset_picker);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        assert view != null;
        mGrid = view.findViewById(R.id.gearsGrid);
        mGrid.setColumnCount(COLUMNS);
        mGrid.setRowCount((TEETH_MAX - TEETH_MIN) / COLUMNS + 1);

        if (mGrid != null)
        {
            for (int t = TEETH_MIN; t <= TEETH_MAX; t++)
            {
                ToggleButton button = new ToggleButton(mGrid.getContext(), null, R.attr.buttonBarButtonStyle);
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setText(String.valueOf(t));
                button.setTextOn(String.valueOf(t));
                button.setTextOff(String.valueOf(t));
                button.setTag(t);
                button.setPadding(2, 2, 2, 2);
                button.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.toglebuttonstatecolors));
                mButtons.put(t, button);
                mGrid.addView(button);
            }
        }
        
        if (mGearSet != null && !mGearSet.isEmpty())
        {
            for (Integer n: mGearSet)
            {
                CompoundButton cb = mButtons.get(n);
                if (cb != null)
                    cb.setChecked(true);
            }
        }

        view.findViewById(R.id.buttonSelectAll).setOnClickListener(_clickListener);
        view.findViewById(R.id.buttonReset).setOnClickListener(_clickListener);
        view.findViewById(R.id.buttonOk).setOnClickListener(_clickListener);
        view.findViewById(R.id.buttonCancel).setOnClickListener(_clickListener);

        return view;
    }

    public ArrayList<Integer> getGearsList()
    {
        return mGearSet;
    }

    public Integer[] getGears()
    {
        Integer[] gears = getGearsList().toArray(new Integer[]{});
        return gears;
    }

    private void selectAll()
    {
        int count = mGrid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CompoundButton cb = (CompoundButton) mGrid.getChildAt(i);
            if (cb != null)
                cb.setChecked(true);
        }
    }

    private void reset()
    {
        int count = mGrid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CompoundButton cb = (CompoundButton) mGrid.getChildAt(i);
            if (cb != null)
                cb.setChecked(false);
        }
    }

    private void apply()
    {
        mGearSet = new ArrayList<>();
        int count = mGrid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CompoundButton cb = (CompoundButton) mGrid.getChildAt(i);
            if (cb != null && cb.isChecked())
            {
                int teeth = (int)cb.getTag();
                mGearSet.add(teeth);
            }
        }
        dismiss();
        _resultListener.onPositive();
    }

    private void cancel()
    {
        mGearSet = null;
        _resultListener.onNegative();
        dismiss();
    }

    public void setGears(ArrayList<Integer> gearSet)
    {
        mGearSet = new ArrayList<>(gearSet);
    }

    public void setGears(String gearSet)
    {
        mGearSet = Numbers.getNumbersList(gearSet);
    }
}
