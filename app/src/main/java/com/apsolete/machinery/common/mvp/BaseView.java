package com.apsolete.machinery.common.mvp;

public interface BaseView<P extends BasePresenter>
{
    void setPresenter(P presenter);
}
