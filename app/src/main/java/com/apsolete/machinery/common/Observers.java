package com.apsolete.machinery.common;

import android.view.View;
import android.widget.AbsSpinner;
import android.widget.Checkable;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public final class Observers
{
    public static abstract class CustomObserver<V extends View, T> implements Observer<T>
    {
        protected V mView;

        public void setView(V view)
        {
            mView = view;
        }

        public CustomObserver(V view)
        {
            setView(view);
        }
    }

    public static class TextObserver extends CustomObserver<TextView, String>
    {
        public TextObserver(TextView view)
        {
            super(view);
        }

        @Override
        public void onChanged(@Nullable String text)
        {
            mView.setText(text);
        }
    }

    public static class VisibilityObserver extends CustomObserver<View, Boolean>
    {
        public VisibilityObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(Boolean visible)
        {
            mView.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }

    public static class CheckableObserver extends CustomObserver<View, Boolean>
    {
        public CheckableObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(Boolean checked)
        {
            if (mView instanceof Checkable)
            {
                ((Checkable)mView).setChecked(checked);
            }
        }
    }

    public static class EnableObserver extends CustomObserver<View, Boolean>
    {
        public EnableObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(Boolean enable)
        {
            mView.setEnabled(enable);
        }
    }

    public static class SpinnerObserver extends CustomObserver<View, Integer>
    {
        public SpinnerObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(Integer pos)
        {
            if (mView instanceof AbsSpinner)
            {
                ((AbsSpinner) mView).setSelection(pos);
            }
        }
    }

    public static class SpinnerEnumObserver<E extends Enum<E>> extends CustomObserver<View, E>
    {
        public SpinnerEnumObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(E value)
        {
            if (mView instanceof AbsSpinner)
            {
                ((AbsSpinner) mView).setSelection(value.ordinal());
            }
        }
    }

}
