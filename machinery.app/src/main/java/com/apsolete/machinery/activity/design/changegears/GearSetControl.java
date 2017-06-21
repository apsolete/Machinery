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
    public interface OnGearSetListener
    {
        void onSet(GearSetControl gearSetCtrl);
        void onSetChanged(GearSetControl gearSetCtrl);
        void onGearChecked(GearSetControl gearSetCtrl);
    }
    
    private final OnGearSetListener _gearSetListener;
    private final int _gearId;
    private final Button _gearsButton;
    private final CheckBox _gearSelect;
    private final EditText _gearsText;
    private boolean _ownSetEnabled = true;

    public GearSetControl(int id, Button button, EditText text, CheckBox checkbox, OnGearSetListener listener)
    {
        _gearId = id;
        _gearSetListener = listener;

        _gearsButton = button;
        _gearsButton.setOnClickListener(this);
        
        _gearSelect = checkbox;
        if (_gearSelect != null)
            _gearSelect.setOnClickListener(this);

        _gearsText = text;
        _gearsText.addTextChangedListener(this);
        _gearsText.setFilters(new InputFilter[]{this});
        setError();
    }

    @Override
    public void onClick(View view)
    {
        if (view == _gearsButton)
            _gearSetListener.onSet(this);
        if (view == _gearSelect)
            _gearSetListener.onGearChecked(this);
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
        _gearSetListener.onSetChanged(this);
    }

    public void setEnabled(boolean enabled)
    {
        if (!enabled && _gearsText.length() > 0)
            return;
        _gearsButton.setEnabled(enabled && _ownSetEnabled);
        _gearsText.setEnabled(enabled && _ownSetEnabled);
        if (_gearSelect != null)
            _gearSelect.setEnabled(enabled);
        setError();
    }

    public void setChecked(boolean checked)
    {
        if (_gearSelect != null)
            _gearSelect.setChecked(checked);
    }
    
    public int getId()
    {
        return _gearId;
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
    
    public boolean isChecked()
    {
        return _gearSelect != null && _gearSelect.isChecked();
    }
    
    public void enableOwnSet(boolean enable)
    {
        _ownSetEnabled = enable;
        setEnabled(enable);
            
        _gearsText.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (_gearSelect != null)
            _gearSelect.setVisibility(!enable ? View.VISIBLE : View.GONE);
    }
    
    private void setError()
    {
        //if (_gearsText.isEnabled() && _gearsText.length() == 0)
        //    _gearsText.setError("Set gears");
    }
}
