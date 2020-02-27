package com.apsolete.machinery.common;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.Observers.VisibilityObserver;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

    protected void setTextViewObserver(@IdRes int id, LiveData<String> data)
    {
        TextView textView = mRootView.findViewById(id);
        Observer<String> observer = new Observers.TextObserver(textView);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setViewVisibilityObserver(@IdRes int id, LiveData<Boolean> data)
    {
        View view = mRootView.findViewById(id);
        Observer<Boolean> observer = new Observers.VisibilityObserver(view);
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
