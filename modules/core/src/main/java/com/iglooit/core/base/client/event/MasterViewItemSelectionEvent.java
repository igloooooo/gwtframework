package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MasterViewItemSelectionEvent extends GwtEvent<MasterViewItemSelectionEvent.Handler>
{

    public static final Type<Handler> TYPE = new Type<Handler>();

    private MetaModelData metaModelData;

    public MasterViewItemSelectionEvent()
    {

    }

    public MasterViewItemSelectionEvent(MetaModelData metaModelData)
    {
        this.metaModelData = metaModelData;
    }

    public MetaModelData getMetaModelData()
    {
        return metaModelData;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public interface Handler extends EventHandler
    {
        void handle(MasterViewItemSelectionEvent event);
    }
}
