package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.common.*;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;


public class GearKits implements Parcelable
{
    private class GearKit
    {
        public int[] kit;
        public boolean isChecked;

        public GearKit(int[] kit, boolean isChecked)
        {
            this.kit = kit;
            this.isChecked = isChecked;
        }

        public GearKit(Parcel in)
        {
            kit = in.createIntArray();
            isChecked = in.readByte() == 1;
        }

        public void writeToParcel(Parcel out, int flags)
        {
            out.writeIntArray(kit);
            out.writeByte(isChecked ? (byte)1 : (byte)0);
        }
    }

    private SparseArray<GearKit> _gearKits;

    public GearKits()
    {
        _gearKits = new SparseArray<>(G.Z6 + 1);
        _gearKits.put(G.Z0, new GearKit(null, false));
        _gearKits.put(G.Z1, new GearKit(null, false));
        _gearKits.put(G.Z2, new GearKit(null, false));
        _gearKits.put(G.Z3, new GearKit(null, false));
        _gearKits.put(G.Z4, new GearKit(null, false));
        _gearKits.put(G.Z5, new GearKit(null, false));
        _gearKits.put(G.Z6, new GearKit(null, false));
    }

    protected GearKits(Parcel in)
    {
        _gearKits = new SparseArray<>(G.Z6 + 1);
        _gearKits.put(G.Z0, new GearKit(in));
        _gearKits.put(G.Z1, new GearKit(in));
        _gearKits.put(G.Z2, new GearKit(in));
        _gearKits.put(G.Z3, new GearKit(in));
        _gearKits.put(G.Z4, new GearKit(in));
        _gearKits.put(G.Z5, new GearKit(in));
        _gearKits.put(G.Z6, new GearKit(in));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        _gearKits.get(G.Z0).writeToParcel(out, flags);
        _gearKits.get(G.Z1).writeToParcel(out, flags);
        _gearKits.get(G.Z2).writeToParcel(out, flags);
        _gearKits.get(G.Z3).writeToParcel(out, flags);
        _gearKits.get(G.Z4).writeToParcel(out, flags);
        _gearKits.get(G.Z5).writeToParcel(out, flags);
        _gearKits.get(G.Z6).writeToParcel(out, flags);
    }

    public static final Creator<GearKits> CREATOR = new Creator<GearKits>()
    {
        @Override
        public GearKits createFromParcel(Parcel in)
        {
            return new GearKits(in);
        }

        @Override
        public GearKits[] newArray(int size)
        {
            return new GearKits[size];
        }
    };

    public int[] get(int kit)
    {
        return _gearKits.get(kit).kit;
    }

    public int[] getZ0()
    {
        return _gearKits.get(G.Z0).kit;
    }

    public int[] getZ1()
    {
        return _gearKits.get(G.Z1).kit;
    }

    public int[] getZ2()
    {
        return _gearKits.get(G.Z2).kit;
    }

    public int[] getZ3()
    {
        return _gearKits.get(G.Z3).kit;
    }

    public int[] getZ4()
    {
        return _gearKits.get(G.Z4).kit;
    }

    public int[] getZ5()
    {
        return _gearKits.get(G.Z5).kit;
    }

    public int[] getZ6()
    {
        return _gearKits.get(G.Z6).kit;
    }

    public void put(int kit, int[] zz)
    {
        _gearKits.get(kit).kit = zz;
    }

    public void putZ0(int[] zz)
    {
        _gearKits.get(G.Z0).kit = zz;
    }

    public void putZ1(int[] zz)
    {
        _gearKits.get(G.Z1).kit = zz;
    }

    public void putZ2(int[] zz)
    {
        _gearKits.get(G.Z2).kit = zz;
    }

    public void putZ3(int[] zz)
    {
        _gearKits.get(G.Z3).kit = zz;
    }

    public void putZ4(int[] zz)
    {
        _gearKits.get(G.Z4).kit = zz;
    }

    public void putZ5(int[] zz)
    {
        _gearKits.get(G.Z5).kit = zz;
    }

    public void putZ6(int[] zz)
    {
        _gearKits.get(G.Z6).kit = zz;
    }

    public boolean isChecked(int kit)
    {
        return _gearKits.get(kit).isChecked;
    }

    public void setIsChecked(int kit, boolean isChecked)
    {
        _gearKits.get(kit).isChecked = isChecked;
    }
}
