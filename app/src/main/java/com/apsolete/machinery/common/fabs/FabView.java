package com.apsolete.machinery.common.fabs;

import android.animation.Animator;
import android.support.annotation.*;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.content.res.TypedArray;
import android.widget.TextView;

import com.apsolete.machinery.R;

public class FabView extends LinearLayout implements FabManager, Animator.AnimatorListener
{
    private final TextView _textView;
    private final FloatingActionButton _fab;

    private String _header;
    private float _offset;
    //private boolean _expandable;
    private int _srcId;
    private boolean _expanded;

    public FabView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.FabView, 0, 0);

        try
        {
            _header = ta.getString(R.styleable.FabView_header);
            _offset = ta.getDimension(R.styleable.FabView_offset, 0.0f);
            //_expandable = ta.getBoolean(R.styleable.FabView_expandable, false);
            _srcId = ta.getResourceId(R.styleable.FabView_src, R.id.mi_home);
        }
        finally
        {
            ta.recycle();
        }

        setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater.from(getContext()).inflate(R.layout.fab_view, this, true);

        _textView = (TextView)getChildAt(0);
        _textView.setText(_header);
        _fab = (FloatingActionButton)getChildAt(1);
        _fab.setImageResource(_srcId);
    }

    @Override
    public void show()
    {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide()
    {
        setVisibility(View.GONE);
    }

    @Override
    public void expand()
    {
        _expanded = true;
        animate().translationY(-_offset);
    }

    @Override
    public void collapse()
    {
        _expanded = false;
        animate().translationY(0).setListener(this);
    }

    @Override
    public void toggle()
    {

    }

    @Override
    public void onAnimationStart(Animator animation)
    {

    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        if(!_expanded)
            setVisibility(View.GONE);
    }

    @Override
    public void onAnimationCancel(Animator animation)
    {

    }

    @Override
    public void onAnimationRepeat(Animator animation)
    {

    }

    public void setOffset(float offset, boolean force)
    {
        if (_offset == 0.0 || force)
            _offset = offset;
    }
}
