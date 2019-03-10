package com.apsolete.machinery.common.mvp;

import com.apsolete.machinery.common.ContentBase;

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
        ContentBase getContent();
    }

    interface ContentView
    {
        ContentBase getContent();
    }

    interface View<P extends Presenter> extends ContentView
    {
        void setPresenter(P presenter);
    }
}
