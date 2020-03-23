package com.apsolete.machinery.calculation.gearing.changegears;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.IdRes;

import com.apsolete.machinery.common.TextChangedListener;

public final class GearSetView extends TextChangedListener implements View.OnClickListener, InputFilter
{
    public interface OnGearKitViewListener
    {
        void onRequest(GearSetView gearSet);
        void onChanged(GearSetView gearSet);
        void onChecked(GearSetView gearSet);
    }

    private int _gearId;
    private View _gearsButton;
    private CheckBox _gearSwitch;
    private EditText _gearsText;
    private boolean _isEditable = true;
    private OnGearKitViewListener _listener;

    public GearSetView(int id, View parent, @IdRes int buttonSetId, @IdRes int textSetId, @IdRes int switchId, OnGearKitViewListener listener)
    {
        _gearId = id;

        _gearsButton = parent.findViewById(buttonSetId);
        _gearsButton.setOnClickListener(this);

        _gearsText = (EditText)parent.findViewById(textSetId);
        _gearsText.addTextChangedListener(this);
        _gearsText.setFilters(new InputFilter[]{this});

        if (switchId != 0)
        {
            _gearSwitch = (CheckBox)parent.findViewById(switchId);
            _gearSwitch.setOnClickListener(this);
        }

        _listener = listener;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
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
    public void onClick(View view)
    {
        if (view == _gearsButton)
        {
            if (!_isEditable && _gearSwitch != null)
            {
                boolean switched = _gearSwitch.isChecked();
                _gearSwitch.setChecked(!switched);
                _listener.onChecked(this);
            }
            else
                _listener.onRequest(this);
        }

        if (view == _gearSwitch)
            _listener.onChecked(this);
    }

    @Override
    public void onTextChanged(Editable editable)
    {
        _listener.onChanged(this);
    }

    public void setEnabled(boolean enabled)
    {
        _gearsButton.setEnabled(enabled);
        _gearsText.setEnabled(enabled && _isEditable);
        if (_gearSwitch != null)
            _gearSwitch.setEnabled(enabled);
    }

    public void setSwitched(boolean switched)
    {
        if (_gearSwitch != null)
            _gearSwitch.setChecked(switched);
    }

    public int id()
    {
        return _gearId;
    }

    public String gears()
    {
        return _gearsText.getText().toString();
    }

    public void setGears(String text)
    {
        try
        {
            this.stopTracking();
            _gearsText.setText(text);
        }
        finally
        {
            this.startTracking();
        }
        _gearsText.setText(text);
    }

    public boolean isEmpty()
    {
        return _gearsText.length() == 0;
    }

    public boolean switched()
    {
        return _gearSwitch != null && _gearSwitch.isChecked();
    }

    public void enableEdit(boolean enable)
    {
        _isEditable = enable;

        _gearsText.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (_gearSwitch != null)
            _gearSwitch.setVisibility(!enable ? View.VISIBLE : View.GONE);
    }
}
