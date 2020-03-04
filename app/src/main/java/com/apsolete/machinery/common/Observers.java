package com.apsolete.machinery.common;

import android.view.View;
import android.widget.AbsSpinner;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public final class Observers
{
    public static abstract class CustomObserver<V extends View, T> implements Observer<T>
    {
        V mView;

        public void setView(V view)
        {
            mView = view;
        }

        CustomObserver(V view)
        {
            setView(view);
        }
    }

    public static class TextObserver extends CustomObserver<TextView, String>
    {
        TextObserver(TextView view)
        {
            super(view);
        }

        @Override
        public void onChanged(@Nullable String text)
        {
            mView.setText(text);
        }
    }

    public static class EditTextObserver extends CustomObserver<EditText, String>
    {
        EditTextObserver(EditText view)
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
        VisibilityObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(Boolean visible)
        {
            mView.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }

    public static class CheckableObserver extends CustomObserver<CompoundButton, Boolean>
    {
        CheckableObserver(CompoundButton view)
        {
            super(view);
        }

        @Override
        public void onChanged(Boolean checked)
        {
            mView.setChecked(checked);
        }
    }

    public static class EnableObserver extends CustomObserver<View, Boolean>
    {
        EnableObserver(View view)
        {
            super(view);
        }

        @Override
        public void onChanged(Boolean enable)
        {
            mView.setEnabled(enable);
        }
    }

    public static class SpinnerObserver extends CustomObserver<AbsSpinner, Integer>
    {
        SpinnerObserver(AbsSpinner view)
        {
            super(view);
        }

        @Override
        public void onChanged(Integer pos)
        {
            mView.setSelection(pos);
        }
    }

    public static class SpinnerEnumObserver<E extends Enum<E>> extends CustomObserver<AbsSpinner, E>
    {
        SpinnerEnumObserver(AbsSpinner view)
        {
            super(view);
        }

        @Override
        public void onChanged(E value)
        {
            mView.setSelection(value.ordinal());
        }
    }
}
