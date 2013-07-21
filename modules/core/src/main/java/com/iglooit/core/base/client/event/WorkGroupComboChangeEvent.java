package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class WorkGroupComboChangeEvent extends GwtEvent<WorkGroupComboChangeEvent.Handler>
{

    private static final Type<Handler> TYPE =
        new Type<Handler>();
    private String workGroupName;
    private Boolean isSameWorkGroup = Boolean.FALSE;

    public WorkGroupComboChangeEvent()
    {

    }

    public WorkGroupComboChangeEvent(String workGroupName)
    {
        this.workGroupName = workGroupName;
    }

    public WorkGroupComboChangeEvent(String workGroupName, Boolean sameWorkGroup)
    {
        this.workGroupName = workGroupName;
        isSameWorkGroup = sameWorkGroup;
    }

    public Boolean isSameWorkGroup()
    {
        return isSameWorkGroup;
    }

    public static Type<Handler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public String getWorkGroupName()
    {
        return workGroupName;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(WorkGroupComboChangeEvent event);
    }

}
