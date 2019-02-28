package com.apsolete.machinery.common.fabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apsolete.machinery.R;

import java.util.ArrayList;

public class FabsView extends RelativeLayout implements FabManager
{
    public interface OnFabClickListener
    {
        void OnClick(int fabId);
    }

    private int _deviceWidth;
    private int _backColor;
    private Drawable _background;
    private FabView _fabMain;
    //private View _background;
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

    public FabsView(Context context, AttributeSet attributes)
    {
        super(context, attributes);

        TypedArray attrs = context.obtainStyledAttributes(attributes,
                R.styleable.FabsView, 0, 0);

        try
        {
            _backColor = attrs.getColor(R.styleable.FabsView_backColor, 0x26000000);
//            _offset = attrs.getDimension(R.styleable.FabView_offset, 0.0f);
//            _expandable = attrs.getBoolean(R.styleable.FabView_expandable, false);
//            _srcId = attrs.getResourceId(R.styleable.FabView_src, R.id.mi_home);
        }
        finally
        {
            attrs.recycle();
        }
        //init(context);

/*
        LayoutInflater.from(getContext()).inflate(R.layout.fabs_view, this, true);
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
            _fabs.add((FabView) getChildAt(i));
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
*/
    }

    private void init(Context context)
    {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        _deviceWidth = deviceDisplay.x;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
        }
        /*
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++)
        {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();

            if ((mLeftWidth / _deviceWidth) > rowCount)
            {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            }
            else
            {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
        */
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
//        int childCount = getChildCount();
//        View child;
//        for (int i = 0; i < childCount; i++)
//        {
//            child = getChildAt(i);
//            int childWidth = child.getWidth();
//            int childHeight = child.getHeight();
//            child.layout(r-childWidth, b-childHeight, r, b);
//        }
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        //LayoutInflater.from(getContext()).inflate(R.layout.fabs_view, this, true);
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
        //_background.setVisibility(View.VISIBLE);
        setBackgroundColor(_backColor);

        _fabMain.animate().rotationBy(180);
        //this.getChildAt(0);
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
            //_background.setVisibility(View.GONE);
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
