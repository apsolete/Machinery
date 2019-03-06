package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.utils.*;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.util.*;

import java.util.ArrayList;

public class TeethNumbersDialog extends DialogBase
{
    private static final int COLUMNS = 5;
    private static final int TEETH_MIN = 13;
    private static final int TEETH_MAX = 132;

    private ArrayList<Integer> _teethNumbers = null;
    private ArrayMap<Integer, CompoundButton> _tglButtons = new ArrayMap<Integer, CompoundButton>();

    private GridLayout _grid;

    private View.OnClickListener _clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.buttonSelectAll:
                    selectall();
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

    public TeethNumbersDialog()
    {
        super(R.layout.teeth_numbers);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        _grid = (GridLayout)view.findViewById(R.id.teethNumbersGrid);
        _grid.setColumnCount(COLUMNS);
        _grid.setRowCount((TEETH_MAX - TEETH_MIN) / COLUMNS + 1);

        if (_grid != null)
        {
            for (int t = TEETH_MIN; t <= TEETH_MAX; t++)
            {
                ToggleButton button = new ToggleButton(_grid.getContext());
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setText(String.valueOf(t));
                button.setTextOn(String.valueOf(t));
                button.setTextOff(String.valueOf(t));
                button.setPadding(2, 2, 2, 2);
                button.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.toglebuttonstatecolors));
                _tglButtons.put(t, button);
                _grid.addView(button);
            }
        }
        
        if (_teethNumbers != null && !_teethNumbers.isEmpty())
        {
            for (Integer n: _teethNumbers)
            {
                CompoundButton cb = _tglButtons.get(n);
                if (cb != null)
                    cb.setChecked(true);
            }
        }

        ((Button)view.findViewById(R.id.buttonSelectAll)).setOnClickListener(_clickListener);
        ((Button)view.findViewById(R.id.buttonReset)).setOnClickListener(_clickListener);
        ((Button)view.findViewById(R.id.buttonOk)).setOnClickListener(_clickListener);
        ((Button)view.findViewById(R.id.buttonCancel)).setOnClickListener(_clickListener);

        return view;
    }

    public ArrayList<Integer> getTeethNumbers()
    {
        return _teethNumbers;
    }

    private void selectall()
    {
        int count = _grid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CompoundButton cb = (CompoundButton)_grid.getChildAt(i);
            if (cb != null)
                cb.setChecked(true);
        }
    }

    private void reset()
    {
        int count = _grid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CompoundButton cb = (CompoundButton)_grid.getChildAt(i);
            if (cb != null)
                cb.setChecked(false);
        }
    }

    private void apply()
    {
        _teethNumbers = new ArrayList<>();
        int count = _grid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CompoundButton cb = (CompoundButton)_grid.getChildAt(i);
            if (cb != null && cb.isChecked())
            {
                int teeth = Integer.parseInt(cb.getText().toString());
                _teethNumbers.add(teeth);
            }
        }
        dismiss();
        _resultListener.onPositive();
    }

    private void cancel()
    {
        _teethNumbers = null;
        _resultListener.onNegative();
        dismiss();
    }

    public void setSelection(ArrayList<Integer> selection)
    {
        _teethNumbers = new ArrayList<>(selection);
    }

    public void setSelection(String selection)
    {
        _teethNumbers = Numbers.getNumbersList(selection);
    }
}
