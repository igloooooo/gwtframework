package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public class RemoveItemsEvent extends GwtEvent<RemoveItemsEventHandler>
{
    private static final Type<RemoveItemsEventHandler> TYPE = new Type<RemoveItemsEventHandler>();

    private final List<MetaModelData> itemsToRemove;

    public RemoveItemsEvent(List<MetaModelData> itemsToRemove)
    {
        this.itemsToRemove = itemsToRemove;
    }

    public static Type<RemoveItemsEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<RemoveItemsEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    public List<MetaModelData> getItemsToRemove()
    {
        return itemsToRemove;
    }

    @Override
    protected void dispatch(RemoveItemsEventHandler handler)
    {
        handler.handle(this);
    }
}
