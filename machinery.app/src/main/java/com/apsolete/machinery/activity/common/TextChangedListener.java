package com.apsolete.machinery.activity.common;

import android.text.*;

public abstract class TextChangedListener implements TextWatcher
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
        onTextChanged(editable);
    }

    public abstract void onTextChanged(Editable editable);
}
