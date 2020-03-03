package com.apsolete.machinery.calculation.gearing.changegears;

import android.util.SparseArray;

import com.apsolete.machinery.common.G;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GearKitsViewModel
{
    public static class Kit
    {
        private MutableLiveData<Integer[]> mGears;
        private MutableLiveData<Boolean> mIsChecked;
        private MutableLiveData<Boolean> mIsEditable;
        private MutableLiveData<Boolean> mIsEnabled;

        public Kit()
        {
            mGears = new MutableLiveData<Integer[]>(null);
            mIsChecked = new MutableLiveData<>(false);
            mIsEditable = new MutableLiveData<>(false);
            mIsEnabled = new MutableLiveData<>(false);
        }

        public Kit(Integer[] kit, boolean isChecked)
        {
            mGears = new MutableLiveData<Integer[]>(kit);
            mIsChecked = new MutableLiveData<>(isChecked);
        }

        public MutableLiveData<Integer[]> getGears()
        {
            return mGears;
        }

        public Kit setGears(Integer[] gears)
        {
            mGears.setValue(gears);
            return this;
        }

        public MutableLiveData<Boolean> isChecked()
        {
            return mIsChecked;
        }

        public Kit setChecked(Boolean checked)
        {
            mIsChecked.setValue(checked);
            return this;
        }

        public MutableLiveData<Boolean> isEditable()
        {
            return mIsEditable;
        }

        public Kit setEditable(Boolean editable)
        {
            mIsEditable.setValue(editable);
            return this;
        }

        public MutableLiveData<Boolean> isEnabled()
        {
            return mIsEnabled;
        }

        public Kit setEnabled(Boolean enabled)
        {
            mIsEnabled.setValue(enabled);
            return this;
        }
    }

    private SparseArray<Kit> mKits;

    public GearKitsViewModel()
    {
        mKits = new SparseArray<>(G.Z6 + 1);
        mKits.put(G.Z0, new Kit());
        mKits.put(G.Z1, new Kit());
        mKits.put(G.Z2, new Kit());
        mKits.put(G.Z3, new Kit());
        mKits.put(G.Z4, new Kit());
        mKits.put(G.Z5, new Kit());
        mKits.put(G.Z6, new Kit());
    }

    public Kit get(int z)
    {
        return mKits.get(z);
    }

    public void putGears(int kit, Integer[] zz)
    {
        mKits.get(kit).setGears(zz);
    }
}
