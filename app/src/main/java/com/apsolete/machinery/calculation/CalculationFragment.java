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
import com.apsolete.machinery.common.Event;
import com.apsolete.machinery.common.Observers;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

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

        mViewModel.getNotificationEvent().observe(getViewLifecycleOwner(),
                new Observers.EventObserver<>(this::displayMessage));

        mViewModel.getCalculationEvent().observe(getViewLifecycleOwner(), new Observers.EventObserver<UUID>(id ->
        {
            final LiveData<WorkInfo> workInfo = WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(id);
            workInfo.observe(getViewLifecycleOwner(), info ->
            {
                if (info != null)
                {
                    if (info.getState() == WorkInfo.State.SUCCEEDED)
                        displayMessage("Calculation finished");
                    Data progress = info.getProgress();
                    int value = progress.getInt("PROGRESS", 0);
                    showProgress(value);
                }
            });
        }));

        return view;
    }

    public int type()
    {
        return _type;
    }

    protected void displayMessage(String message)
    {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
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
