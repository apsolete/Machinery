package com.apsolete.machinery.common;

import android.text.*;

@Deprecated
public abstract class TextChangedListener implements TextWatcher
{
    private boolean _ignoreTextChange = false;

    public void stop()
    {
        _ignoreTextChange = true;
    }

    public void start()
    {
        _ignoreTextChange = false;
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
        if (!_ignoreTextChange)
            onTextChanged(editable);
    }

    public abstract void onTextChanged(Editable editable);
}
