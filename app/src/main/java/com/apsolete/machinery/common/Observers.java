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
        CheckableObserver(CompoundButton view, boolean inverse)
        {
            super(view, inverse);
        }

        CheckableObserver(CompoundButton[] views, boolean inverse)
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
        EditTextObserver(EditText view)
        {
            super(view);
        }

        @Override
        public void onChanged(@Nullable String text)
        {
            view().setText(text);
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
