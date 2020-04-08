package com.apsolete.machinery.common;

public class Event<T>
{
    private T content;
    private boolean isHandled = false;

    public T getContentIfNotHandled()
    {
        if (isHandled)
        {
            return null;
        }
        else
        {
            isHandled = true;
            return content;
        }
    }

    public T peekContent()
    {
        return content;
    }

    public boolean isHandled()
    {
        return isHandled;
    }
}
