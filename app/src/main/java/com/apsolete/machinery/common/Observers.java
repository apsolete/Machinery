package com.apsolete.machinery.common;

import android.view.View;
import android.widget.AbsSpinner;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;

public final class Observers
{
    public static abstract class ViewObserver<V extends View, T> implements Observer<T>
    {
        View[] mViews;
        Listeners.ChangeListener<T> mListener = null;

        ViewObserver(V view)
        {
            mViews = new View[1];
            mViews[0] = view;
        }

        ViewObserver(V view, Listeners.ChangeListener<T> listener)
        {
            mViews = new View[1];
            mViews[0] = view;
            mListener = listener;
        }

        ViewObserver(V[] views)
        {
            mViews = views;
        }

        public V view()
        {
            return (V)mViews[0];
        }

        public V view(int i)
        {
            return (V)mViews[i];
        }

        protected void setListener(Listeners.ChangeListener<T> listener)
        {
            mListener = listener;
        }

        @Override
        public void onChanged(T value)
        {
            if (mListener != null)
            {
                if (mListener.isBroadcasting())
                    return;
                mListener.stop();
                try
                {
                    change(value);
                }
                finally
                {
                    mListener.start();
                }
            }
            else
            {
                change(value);
            }
        }

        protected abstract void change(T value);
    }

    public static abstract class ViewBoolObserver<V extends View> extends ViewObserver<V, Boolean>
    {
        boolean mInverse;

        ViewBoolObserver(V view, boolean inverse)
        {
            super(view);
            mInverse = inverse;
        }

        ViewBoolObserver(V[] views, boolean inverse)
        {
            super(views);
            mInverse = inverse;
        }
    }

    public static class VisibilityObserver extends ViewBoolObserver<View>
    {
        VisibilityObserver(View view, boolean inverse)
        {
            super(view, inverse);
        }

        VisibilityObserver(View[] views, boolean inverse)
        {
            super(views, inverse);
        }

        @Override
        public void change(Boolean value)
        {
            int visibility;
            if (!mInverse)
                visibility = value ? View.VISIBLE : View.GONE;
            else
                visibility = !value ? View.VISIBLE : View.GONE;
            for (View v : mViews)
            {
                v.setVisibility(visibility);
            }
        }
    }

    public static class EnableObserver extends ViewBoolObserver<View>
    {
        EnableObserver(View view, boolean inverse)
        {
            super(view, inverse);
        }

        EnableObserver(View[] views, boolean inverse)
        {
            super(views, inverse);
        }

        @Override
        public void change(Boolean value)
        {
            boolean enable = !mInverse ? value : !value;
            for (View v : mViews)
            {
                v.setEnabled(enable);
            }
        }
    }

    public static class CheckableObserver extends ViewBoolObserver<CompoundButton>
    {
        CheckableObserver(CompoundButton view, MutableLiveData<Boolean> data, boolean inverse)
        {
            super(view, inverse);
            Listeners.CheckedChangeListener listener = new Listeners.CheckedChangeListener(data);
            view.setOnCheckedChangeListener(listener);
            setListener(listener);
        }

        CheckableObserver(CompoundButton[] views, MutableLiveData<Boolean> data, boolean inverse)
        {
            super(views, inverse);
            Listeners.CheckedChangeListener listener = new Listeners.CheckedChangeListener(data);
            for (CompoundButton view: views)
            {
                view.setOnCheckedChangeListener(listener);
            }
            setListener(listener);
        }

        @Override
        public void change(Boolean value)
        {
            boolean checked = mInverse ? !value : value;
            for (int i = 0; i < mViews.length; i++)
            {
                view(i).setChecked(checked);
            }
        }
    }

    public static class TextObserver extends ViewObserver<TextView, String>
    {
        TextObserver(TextView view)
        {
            super(view);
        }

        @Override
        public void change(@Nullable String text)
        {
            view().setText(text);
        }
    }

    public static class EditTextObserver extends ViewObserver<EditText, String>
    {
        EditTextObserver(EditText view, MutableLiveData<String> data)
        {
            super(view);

            Listeners.TextChangeListener listener = new Listeners.TextChangeListener(data);
            view.addTextChangedListener(listener);
            setListener(listener);
        }

        @Override
        public void change(@Nullable String text)
        {
            view().setText(text);
        }
    }

    public static class EditTextDoubleObserver extends ViewObserver<EditText, Double>
    {
        EditTextDoubleObserver(EditText view, MutableLiveData<Double> data)
        {
            super(view);

            Listeners.DoubleChangeListener listener = new Listeners.DoubleChangeListener(data);
            view.addTextChangedListener(listener);
            setListener(listener);
        }

        @Override
        public void change(@Nullable Double value)
        {
            view().setText(value.toString());
        }
    }

    public static class EditTextIntegerObserver extends ViewObserver<EditText, Integer>
    {
        EditTextIntegerObserver(EditText view, MutableLiveData<Integer> data)
        {
            super(view);

            Listeners.IntegerChangeListener listener = new Listeners.IntegerChangeListener(data);
            view.addTextChangedListener(listener);
            setListener(listener);
        }

        @Override
        public void change(@Nullable Integer value)
        {
            view().setText(value.toString());
        }
    }

    public static class SpinnerObserver extends ViewObserver<AbsSpinner, Integer>
    {
        SpinnerObserver(AbsSpinner view, MutableLiveData<Integer> data)
        {
            super(view);

            Listeners.IntegerItemSelectListener listener = new Listeners.IntegerItemSelectListener(data);
            view.setOnItemSelectedListener(listener);
            setListener(listener);
        }

        @Override
        public void change(Integer pos)
        {
            view().setSelection(pos);
        }
    }

    public static class SpinnerEnumObserver<E extends Enum<E>> extends ViewObserver<AbsSpinner, E>
    {
        SpinnerEnumObserver(AbsSpinner view, MutableLiveData<E> data, E[] values)
        {
            super(view);
            Listeners.EnumItemSelectListener<E> listener = new Listeners.EnumItemSelectListener<>(data, values);
            view.setOnItemSelectedListener(listener);
            setListener(listener);
        }

        @Override
        public void change(E value)
        {
            view().setSelection(value.ordinal());
        }
    }

    public static class SnackbarObserver extends ViewObserver<View, String>
    {

        SnackbarObserver(View view)
        {
            super(view);
        }

        @Override
        protected void change(String message)
        {
            Snackbar.make(view(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static class ArrayListObserver<T> implements Observer<LiveArrayList.Item<T>>
    {
        @Override
        public void onChanged(LiveArrayList.Item<T> tItem)
        {

        }
    }
}
