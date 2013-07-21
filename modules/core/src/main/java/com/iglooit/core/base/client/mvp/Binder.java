package com.iglooit.core.base.client.mvp;

public abstract class Binder<X>
{
    private static final BinderTracker INSTANCE = new BinderTracker();

    public static BinderTracker getTracker()
    {
        return INSTANCE;
    }

    private String metaFieldName;

    protected Binder(String metaFieldName)
    {
        this.metaFieldName = metaFieldName;
        getTracker().addBinder(this);
    }

    public String getMetaFieldName()
    {
        return metaFieldName;
    }

    public abstract boolean isModified();

    public abstract void unbind();

    public abstract String getFieldLabel();

    public abstract void undoModifications();
}
