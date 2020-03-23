package com.apsolete.machinery.calculation.gearing.changegears;

import android.util.SparseArray;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.Numbers;

import androidx.lifecycle.MutableLiveData;

public class GearSetsViewModel
{
    public static class GSet
    {
        private boolean mSetGearsInternal = false;
        private GSet mNext = null;
        private MutableLiveData<Integer[]> mGears = new MutableLiveData<Integer[]>(null)
        {
            @Override
            public void setValue(Integer[] value)
            {
                super.setValue(value);
                if (mSetGearsInternal)
                    return;
                else
                {
                    mSetGearsInternal = true;
                    mGearsStr.setValue(Numbers.getString(value));
                    mSetGearsInternal = false;
                }
            }
        };
        private MutableLiveData<String> mGearsStr = new MutableLiveData<String>(null)
        {
            @Override
            public void setValue(String value)
            {
                super.setValue(value);
                if (mSetGearsInternal)
                    return;
                else
                {
                    mSetGearsInternal = true;
                    mGears.setValue(Numbers.getNumbers(value));
                    mSetGearsInternal = false;
                }
            }
        };
        private MutableLiveData<Boolean> mSwitched = new MutableLiveData<Boolean>(false)
        {
            @Override
            public void setValue(Boolean value)
            {
                super.setValue(value);
                boolean editAllowed = value && mEditable.getValue();
                mEditAllowed.setValue(editAllowed);
                if (mNext != null)
                {
                    mNext.setEnabled(value);
                    if (!value)
                        mNext.setSwitched(false);
                }
            }
        };
        private MutableLiveData<Boolean> mEditable = new MutableLiveData<>(false);
        private MutableLiveData<Boolean> mEditAllowed = new MutableLiveData<>(false);
        private MutableLiveData<Boolean> mEnabled = new MutableLiveData<>(false);

        public GSet(GSet next)
        {
            mNext = next;
        }

//        public GSet(Integer[] set, boolean isSwitched)
//        {
//            mGears = new MutableLiveData<Integer[]>(set);
//            mSwitched = new MutableLiveData<>(isSwitched);
//        }

        public MutableLiveData<Integer[]> getGears()
        {
            return mGears;
        }

        public GSet setGears(Integer[] gears)
        {
            try
            {
                mSetGearsInternal = true;
                mGears.setValue(gears);
                mGearsStr.setValue(Numbers.getString(gears));
                return this;
            }
            finally
            {
                mSetGearsInternal = false;
            }
        }

        public MutableLiveData<String> getGearsStr()
        {
            return mGearsStr;
        }

        public GSet setGears(String gears)
        {
            try
            {
                mSetGearsInternal = true;
                mGearsStr.setValue(gears);
                mGears.setValue(Numbers.getNumbers(gears));
                return this;
            }
            finally
            {
                mSetGearsInternal = false;
            }

        }

        public MutableLiveData<Boolean> isSwitched()
        {
            return mSwitched;
        }

        public GSet setSwitched(Boolean switched)
        {
            mSwitched.setValue(switched);
            return this;
        }

        public MutableLiveData<Boolean> isEditable()
        {
            return mEditable;
        }

        public GSet setEditable(Boolean editable)
        {
            mEditable.setValue(editable);
            return this;
        }

        public MutableLiveData<Boolean> isEnabled()
        {
            return mEnabled;
        }

        public GSet setEnabled(Boolean enabled)
        {
            mEnabled.setValue(enabled);
            return this;
        }

        public MutableLiveData<Boolean> isEditAllowed()
        {
            return mEditAllowed;
        }

        public boolean isEmpty()
        {
            return mGears.getValue().length == 0;
        }
    }

    private SparseArray<GSet> mGSets;

    public GearSetsViewModel()
    {
        mGSets = new SparseArray<>(G.Z6 + 1);
        GSet z6 = new GSet(null);
        GSet z5 = new GSet(z6);
        GSet z4 = new GSet(z5);
        GSet z3 = new GSet(z4);
        GSet z2 = new GSet(z3);
        GSet z1 = new GSet(z2);
        GSet z0 = new GSet(z1);
        mGSets.put(G.Z0, z0);
        mGSets.put(G.Z1, z1);
        mGSets.put(G.Z2, z2);
        mGSets.put(G.Z3, z3);
        mGSets.put(G.Z4, z4);
        mGSets.put(G.Z5, z5);
        mGSets.put(G.Z6, z6);
    }

    public GSet get(int z)
    {
        return mGSets.get(z);
    }
}
