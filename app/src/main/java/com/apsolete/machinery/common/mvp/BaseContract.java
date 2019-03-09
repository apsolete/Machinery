package com.apsolete.machinery.common.mvp;

import android.support.v4.app.Fragment;

public interface BaseContract
{
    interface BaseModel
    {
        void load();
    }

    interface BasePresenter
    {
        void start();
        void stop();
        Fragment getFragmentView();
    }

    interface BaseFragmentView
    {
        Fragment asFragment();
    }

    interface BaseView<P extends BasePresenter> extends BaseFragmentView
    {
        void setPresenter(P presenter);
    }
}
