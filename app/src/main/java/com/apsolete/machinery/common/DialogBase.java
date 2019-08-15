package com.apsolete.machinery.common;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class DialogBase extends DialogFragment
{
    public interface ResultListener
    {
        void onPositive();
        void onNegative();
        //void onCancel();
    }

    private int _layout;
    protected ResultListener _resultListener;

    public DialogBase(int layoutId)
    {
        super();
        _layout = layoutId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(_layout, container, false);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setResultListener(ResultListener listener)
    {
        _resultListener = listener;
    }
}
