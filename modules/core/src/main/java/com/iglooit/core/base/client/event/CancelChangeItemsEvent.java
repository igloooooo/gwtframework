package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public class CancelChangeItemsEvent extends GwtEvent<CancelChangeItemsEventHandler>
{
    private static final Type<CancelChangeItemsEventHandler> TYPE = new Type<CancelChangeItemsEventHandler>();

    private final List<MetaModelData> itemsToAdd;

    public CancelChangeItemsEvent(List<MetaModelData> itemsToAdd)
    {
        this.itemsToAdd = itemsToAdd;
    }

    public static Type<CancelChangeItemsEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<CancelChangeItemsEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    public List<MetaModelData> getItemsToAdd()
    {
        return itemsToAdd;
    }

    @Override
    protected void dispatch(CancelChangeItemsEventHandler handler)
    {
        handler.handle(this);
    }
}
