package com.apsolete.machinery.common;

public class Event<T>
{
    private T _content;
    private boolean _isHandled = false;

    public Event(T content)
    {
        _content = content;
    }

    public T getContentIfNotHandled()
    {
        if (_isHandled)
        {
            return null;
        }
        else
        {
            _isHandled = true;
            return _content;
        }
    }

    public T peekContent()
    {
        return _content;
    }

    public boolean isHandled()
    {
        return _isHandled;
    }

    public void setHandled(boolean handled)
    {
        _isHandled = handled;
    }
}
