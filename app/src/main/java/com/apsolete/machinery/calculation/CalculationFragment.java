package com.apsolete.machinery.calculation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.gearing.changegears.ChangeGearsViewModel;
import com.apsolete.machinery.common.CustomFragment;
import com.apsolete.machinery.common.CustomViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class CalculationFragment<VM extends CalculationViewModel> extends CustomFragment<VM>
{
    private ProgressBar ProgressBar;
    private int _type;

    public CalculationFragment(int type, int layout, int title, Class<VM> vmClass)
    {
        super(layout, title, vmClass);
        _type = type;
    }

    @Override
    public VM getViewModel()
    {
        return mViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;
        ProgressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    public int type()
    {
        return _type;
    }

    protected void showProgress(int progress)
    {
        if (ProgressBar.getVisibility() == View.GONE)
            ProgressBar.setVisibility(View.VISIBLE);
        ProgressBar.setProgress(progress);
    }

    protected void resetProgress()
    {
        ProgressBar.setProgress(0);
        ProgressBar.setVisibility(View.GONE);
    }


    public static NumberFormat getNumberFormat(String pattern)
    {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat _numFormat = new DecimalFormat(pattern, formatSymbols);
        _numFormat.setRoundingMode(RoundingMode.CEILING);
        return _numFormat;
    }

}
