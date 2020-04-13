package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.common.*;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;


@Deprecated
public class GearSets implements Parcelable
{
    private class GearSet
    {
        public int[] set;
        public boolean isChecked;

        public GearSet(int[] set, boolean isChecked)
        {
            this.set = set;
            this.isChecked = isChecked;
        }

        public GearSet(Parcel in)
        {
            set = in.createIntArray();
            isChecked = in.readByte() == 1;
        }

        public void writeToParcel(Parcel out, int flags)
        {
            out.writeIntArray(set);
            out.writeByte(isChecked ? (byte)1 : (byte)0);
        }
    }

    private SparseArray<GearSet> mGearSets;

    public GearSets()
    {
        mGearSets = new SparseArray<>(G.Z6 + 1);
        mGearSets.put(G.Z0, new GearSet(null, false));
        mGearSets.put(G.Z1, new GearSet(null, false));
        mGearSets.put(G.Z2, new GearSet(null, false));
        mGearSets.put(G.Z3, new GearSet(null, false));
        mGearSets.put(G.Z4, new GearSet(null, false));
        mGearSets.put(G.Z5, new GearSet(null, false));
        mGearSets.put(G.Z6, new GearSet(null, false));
    }

    protected GearSets(Parcel in)
    {
        mGearSets = new SparseArray<>(G.Z6 + 1);
        mGearSets.put(G.Z0, new GearSet(in));
        mGearSets.put(G.Z1, new GearSet(in));
        mGearSets.put(G.Z2, new GearSet(in));
        mGearSets.put(G.Z3, new GearSet(in));
        mGearSets.put(G.Z4, new GearSet(in));
        mGearSets.put(G.Z5, new GearSet(in));
        mGearSets.put(G.Z6, new GearSet(in));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        mGearSets.get(G.Z0).writeToParcel(out, flags);
        mGearSets.get(G.Z1).writeToParcel(out, flags);
        mGearSets.get(G.Z2).writeToParcel(out, flags);
        mGearSets.get(G.Z3).writeToParcel(out, flags);
        mGearSets.get(G.Z4).writeToParcel(out, flags);
        mGearSets.get(G.Z5).writeToParcel(out, flags);
        mGearSets.get(G.Z6).writeToParcel(out, flags);
    }

    public static final Creator<GearSets> CREATOR = new Creator<GearSets>()
    {
        @Override
        public GearSets createFromParcel(Parcel in)
        {
            return new GearSets(in);
        }

        @Override
        public GearSets[] newArray(int size)
        {
            return new GearSets[size];
        }
    };

    public int[] get(int kit)
    {
        return mGearSets.get(kit).set;
    }

    public int[] getZ0()
    {
        return mGearSets.get(G.Z0).set;
    }

    public int[] getZ1()
    {
        return mGearSets.get(G.Z1).set;
    }

    public int[] getZ2()
    {
        return mGearSets.get(G.Z2).set;
    }

    public int[] getZ3()
    {
        return mGearSets.get(G.Z3).set;
    }

    public int[] getZ4()
    {
        return mGearSets.get(G.Z4).set;
    }

    public int[] getZ5()
    {
        return mGearSets.get(G.Z5).set;
    }

    public int[] getZ6()
    {
        return mGearSets.get(G.Z6).set;
    }

    public void put(int kit, int[] zz)
    {
        mGearSets.get(kit).set = zz;
    }

    public void putZ0(int[] zz)
    {
        mGearSets.get(G.Z0).set = zz;
    }

    public void putZ1(int[] zz)
    {
        mGearSets.get(G.Z1).set = zz;
    }

    public void putZ2(int[] zz)
    {
        mGearSets.get(G.Z2).set = zz;
    }

    public void putZ3(int[] zz)
    {
        mGearSets.get(G.Z3).set = zz;
    }

    public void putZ4(int[] zz)
    {
        mGearSets.get(G.Z4).set = zz;
    }

    public void putZ5(int[] zz)
    {
        mGearSets.get(G.Z5).set = zz;
    }

    public void putZ6(int[] zz)
    {
        mGearSets.get(G.Z6).set = zz;
    }

    public boolean isChecked(int kit)
    {
        return mGearSets.get(kit).isChecked;
    }

    public void setChecked(int kit, boolean isChecked)
    {
        mGearSets.get(kit).isChecked = isChecked;
    }
}
