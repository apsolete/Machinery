package com.apsolete.machinery.activity.design.changegears;

import com.apsolete.machinery.activity.common.TextChangedListener;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.*;

import java.util.Vector;

public class GearSetControl extends TextChangedListener implements View.OnClickListener, InputFilter
{
    public interface OnGearSetListener
    {
        void onSet(GearSetControl gearSetCtrl);
        void onSetChanged(GearSetControl gearSetCtrl);
        void onGearChecked(GearSetControl gearSetCtrl);
    }
    
    private OnGearSetListener _gearSetListener;
    private final int _gearId;
    private final Button _gearsButton;
    private CheckBox _gearSelect;
    private final EditText _gearsText;
    private boolean _ownSetEnabled = true;

    public GearSetControl(int id, View parent, int buttonSetId, int textGearsId, int checkboxSelectId, OnGearSetListener listener)
    {
        _gearId = id;
        _gearSetListener = listener;
        
        _gearsButton = (Button)parent.findViewById(buttonSetId);
        _gearsButton.setOnClickListener(this);
        
        _gearsText = (EditText)parent.findViewById(textGearsId);
        _gearsText.addTextChangedListener(this);
        _gearsText.setFilters(new InputFilter[]{this});
        setError();
        
        if (checkboxSelectId != 0)
        {
            _gearSelect = (CheckBox)parent.findViewById(checkboxSelectId);
            _gearSelect.setOnClickListener(this);
        }
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
            if (Character.isDigit(currentChar) || Character.isSpaceChar(currentChar) || currentChar == '-') {
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
        Vector<Integer> numbers = new Vector<>();

        for (String s : strs)
        {
            if (!s.isEmpty())
            {
                //if (s.startsWith("-")) s.re
                if (s.contains("-"))
                {
                    String[] range = s.split("-");
                    int r1 = Integer.parseInt(range[0]);
                    int r2 = Integer.parseInt(range[1]);
                    int start = Math.min(r1, r2);
                    int end = Math.max(r1, r2);
                    while (start <= end)
                    {
                        numbers.add(start);
                        start++;
                    }
                }
                else
                {
                    int n = Integer.parseInt(s);
                    numbers.add(Math.abs(n));
                }
            }
        }

        int[] gears = new int[numbers.size()];
        int i = 0;
        for (int n : numbers)
        {
            gears[i] = n;
            i++;
        }
        return gears;
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
