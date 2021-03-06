package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.utils.*;

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

    private ArrayList<Integer> _teethNumbers = null;
    private ArrayMap<Integer, CheckBox> _checkBoxes = new ArrayMap<Integer, CheckBox>();

    private final int _teethMin = 13;
    private final int _teethMax = 132;
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
        _grid.setRowCount((_teethMax - _teethMin) / COLUMNS + 1);

        if (_grid != null)
        {
            for (int t = _teethMin; t <= _teethMax; t++)
            {
                CheckBox checkBox = new CheckBox(_grid.getContext());
                checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setText(String.valueOf(t));
                checkBox.setPadding(2, 2, 2, 2);
                _checkBoxes.put(t, checkBox);
                _grid.addView(checkBox);
            }
        }
        
        if (_teethNumbers != null && !_teethNumbers.isEmpty())
        {
            for (Integer n: _teethNumbers)
            {
                CheckBox checkBox = _checkBoxes.get(n);
                if (checkBox != null)
                    checkBox.setChecked(true);
            }
        }

        Button button = (Button)view.findViewById(R.id.buttonSelectAll);
        button.setOnClickListener(_clickListener);

        button = (Button)view.findViewById(R.id.buttonReset);
        button.setOnClickListener(_clickListener);

        button = (Button)view.findViewById(R.id.buttonOk);
        button.setOnClickListener(_clickListener);

        button = (Button)view.findViewById(R.id.buttonCancel);
        button.setOnClickListener(_clickListener);

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
            CheckBox cb = (CheckBox)_grid.getChildAt(i);
            if (cb != null)
                cb.setChecked(true);
        }
    }

    private void reset()
    {
        int count = _grid.getChildCount();
        for (int i = 0; i < count; i++)
        {
            CheckBox cb = (CheckBox)_grid.getChildAt(i);
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
            CheckBox cb = (CheckBox)_grid.getChildAt(i);
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
