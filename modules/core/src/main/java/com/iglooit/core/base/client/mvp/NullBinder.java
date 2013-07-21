package com.iglooit.core.base.client.mvp;

public class NullBinder extends Binder
{
    public NullBinder(String metaFieldName)
    {
        super(metaFieldName);
    }

    public boolean isModified()
    {
        return false;
    }

    public void unbind()
    {
        getTracker().removeBinder(this);
    }

    public String getFieldLabel()
    {
        return "";
    }

    public void undoModifications()
    {

    }
}
