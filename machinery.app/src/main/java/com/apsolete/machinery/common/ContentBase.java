package com.apsolete.machinery.common;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.apsolete.machinery.*;

public class ContentBase extends Fragment
{
    protected AppCompatActivity Activity;
    protected ProgressBar ProgressBar;
    
    private int _layoutId;
    private int _titleId;

    public ContentBase(int layoutId, int titleId)
    {
        _layoutId = layoutId;
        _titleId = titleId;
    }

    @Override
    public void onAttach(Context context)
    {
        Activity = (AppCompatActivity)getActivity();
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
        Activity.getSupportActionBar().setTitle(_titleId);
        ProgressBar = (ProgressBar)Activity.findViewById(R.id.progressBar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("layout", _layoutId);
        outState.putInt("title", _titleId);
    }
    
    protected void showProgress(int progress)
    {
        if (ProgressBar.getVisibility() == View.GONE)
            ProgressBar.setVisibility(View.VISIBLE);
        ProgressBar.setProgress(progress);
    }

    protected void resetProgress()
    {
        ProgressBar.setProgress(0);
        ProgressBar.setVisibility(View.GONE);
    }
}
