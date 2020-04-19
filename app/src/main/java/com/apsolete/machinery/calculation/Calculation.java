package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.CustomSettingsFragment;
import com.apsolete.machinery.common.Event;

public final class Calculation
{
    public static final int NOTIFY_MESSAGE = 1;
    public static final int NOTIFY_CLEAR = 2;

    public static class Notify
    {
        public int action;
        public String message;

        public Notify(int action, String message)
        {
            this.action = action;
            this.message = message;
        }
    }

    public static Event<Notify> notifyMessage(String message)
    {
        return new Event<>(new Notify(Calculation.NOTIFY_MESSAGE, message));
    }

    public static Event<Notify> notifyAction(int action)
    {
        return new Event<>(new Notify(action, null));
    }

    interface Model
    {

    }

    interface ViewModel
    {
        boolean validate();
        void save();
        void clear();
        void calculate();
        boolean close();
    }

    interface View
    {
        CustomSettingsFragment getSettings();
        boolean close();
    }
}
