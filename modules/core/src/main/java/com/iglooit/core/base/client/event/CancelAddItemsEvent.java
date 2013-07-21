package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public class CancelAddItemsEvent extends GwtEvent<CancelAddItemsEventHandler>
{
    private static final Type<CancelAddItemsEventHandler> TYPE = new Type<CancelAddItemsEventHandler>();

    private final List<MetaModelData> itemsToAdd;

    public CancelAddItemsEvent(List<MetaModelData> itemsToAdd)
    {
        this.itemsToAdd = itemsToAdd;
    }

    public static Type<CancelAddItemsEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<CancelAddItemsEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    public List<MetaModelData> getItemsToAdd()
    {
        return itemsToAdd;
    }

    @Override
    protected void dispatch(CancelAddItemsEventHandler handler)
    {
        handler.handle(this);
    }
}
