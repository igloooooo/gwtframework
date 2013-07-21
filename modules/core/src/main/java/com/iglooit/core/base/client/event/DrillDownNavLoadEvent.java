package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.widget.layoutcontainer.DrillDownNavEntity;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DrillDownNavLoadEvent extends GwtEvent<DrillDownNavLoadEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private DrillDownNavEntity drillDownEntity;
    private String drillDownEntityName;
    private boolean isBreadcrumbClick;

    public DrillDownNavLoadEvent(String drillDownEntityName,
                                 boolean isBreadcrumbClick)
    {
        this.drillDownEntityName = drillDownEntityName;
        this.isBreadcrumbClick = isBreadcrumbClick;
    }

    public DrillDownNavEntity getDrillDownEntity()
    {
        return drillDownEntity;
    }

    public void setDrillDownEntity(DrillDownNavEntity drillDownEntity)
    {
        this.drillDownEntity = drillDownEntity;
    }

    public String getDrillDownEntityName()
    {
        return drillDownEntityName;
    }

    public boolean isBreadcrumbClick()
    {
        return isBreadcrumbClick;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    @Override
    public Type getAssociatedType()
    {
        return TYPE;
    }

    public interface Handler extends EventHandler
    {
        void handle(DrillDownNavLoadEvent event);
    }
}
