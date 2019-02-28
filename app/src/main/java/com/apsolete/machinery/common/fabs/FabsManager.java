package com.apsolete.machinery.common.fabs;

import com.apsolete.machinery.*;

import android.animation.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class FabsManager implements FabManager
{
    public interface OnFabClickListener
    {
        void OnClick(int fabId);
    }

    private AppCompatActivity _activity;
    //private FloatingActionButton _fabMain;
    private FabView _fabMain;
    private View _background;
    private boolean _isExpanded = false;
    private boolean _isVisible = true;
    private ArrayList<FabView> _fabs = new ArrayList<>();

    private OnFabClickListener _listener;

    private View.OnClickListener _fabClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (_listener != null)
                _listener.OnClick(view.getId());
        }
    };
    
    public FabsManager(AppCompatActivity activity)
    {
        _activity = activity;

        FabView fabView1 = (FabView) _activity.findViewById(R.id.fab1);
        fabView1.setOnClickListener(_fabClickListener);
        _fabs.add(fabView1);
        
        FabView fabView2 = (FabView) _activity.findViewById(R.id.fab2);
        fabView2.setOnClickListener(_fabClickListener);
        _fabs.add(fabView2);
        
        FabView fabView3 = (FabView) _activity.findViewById(R.id.fab3);
        fabView3.setOnClickListener(_fabClickListener);
        _fabs.add(fabView3);
        
        //_fabMain = (FloatingActionButton) _activity.findViewById(R.id.fab_main);
        _fabMain = (FabView) _activity.findViewById(R.id.fab4);
        //_background = _activity.findViewById(R.id.fabsBackground);

        _fabMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    toggle();
                }
            });

        _background.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    collapse();
                }
            });
    }

    public void expand()
    {
        if (!_isVisible)
            return;

        _isExpanded = true;
        for (FabView fv : _fabs)
            fv.show();
        _background.setVisibility(View.VISIBLE);

        _fabMain.animate().rotationBy(180);
        for (FabView fv : _fabs)
            fv.expand();
    }

    public void collapse()
    {
        if (!_isVisible)
            return;

        _isExpanded = false;
        _background.setVisibility(View.GONE);
        _fabMain.animate().rotationBy(-180);

        for (FabView fv : _fabs)
            fv.collapse();
    }

    public void toggle()
    {
        if (!_isVisible)
            return;

        if (_isExpanded)
            collapse();
        else
            expand();
    }

    public void show()
    {
        if (!_isVisible)
        {
            _isVisible = true;
            _fabMain.setVisibility(View.VISIBLE);
        }
    }

    public void hide()
    {
        if (_isVisible)
        {
            if (_isExpanded)
                collapse();
            _isVisible = false;
            _fabMain.setVisibility(View.GONE);
        }
    }

    public boolean isVisible()
    {
        return _isVisible;
    }

    public boolean isExpanded()
    {
        return _isExpanded;
    }

    public void setClickListener(OnFabClickListener listener)
    {
        _listener = listener;
    }
}
