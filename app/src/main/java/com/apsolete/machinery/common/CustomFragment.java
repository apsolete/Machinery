package com.apsolete.machinery.common;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class CustomFragment<VM extends CustomViewModel> extends Fragment
{
    protected AppCompatActivity Activity;
    protected View mRootView;
    protected VM mViewModel;

    private int _layoutId;
    private int _titleId;
    private Class<VM> mVmClass;

//    public CustomFragment(@LayoutRes int contentLayoutId)
//    {
//        super(contentLayoutId);
//    }

    public CustomFragment(@LayoutRes int contentLayoutId, @LayoutRes int titleId, Class<VM> vmClass)
    {
        super(contentLayoutId);
        mVmClass = vmClass;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(mVmClass);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Activity.getSupportActionBar().setTitle(_titleId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("layout", _layoutId);
        outState.putInt("title", _titleId);
    }

    protected void setTextObserver(@IdRes int id, LiveData<String> data)
    {
        TextView textView = mRootView.findViewById(id);
        Observer<String> observer = new Observers.TextObserver(textView);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setEditTextObserver(@IdRes int id, final MutableLiveData<String> data)
    {
        EditText editText = mRootView.findViewById(id);
        editText.addTextChangedListener(new TextChangedListener()
        {
            @Override
            public void onTextChanged(Editable editable)
            {
                data.setValue(editable.toString());
            }
        });
        Observer<String> observer = new Observers.EditTextObserver(editText);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setVisibilityObserver(@IdRes int id, LiveData<Boolean> data)
    {
        View view = mRootView.findViewById(id);
        Observer<Boolean> observer = new Observers.VisibilityObserver(view);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setVisibilityMultiObserver(@IdRes int[] ids, LiveData<Boolean> data)
    {
        View[] views = new View[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            views[i] = mRootView.findViewById(ids[i]);
        }
        Observer<Boolean> observer = new Observers.VisibilityMultiObserver(views);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setCheckableObserver(@IdRes int id, final MutableLiveData<Boolean> data)
    {
        CompoundButton view = mRootView.findViewById(id);
        view.setOnClickListener(new android.view.View.OnClickListener()
        {
            @Override
            public void onClick(android.view.View view)
            {
                boolean isOneSet = ((CompoundButton)view).isChecked();
                data.setValue(isOneSet);
            }
        });
        Observer<Boolean> observer = new Observers.CheckableObserver(view);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setEnableObserver(@IdRes int id, LiveData<Boolean> data)
    {
        View view = mRootView.findViewById(id);
        Observer<Boolean> observer = new Observers.EnableObserver(view);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setEnableMultiObserver(@IdRes int[] ids, LiveData<Boolean> data)
    {
        View[] views = new View[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            views[i] = mRootView.findViewById(ids[i]);
        }
        Observer<Boolean> observer = new Observers.EnableMultiObserver(views);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected void setSpinnerObserver(@IdRes int id, @ArrayRes int strArrayId, final MutableLiveData<Integer> data)
    {
        Spinner spinner = mRootView.findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity,
                strArrayId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                data.setValue(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
        Observer<Integer> observer = new Observers.SpinnerObserver(spinner);
        data.observe(getViewLifecycleOwner(), observer);
    }

    protected <E extends Enum<E>> void setSpinnerEnumObserver(@IdRes int id, @ArrayRes int strArrayId, final MutableLiveData<E> data, final E[] values)
    {
        Spinner spinner = mRootView.findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity,
                strArrayId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                data.setValue(values[pos]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
        Observer<E> observer = new Observers.SpinnerEnumObserver<E>(spinner);
        data.observe(getViewLifecycleOwner(), observer);
    }
}
