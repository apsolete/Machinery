package com.apsolete.machinery.calculation.gearing.changegears;

import android.util.SparseArray;

import com.apsolete.machinery.common.G;

import androidx.lifecycle.MutableLiveData;

public class GearKitsViewModel
{
    public static class Kit
    {
        private Kit mNext = null;
        private MutableLiveData<Integer[]> mGears = new MutableLiveData<Integer[]>(null);
        private MutableLiveData<Boolean> mSelected = new MutableLiveData<Boolean>(false)
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
                        mNext.setSelected(false);
                }
            }
        };
        private MutableLiveData<Boolean> mEditable = new MutableLiveData<>(false);
        private MutableLiveData<Boolean> mEditAllowed = new MutableLiveData<>(false);
        private MutableLiveData<Boolean> mEnabled = new MutableLiveData<>(false);

        public Kit(Kit next)
        {
            mNext = next;
        }

//        public Kit(Integer[] kit, boolean isChecked)
//        {
//            mGears = new MutableLiveData<Integer[]>(kit);
//            mIsChecked = new MutableLiveData<>(isChecked);
//        }

        public MutableLiveData<Integer[]> getGears()
        {
            return mGears;
        }

        public Kit setGears(Integer[] gears)
        {
            mGears.setValue(gears);
            return this;
        }

        public MutableLiveData<Boolean> isSelected()
        {
            return mSelected;
        }

        public Kit setSelected(Boolean checked)
        {
            mSelected.setValue(checked);
            return this;
        }

        public MutableLiveData<Boolean> isEditable()
        {
            return mEditable;
        }

        public Kit setEditable(Boolean editable)
        {
            mEditable.setValue(editable);
            return this;
        }

        public MutableLiveData<Boolean> isEnabled()
        {
            return mEnabled;
        }

        public Kit setEnabled(Boolean enabled)
        {
            mEnabled.setValue(enabled);
            return this;
        }

        public MutableLiveData<Boolean> isEditAllowed()
        {
            return mEditAllowed;
        }
    }

    private SparseArray<Kit> mKits;

    public GearKitsViewModel()
    {
        mKits = new SparseArray<>(G.Z6 + 1);
        Kit z6 = new Kit(null);
        Kit z5 = new Kit(z6);
        Kit z4 = new Kit(z5);
        Kit z3 = new Kit(z4);
        Kit z2 = new Kit(z3);
        Kit z1 = new Kit(z2);
        Kit z0 = new Kit(z1);
        mKits.put(G.Z0, z0);
        mKits.put(G.Z1, z1);
        mKits.put(G.Z2, z2);
        mKits.put(G.Z3, z3);
        mKits.put(G.Z4, z4);
        mKits.put(G.Z5, z5);
        mKits.put(G.Z6, z6);
    }

    public Kit get(int z)
    {
        return mKits.get(z);
    }
}
