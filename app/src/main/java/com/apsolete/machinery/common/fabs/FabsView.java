package com.apsolete.machinery.common.fabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.apsolete.machinery.R;

import java.util.ArrayList;

public class FabsView extends RelativeLayout implements FabManager
{
    public interface OnFabClickListener
    {
        void onClick(int fabId);
    }

    private int _backColor;
    private float _offset;
    private Drawable _background;
    private FabView _fabMain;
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
                _listener.onClick(view.getId());
        }
    };

    public FabsView(Context context)
    {
        this(context, null);
    }

    public FabsView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public FabsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        this(context, attrs, defStyleAttr, 0);
    }

    public FabsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FabsView, 0, 0);

        try
        {
            _backColor = ta.getColor(R.styleable.FabsView_backColor, 0x26000000);
            _offset = ta.getDimension(R.styleable.FabsView_defoffset, 55.0f);
        }
        finally
        {
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        _fabMain = (FabView)getChildAt(0);
        _fabMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggle();
            }
        });

        int childCount = getChildCount();
        for (int i = 1; i < childCount; i++)
        {
            FabView fv = (FabView) getChildAt(i);
            fv.setOnClickListener(_fabClickListener);
            fv.setOffset(i * _offset, false);
            _fabs.add(fv);
        }

        setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                collapse();
            }
        });

        _background = getBackground();
    }

    @Override
    public void show()
    {
        if (!_isVisible)
        {
            _isVisible = true;
            _fabMain.setVisibility(View.VISIBLE);
        }
    }

    @Override
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

    @Override
    public void expand()
    {
        if (!_isVisible)
            return;

        _isExpanded = true;
        for (FabView fv : _fabs)
            fv.show();

        setBackgroundColor(_backColor);

        _fabMain.animate().rotationBy(180);
        for (FabView fv : _fabs)
            fv.expand();
    }

    @Override
    public void collapse()
    {
        if (!_isVisible)
            return;

        if (_isExpanded)
        {
            _isExpanded = false;

            setBackground(_background);
            _fabMain.animate().rotationBy(-180);

            for (FabView fv : _fabs)
                fv.collapse();
        }
    }

    @Override
    public void toggle()
    {
        if (!_isVisible)
            return;

        if (_isExpanded)
            collapse();
        else
            expand();
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
