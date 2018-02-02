package com.apsolete.machinery.common;

public class ProgressPublisher
{
    private int _total = 1;
    private int _progress = 0;
    private OnProgressListener _listener;
    
    public ProgressPublisher(OnProgressListener listener)
    {
        if (listener == null)
            throw new NullPointerException();
            
        _listener = listener;
    }
    
    public void publish()
    {
        _listener.onProgress((100 * _progress) / _total);
        _progress++;
    }
    
    public void reset(int total)
    {
        _total = total;
        _progress = 0;
    }
}
