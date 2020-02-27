package com.apsolete.machinery.common;

import android.view.View;
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
}
