package com.apsolete.machinery.calculation.gearing.changegears;

import android.util.SparseArray;
import androidx.lifecycle.MutableLiveData;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.Numbers;

public class GearSetsViewModel
{
    private final GSet _set0;
    private final GSet _set1;
    private final GSet _set2;
    private final GSet _set3;
    private final GSet _set4;
    private final GSet _set5;
    private final GSet _set6;

    public static class GSet
    {
        private GSet mNext = null;

        private MutableLiveData<String> mGearsStr = new MutableLiveData<String>(null)
        {
            @Override
            public void setValue(String value)
            {
                super.setValue(value);
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

        public Integer[] getGears()
        {
            return Numbers.getIntegerNumbers(mGearsStr.getValue());
        }

        public MutableLiveData<String> getGearsStr()
        {
            return mGearsStr;
        }

        public GSet setGears(String gears)
        {
            mGearsStr.setValue(gears);
            return this;
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
            return mGearsStr.getValue().isEmpty();
        }
    }

//    private SparseArray<GSet> mGSets;

    public GearSetsViewModel()
    {
//        mGSets = new SparseArray<>(G.Z6 + 1);
        _set6 = new GSet(null);
        _set5 = new GSet(_set6);
        _set4 = new GSet(_set5);
        _set3 = new GSet(_set4);
        _set2 = new GSet(_set3);
        _set1 = new GSet(_set2);
        _set0 = new GSet(_set1);
//        mGSets.put(G.Z0, _set0);
//        mGSets.put(G.Z1, _set1);
//        mGSets.put(G.Z2, _set2);
//        mGSets.put(G.Z3, _set3);
//        mGSets.put(G.Z4, _set4);
//        mGSets.put(G.Z5, _set5);
//        mGSets.put(G.Z6, _set6);
    }

    public GSet get(int z)
    {
        switch (z)
        {
            case G.Z0: return _set0;
            case G.Z1: return _set1;
            case G.Z2: return _set2;
            case G.Z3: return _set3;
            case G.Z4: return _set4;
            case G.Z5: return _set5;
            case G.Z6: return _set6;
        }
        return null;
    }

    public GSet set0()
    {
        return _set0;
    }
    public GSet set1()
    {
        return _set1;
    }
    public GSet set2()
    {
        return _set2;
    }
    public GSet set3()
    {
        return _set3;
    }
    public GSet set4()
    {
        return _set4;
    }
    public GSet set5()
    {
        return _set5;
    }
    public GSet set6()
    {
        return _set6;
    }

    public int getWheelsCount()
    {
        int count = 2;
        if(_set3.isSwitched().getValue())
            count++;
        if(_set4.isSwitched().getValue())
            count++;
        if(_set5.isSwitched().getValue())
            count++;
        if(_set6.isSwitched().getValue())
            count++;

        return count;
    }

    public void init(ChGearsEntity entity)
    {

    }
}
