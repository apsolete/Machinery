package com.apsolete.machinery.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
