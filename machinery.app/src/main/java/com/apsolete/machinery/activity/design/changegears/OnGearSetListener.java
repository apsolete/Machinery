package com.apsolete.machinery.activity.design.changegears;

public interface OnGearSetListener
{
    void onSelectGears(int id);

    void onGearsChanged(int id, boolean empty);
}
