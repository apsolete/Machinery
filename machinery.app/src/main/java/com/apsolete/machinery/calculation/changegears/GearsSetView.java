package com.apsolete.machinery.calculation.changegears;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.apsolete.machinery.common.TextChangedListener;

public final class GearsSetView extends TextChangedListener implements View.OnClickListener, InputFilter
{
    public interface OnGearsSetViewListener
    {
        void onRequest(GearsSetView gearsSet);
        void onChanged(GearsSetView gearsSet);
        void onChecked(GearsSetView gearsSet);
    }

    private int _gearId;
    //private ChangeGearsContract.Presenter _presenter;
    private Button _gearsButton;
    private CheckBox _gearSelect;
    private EditText _gearsText;
    private boolean _isEditable = true;
    private OnGearsSetViewListener _listener;

    public GearsSetView(int id, View parent, int buttonSetId, int textGearsId, int checkboxSelectId, OnGearsSetViewListener listener)
    {
        _gearId = id;
        //_presenter = presenter;

        _gearsButton = (Button)parent.findViewById(buttonSetId);
        _gearsButton.setOnClickListener(this);

        _gearsText = (EditText)parent.findViewById(textGearsId);
        _gearsText.addTextChangedListener(this);
        _gearsText.setFilters(new InputFilter[]{this});

        if (checkboxSelectId != 0)
        {
            _gearSelect = (CheckBox)parent.findViewById(checkboxSelectId);
            _gearSelect.setOnClickListener(this);
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
            if (!_isEditable && _gearSelect != null)
            {
                boolean isChecked = _gearSelect.isChecked();
                _gearSelect.setChecked(!isChecked);
                _listener.onChecked(this);
            }
            else
                _listener.onRequest(this);
        }

        if (view == _gearSelect)
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
        if (_gearSelect != null)
            _gearSelect.setEnabled(enabled);
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

    public boolean isEmpty()
    {
        return _gearsText.length() == 0;
    }

    public boolean isChecked()
    {
        return _gearSelect != null && _gearSelect.isChecked();
    }

    public void setEditable(boolean enable)
    {
        _isEditable = enable;

        _gearsText.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (_gearSelect != null)
            _gearSelect.setVisibility(!enable ? View.VISIBLE : View.GONE);
    }
}
