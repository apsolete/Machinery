package com.apsolete.machinery.activity.common.fabs;
import android.widget.*;
import android.support.design.widget.*;
import android.view.*;

public class FabView
{
    ViewGroup _layout;
    TextView _title;
    FloatingActionButton _fab;
    float _st;
    
    public FabView(ViewGroup layout, float st)
    {
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
