package com.apsolete.machinery.common;

import android.text.Editable;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public final class Observers
{
    public static abstract class ViewObserver<V extends View, T> implements Observer<T>
    {
        View[] mViews;

        ViewObserver(V view)
        {
            mViews = new View[1];
            mViews[0] = view;
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
        public void onChanged(Boolean value)
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
        public void onChanged(Boolean value)
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
        MutableLiveData<Boolean> mLiveData;

        android.view.View.OnClickListener mClickListener = new android.view.View.OnClickListener()
        {
            @Override
            public void onClick(android.view.View view)
            {
                boolean checked = ((CompoundButton)view).isChecked();
                mLiveData.setValue(mInverse != checked);
            }
        };

        CheckableObserver(CompoundButton view, MutableLiveData<Boolean> data, boolean inverse)
        {
            super(view, inverse);
            mLiveData = data;
            view.setOnClickListener(mClickListener);
        }

        CheckableObserver(CompoundButton[] views, MutableLiveData<Boolean> data, boolean inverse)
        {
            super(views, inverse);
        }

        @Override
        public void onChanged(Boolean value)
        {
            boolean checked = !mInverse ? value : !value;
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
        public void onChanged(@Nullable String text)
        {
            view().setText(text);
        }
    }

    public static class EditTextObserver extends ViewObserver<EditText, String>
    {
        MutableLiveData<String> mLiveData;

        private TextChangedListener mTextChangedListener = new TextChangedListener()
        {
            @Override
            public void onTextChanged(Editable editable)
            {
                mLiveData.setValue(editable.toString());
            }
        };

        EditTextObserver(EditText view, MutableLiveData<String> data)
        {
            super(view);
            mLiveData = data;
            view.addTextChangedListener(mTextChangedListener);
        }

        @Override
        public void onChanged(@Nullable String text)
        {
            mTextChangedListener.stop();
            try
            {
                view().setText(text);
            }
            finally
            {
                mTextChangedListener.start();
            }
        }
    }

    public static class SpinnerObserver extends ViewObserver<AbsSpinner, Integer>
    {
        SpinnerObserver(AbsSpinner view)
        {
            super(view);
        }

        @Override
        public void onChanged(Integer pos)
        {
            view().setSelection(pos);
        }
    }

    public static class SpinnerEnumObserver<E extends Enum<E>> extends ViewObserver<AbsSpinner, E>
    {
        SpinnerEnumObserver(AbsSpinner view)
        {
            super(view);
        }

        @Override
        public void onChanged(E value)
        {
            view().setSelection(value.ordinal());
        }
    }
}
