package com.apsolete.machinery.calculation;

import android.os.AsyncTask;
import android.os.Parcelable;

import com.apsolete.machinery.common.OnProgressListener;

public abstract class CalculationModel implements Parcelable, CalculationContract.Model
{
    class AsyncCalc extends AsyncTask<CalculationModel, Integer, Void>
    {
        @Override
        protected Void doInBackground(CalculationModel... params)
        {
            params[0].onCalculate();
            return null;
        }
    }

    private int _total = 1;
    private int _progress = 0;
    private OnProgressListener _progressListener;

    protected abstract void onCalculate();
    protected abstract void onLoad();

    @Override
    public void load()
    {

    }

    @Override
    public void calculate()
    {
        AsyncCalc asyncCalc = new AsyncCalc();
        asyncCalc.execute(this);
    }

    protected void setOnProgressListener(OnProgressListener listener)
    {
        if (listener == null)
            throw new NullPointerException();

        _progressListener = listener;
    }

    protected void publishProgress()
    {
        _progressListener.onProgress((100 * _progress) / _total);
        _progress++;
    }

    protected void resetProgress(int total)
    {
        _total = total;
        _progress = 0;
    }
}
