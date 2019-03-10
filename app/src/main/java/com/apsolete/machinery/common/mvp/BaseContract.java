package com.apsolete.machinery.common.mvp;

import android.support.v4.app.Fragment;

public interface BaseContract
{
    interface Model
    {
        void load();
    }

    interface Presenter
    {
        void start();
        void stop();
        Fragment getFragmentView();
    }

    interface FragmentView
    {
        Fragment asFragment();
    }

    interface View<P extends Presenter> extends FragmentView
    {
        void setPresenter(P presenter);
    }
}
