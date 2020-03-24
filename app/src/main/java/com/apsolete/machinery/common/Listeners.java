package com.apsolete.machinery.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;

final class Listeners
{
    public static abstract class ChangeListener<T>
    {
        private boolean mIsListening = true;
        private boolean mIsBroadcasting = false;

        public abstract void onChanged(T value);

        public void start()
        {
            mIsListening = true;
        }

        public void stop()
        {
            mIsListening = false;
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

    public static abstract class CheckedChangeListener extends ChangeListener<Boolean> implements CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            onChangedInternal(isChecked);
        }
    }

    public static abstract class TextChangeListener extends ChangeListener<String> implements TextWatcher
    {
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
}
