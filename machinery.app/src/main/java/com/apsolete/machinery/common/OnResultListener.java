package com.apsolete.machinery.common;

public interface OnResultListener<T> extends OnProgressListener
{
    void onResult(T result);
    void onCompleted(int count);
}
