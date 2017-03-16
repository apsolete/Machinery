package com.apsolete.machinery.activity;

import android.animation.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

public class FabsManager
{
    private AppCompatActivity _activity;
    private FloatingActionButton _fabMain;
    private FloatingActionButton _fab1;
    private FloatingActionButton _fab2;
    private FloatingActionButton _fab3;
    private LinearLayout _fabLayout1;
    private LinearLayout _fabLayout2;
    private LinearLayout _fabLayout3;
    private View _fabsBackground;
    private boolean _isExpanded = false;
    private boolean _isVisible = true;
    private float _st_55, _st_100, _st_145;

    public interface OnFabClickListener
    {
        void OnClick(int fabId);
    }
    private OnFabClickListener _listener;
    
    public FabsManager(AppCompatActivity activity)
    {
        _activity = activity;

        _fabLayout1 = (LinearLayout) _activity.findViewById(R.id.fabLayout1);
        _fabLayout2 = (LinearLayout) _activity.findViewById(R.id.fabLayout2);
        _fabLayout3 = (LinearLayout) _activity.findViewById(R.id.fabLayout3);
        _fabMain = (FloatingActionButton) _activity.findViewById(R.id.fab_main);
        _fab1 = (FloatingActionButton) _activity.findViewById(R.id.fab1);
        _fab2 = (FloatingActionButton) _activity.findViewById(R.id.fab2);
        _fab3 = (FloatingActionButton) _activity.findViewById(R.id.fab3);
        _fabsBackground = _activity.findViewById(R.id.fabBGLayout);

        _st_55 = _activity.getResources().getDimension(R.dimen.standard_55);
        _st_100 = _activity.getResources().getDimension(R.dimen.standard_100);
        _st_145 = _activity.getResources().getDimension(R.dimen.standard_145);

        _fabMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    toggle();
                }
            });

        _fabsBackground.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    collapse();
                }
            });
            
        _fab1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (_listener != null)
                        _listener.OnClick(_fab1.getId());
                }
            });
            
        _fab2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (_listener != null)
                        _listener.OnClick(_fab2.getId());
                }
            });
            
        _fab3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (_listener != null)
                        _listener.OnClick(_fab3.getId());
                }
            });
    }

    public void expand()
    {
        if (!_isVisible)
            return;

        _isExpanded = true;
        _fabLayout1.setVisibility(View.VISIBLE);
        _fabLayout2.setVisibility(View.VISIBLE);
        _fabLayout3.setVisibility(View.VISIBLE);
        _fabsBackground.setVisibility(View.VISIBLE);

        _fabMain.animate().rotationBy(180);
        _fabLayout1.animate().translationY(-_st_55);
        _fabLayout2.animate().translationY(-_st_100);
        _fabLayout3.animate().translationY(-_st_145);
    }

    public void collapse()
    {
        if (!_isVisible)
            return;

        _isExpanded = false;
        _fabsBackground.setVisibility(View.GONE);
        _fabMain.animate().rotationBy(-180);
        _fabLayout1.animate().translationY(0);
        _fabLayout2.animate().translationY(0);
        _fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animator)
                {
                }

                @Override
                public void onAnimationEnd(Animator animator)
                {
                    if (!_isExpanded)
                    {
                        _fabLayout1.setVisibility(View.GONE);
                        _fabLayout2.setVisibility(View.GONE);
                        _fabLayout3.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator)
                {
                }

                @Override
                public void onAnimationRepeat(Animator animator)
                {
                }
            });
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
