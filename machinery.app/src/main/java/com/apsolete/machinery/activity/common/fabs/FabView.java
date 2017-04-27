package com.apsolete.machinery.activity.common.fabs;
import android.widget.*;
import android.support.design.widget.*;
import android.view.*;

public class FabView
{
    private static int _fabId =0;
    
    ViewGroup _layout;
    TextView _title;
    FloatingActionButton _fab;
    int _id;
    float _st;
    
    public FabView(ViewGroup layout, float st)
    {
        _id = _fabId++;
        _layout = layout;
        _title = (TextView)_layout.getChildAt(0);
        _fab = (FloatingActionButton)_layout.getChildAt(1);
        _st = st;
    }
    
    public void show()
    {
        _layout.setVisibility(View.VISIBLE);
        //_layout.animate().translationY(-_st);
    }
    
    public void hide()
    {
        //_layout.animate().translationY(0);
        _layout.setVisibility(View.GONE);
    }
}
