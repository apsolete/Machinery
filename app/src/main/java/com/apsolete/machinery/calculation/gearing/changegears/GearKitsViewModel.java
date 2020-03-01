package com.apsolete.machinery.calculation.gearing.changegears;

import android.util.SparseArray;

import com.apsolete.machinery.common.G;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GearKitsViewModel
{
    public static class Kit
    {
        private MutableLiveData<Integer[]> mKit;
        private MutableLiveData<Boolean> mIsChecked;

        public Kit()
        {
            mKit = new MutableLiveData<Integer[]>(null);
            mIsChecked = new MutableLiveData<>(false);
        }

        public Kit(Integer[] kit, boolean isChecked)
        {
            mKit = new MutableLiveData<Integer[]>(kit);
            mIsChecked = new MutableLiveData<>(isChecked);
        }

        public LiveData<Integer[]> getKit()
        {
            return mKit;
        }

        public void setKit(Integer[] kit)
        {
            mKit.setValue(kit);
        }

        public LiveData<Boolean> isChecked()
        {
            return mIsChecked;
        }

        public void setChecked(Boolean checked)
        {
            mIsChecked.setValue(checked);
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

    public void put(int kit, Integer[] zz)
    {
        mKits.get(kit).setKit(zz);
    }
}
