package com.apsolete.machinery.common.mvp;

import com.apsolete.machinery.common.ContentBase;

@Deprecated
public interface BaseContract
{
    interface Model
    {
        void load();
    }

    @Deprecated
    interface Presenter
    {
        void start();
        void stop();
        ContentBase getContent();
    }

    @Deprecated
    interface ContentView
    {
        ContentBase getContent();
    }

    @Deprecated
    interface View<P extends Presenter> extends ContentView
    {
        void setPresenter(P presenter);
    }
}
