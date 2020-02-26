package com.apsolete.machinery.common;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apsolete.machinery.R;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class CustomFragment<VM extends ViewModel> extends Fragment
{
    protected AppCompatActivity Activity;
    protected ProgressBar ProgressBar;
    protected View mRootView;
    protected VM mViewModel;

    private int _layoutId;
    private int _titleId;

    public CustomFragment(@LayoutRes int contentLayoutId)
    {
        super(contentLayoutId);
    }
    public CustomFragment(@LayoutRes int contentLayoutId, @LayoutRes int titleId)
    {
        super(contentLayoutId);
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

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
//    {
//        mRootView = super.onCreateView(inflater, container, savedInstanceState);
//        return mRootView;
//    }

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

    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, @NonNull Class<VM> modelClass)
    {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(modelClass);
        return mRootView;
    }

    protected void initTextView(@IdRes int id, LiveData<String> data, CustomObserver<TextView, String> observer)
    {
        TextView textView = mRootView.findViewById(id);
        observer.setView(textView);
        data.observe(getViewLifecycleOwner(), observer);
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
