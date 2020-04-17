package com.apsolete.machinery.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.lifecycle.MutableLiveData;

public final class Listeners
{
    public static abstract class ChangeListener<T>
    {
        private MutableLiveData<T> mLiveData;
        private boolean mIsListening = true;
        private boolean mIsBroadcasting = false;

        public ChangeListener(MutableLiveData<T> liveData)
        {
            mLiveData = liveData;
        }

        public void start()
        {
            mIsListening = true;
        }

        public void stop()
        {
            mIsListening = false;
        }

        protected void onChanged(T value)
        {
            mLiveData.setValue(value);
        }

        void onChangedInternal(T value)
        {
            if (!mIsListening)
                return;

            startBroadcasting();
            onChanged(value);
            stopBroadcasting();
        }

        boolean isBroadcasting()
        {
            return mIsBroadcasting;
        }

        void startBroadcasting()
        {
            mIsBroadcasting = true;
        }

        void stopBroadcasting()
        {
            mIsBroadcasting = false;
        }
    }

    public static class CheckedChangeListener extends ChangeListener<Boolean> implements CompoundButton.OnCheckedChangeListener
    {
        public CheckedChangeListener(MutableLiveData<Boolean> liveData)
        {
            super(liveData);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            onChangedInternal(isChecked);
        }
    }

    public static class TextChangeListener extends ChangeListener<String> implements TextWatcher
    {
        public TextChangeListener(MutableLiveData<String> liveData)
        {
            super(liveData);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            onChangedInternal(editable.toString());
        }
    }

    public static class DoubleChangeListener extends ChangeListener<Double> implements TextWatcher
    {
        public DoubleChangeListener(MutableLiveData<Double> liveData)
        {
            super(liveData);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            String str = editable.toString();
            double value = 0.0;
            try
            {
                value = !str.isEmpty() ? Double.parseDouble(str) : 0.0;
            }
            finally
            {
                onChangedInternal(value);
            }
        }
    }

    public static class IntegerChangeListener extends ChangeListener<Integer> implements TextWatcher
    {
        public IntegerChangeListener(MutableLiveData<Integer> liveData)
        {
            super(liveData);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            String str = editable.toString();
            int value = 0;
            try
            {
                value = !str.isEmpty() ? Integer.parseInt(str) : 0;
            }
            finally
            {
                onChangedInternal(value);
            }
        }
    }

    public static class LongChangeListener extends ChangeListener<Long> implements TextWatcher
    {
        public LongChangeListener(MutableLiveData<Long> liveData)
        {
            super(liveData);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            String str = editable.toString();
            long value = 0;
            try
            {
                value = !str.isEmpty() ? Long.parseLong(str) : 0;
            }
            finally
            {
                onChangedInternal(value);
            }
        }
    }

    public static class IntegerItemSelectListener extends ChangeListener<Integer> implements AdapterView.OnItemSelectedListener
    {
        public IntegerItemSelectListener(MutableLiveData<Integer> liveData)
        {
            super(liveData);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
        {
            onChangedInternal(pos);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {
        }
    }

    public static class EnumItemSelectListener<E extends Enum<E>> extends ChangeListener<E> implements AdapterView.OnItemSelectedListener
    {
        private E[] mValues;

        public EnumItemSelectListener(MutableLiveData<E> liveData, E[] values)
        {
            super(liveData);
            mValues = values;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
        {
            onChangedInternal(mValues[pos]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {
        }
    }
}
