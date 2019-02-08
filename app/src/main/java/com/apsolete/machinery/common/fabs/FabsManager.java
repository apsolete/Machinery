package com.apsolete.machinery.common.fabs;

import com.apsolete.machinery.*;

import android.animation.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class FabsManager
{
    public interface OnFabClickListener
    {
        void OnClick(int fabId);
    }

    public class FabView implements Animator.AnimatorListener
    {
        ViewGroup _layout;
        TextView _title;
        FloatingActionButton _fab;
        private float _offset;
        private boolean _expanded;

        public FabView(ViewGroup layout, float offset, View.OnClickListener listener)
        {
            _layout = layout;
            _offset = offset;
            _title = (TextView)_layout.getChildAt(0);
            _fab = (FloatingActionButton)_layout.getChildAt(1);
            _fab.setOnClickListener(listener);
        }

        public void show()
        {
            _layout.setVisibility(View.VISIBLE);
        }

        public void hide()
        {
            _layout.setVisibility(View.GONE);
        }

        public void expand()
        {
            _expanded = true;
            _layout.animate().translationY(-_offset);
        }

        public void collapse()
        {
            _expanded = false;
            _layout.animate().translationY(0).setListener(this);
        }

        @Override
        public void onAnimationStart(Animator animator)
        {
        }

        @Override
        public void onAnimationEnd(Animator animator)
        {
            if(!_expanded)
                _layout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animator)
        {
        }

        @Override
        public void onAnimationRepeat(Animator animator)
        {
        }
    }

    private AppCompatActivity _activity;
    private FloatingActionButton _fabMain;
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

        LinearLayout fabLayout1 = (LinearLayout) _activity.findViewById(R.id.fabLayout1);
        float offset = _activity.getResources().getDimension(R.dimen.standard_55);
        _fabs.add(new FabView(fabLayout1, offset, _fabClickListener));
        
        LinearLayout fabLayout2 = (LinearLayout) _activity.findViewById(R.id.fabLayout2);
        offset = _activity.getResources().getDimension(R.dimen.standard_100);
        _fabs.add(new FabView(fabLayout2, offset, _fabClickListener));
        
        LinearLayout fabLayout3 = (LinearLayout) _activity.findViewById(R.id.fabLayout3);
        offset = _activity.getResources().getDimension(R.dimen.standard_145);
        _fabs.add(new FabView(fabLayout3, offset, _fabClickListener));
        
        _fabMain = (FloatingActionButton) _activity.findViewById(R.id.fab_main);
        _background = _activity.findViewById(R.id.fabsBackground);

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
