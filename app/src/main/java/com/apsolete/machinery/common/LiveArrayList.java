package com.apsolete.machinery.common;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class LiveArrayList<T> extends LiveData<LiveArrayList.Item<T>> //implements List<T>
{
    public final int ADD = 1;
    public final int REMOVE = 2;
    public final int CLEAR = 3;

    public static class Item<T>
    {
        public final int Action;
        public final List<T> Values;

        public Item(int action, T value)
        {
            Action = action;
            Values = new ArrayList<>();
            Values.add(value);
        }

        public Item(int action, List<T> values)
        {
            Action = action;
            Values = new ArrayList<>(values);
        }
    }

    private ArrayList<T> mList;
    private int mState;

    public ArrayList<T> get()
    {
        return mList;
    }

    public void add(T value)
    {
        mList.add(value);
        setValue(new Item<>(ADD, value));
    }

    public void addAll(List<T> values)
    {
        mList.addAll(values);
        setValue(new Item<>(ADD, values));
    }

    public void remove(T value)
    {
        mList.remove(value);
        setValue(new Item<>(REMOVE, value));
    }

    public void clear()
    {
        mList.clear();
        setValue(new Item<>(CLEAR, null));
    }

    public int size()
    {
        return mList.size();
    }

    @Override
    protected void setValue(Item<T> value)
    {
        super.setValue(value);
    }

    @Nullable
    @Override
    public Item<T> getValue()
    {
        return null;//return super.getValue();
    }
}
