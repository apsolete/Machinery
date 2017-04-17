package com.apsolete.machinery.activity.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.*;
import android.os.*;
import android.view.*;

public class ContentFragment extends Fragment
{
    protected AppCompatActivity _activity;
    private int _layoutId;
    private int _titleId;

    public ContentFragment(int layoutId, int titleId)
    {
        _layoutId = layoutId;
        _titleId = titleId;
    }

    @Override
    public void onAttach(Context context)
    {
        _activity = (AppCompatActivity)getActivity();
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            _layoutId = savedInstanceState.getInt("layout");
            _titleId = savedInstanceState.getInt("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(_layoutId, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        _activity.getSupportActionBar().setTitle(_titleId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("layout", _layoutId);
        outState.putInt("title", _titleId);
    }
}
