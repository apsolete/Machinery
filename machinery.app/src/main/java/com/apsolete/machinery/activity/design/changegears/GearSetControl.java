package com.apsolete.machinery.activity.design.changegears;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apsolete.machinery.activity.common.TextChangedListener;
import android.widget.*;

public class GearSetControl extends TextChangedListener implements View.OnClickListener, InputFilter
{
    private final OnGearSetListener _gearSetListener;
    private final int _gearId;
    private final Button _gearsButton;
    private final CheckBox _gearSelect;
    private final EditText _gearsText;

    public GearSetControl(int id, Button button, CheckBox checkbox, EditText text, OnGearSetListener listener)
    {
        _gearId = id;
        _gearSetListener = listener;

        _gearsButton = button;
        _gearsButton.setOnClickListener(this);
        
        _gearSelect = checkbox;

        _gearsText = text;
        _gearsText.addTextChangedListener(this);
        _gearsText.setFilters(new InputFilter[]{this});
        setError();
    }

    @Override
    public void onClick(View view)
    {
        _gearSetListener.onSelectGears(_gearId);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend)
    {
        StringBuilder filteredStringBuilder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char currentChar = source.charAt(i);
            if (Character.isDigit(currentChar) || Character.isSpaceChar(currentChar)) {
                filteredStringBuilder.append(currentChar);
            }
        }
        return filteredStringBuilder.toString();
    }

    @Override
    public void onTextChanged(Editable editable)
    {
        setError();
        _gearSetListener.onGearsChanged(_gearId, editable.length() == 0);
    }

    public void setEnabled(Boolean enabled)
    {
        if (!enabled && _gearsText.length() > 0)
            return;
        _gearsButton.setEnabled(enabled);
        _gearsText.setEnabled(enabled);
        setError();
    }

    public String getText()
    {
        return _gearsText.getText().toString();
    }

    public void setText(String text)
    {
        _gearsText.setText(text);
    }
    
    public int[] getGears()
    {
        String text = getText();
        if (text.isEmpty())
            return null;
            
        String[] strs = text.split(" ");
        int[] gs = new int[strs.length];
        int i = 0;
        for (String s : strs)
        {
            if (!s.isEmpty())
            {
                int n = Integer.parseInt(s);
                gs[i] = n;
                i++;
            }
        }
        return gs;
    }

    public boolean isEmpty()
    {
        return _gearsText.length() == 0;
    }
    
    public void setSelectable(boolean selectable)
    {
        setEnabled(false);
        _gearsText.setVisibility(selectable?View.GONE:View.VISIBLE);
        if (_gearSelect != null)
            _gearSelect.setVisibility(selectable?View.VISIBLE:View.GONE);
    }
    
    private void setError()
    {
        //if (_gearsText.isEnabled() && _gearsText.length() == 0)
        //    _gearsText.setError("Set gears");
    }
}
