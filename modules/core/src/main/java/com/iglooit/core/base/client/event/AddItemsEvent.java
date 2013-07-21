package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public class AddItemsEvent extends GwtEvent<AddItemsEventHandler>
{
    private static final Type<AddItemsEventHandler> TYPE = new Type<AddItemsEventHandler>();

    private final List<MetaModelData> itemsToAdd;

    public AddItemsEvent(List<MetaModelData> itemsToAdd)
    {
        this.itemsToAdd = itemsToAdd;
    }

    public static Type<AddItemsEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<AddItemsEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    public List<MetaModelData> getItemsToAdd()
    {
        return itemsToAdd;
    }

    @Override
    protected void dispatch(AddItemsEventHandler handler)
    {
        handler.handle(this);
    }
}
