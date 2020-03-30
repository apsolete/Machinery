package com.apsolete.machinery.common;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.apsolete.machinery.R;

@Deprecated
public abstract class ContentBase extends Fragment
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

    public ContentBase getContent()
    {
        return  this;
    }
}
