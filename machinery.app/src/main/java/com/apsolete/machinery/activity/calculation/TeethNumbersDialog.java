package com.apsolete.machinery.activity.calculation;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.apsolete.machinery.activity.R;
import android.widget.*;
import android.widget.ActionMenuView.*;

public class TeethNumbersDialog extends DialogFragment
{
    private int[] _teethNumbers = new int[120];
    private final int _teethMin = 19;
    private final int _teethMax = 127;

    public TeethNumbersDialog()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.teeth_numbers, container, false);
        GridLayout grid = (GridLayout)view.findViewById(R.id.teethNumbersGrid);
        grid.setColumnCount(5);
        grid.setRowCount((_teethMax - _teethMin)/5 + 1);
        if (grid != null)
        {
            int col = 0, row = 0;
            
            for (int t = _teethMin; t <= _teethMax; t++)
            {
                CheckBox checkBox = new CheckBox(grid.getContext());
                checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                checkBox.setText(String.valueOf(t));
                checkBox.setPadding(2,2,2,2);
                grid.addView(checkBox);
                
                if (col < 6)
                    col++;
                else
                {
                    col = 0;
                    row++;
                }
                
            }
        }
        //grid.addView(
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
