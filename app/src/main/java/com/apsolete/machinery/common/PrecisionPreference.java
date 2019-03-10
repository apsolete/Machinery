package com.apsolete.machinery.common;

import com.apsolete.machinery.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PrecisionPreference extends Preference
{
    private static final int DefPrecision = 1;
    private int _precision;
    
    private static class SavedState extends BaseSavedState
    {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        int _value;

        public SavedState(Parcelable superState)
        {
            super(superState);
        }

        public SavedState(Parcel source)
        {
            super(source);
            // Get the current preference's value
            _value = source.readInt();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(_value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
        new Parcelable.Creator<SavedState>()
        {
            public SavedState createFromParcel(Parcel in)
            {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size)
            {
                return new SavedState[size];
            }
        };
    }
    
    private SeekBar _seekBar;
    private TextView _textView;

    public PrecisionPreference(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PrecisionPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.view_preference_precision);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder)
    {
        super.onBindViewHolder(holder);
        _textView = (TextView)holder.findViewById(R.id.pp_precisiontext);
        _seekBar = (SeekBar)holder.findViewById(R.id.pp_precision);
        _seekBar.setProgress(_precision - 1);
        _seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    setPrecision(progress + 1);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                    // TODO: Implement this method
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    // TODO: Implement this method
                }
            });
        setPrecision(_precision);
    }

    private void setPrecision(int precision)
    {
        if (_precision != precision)
        {
            _precision = precision;
            persistInt(_precision);
        }
        
        StringBuilder text = new StringBuilder("#.");
        for (int i = 0; i < precision; i++)
            text.append("0");
        _textView.setText(text.toString());
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue)
    {
        super.onSetInitialValue(defaultValue);
        int defPrecision = defaultValue != null ? (int) defaultValue : DefPrecision;
        int precision = getPersistedInt(defPrecision);
        if (_precision != precision)
            _precision = precision;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        //return super.onGetDefaultValue(a, index);
        return a.getInteger(index, DefPrecision);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent())
        {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState._value = _seekBar.getProgress() + 1;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        _seekBar.setProgress(myState._value);
    }
}
