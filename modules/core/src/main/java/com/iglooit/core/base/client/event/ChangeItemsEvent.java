package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public class ChangeItemsEvent extends GwtEvent<ChangeItemsEventHandler>
{
    private static final Type<ChangeItemsEventHandler> TYPE = new Type<ChangeItemsEventHandler>();

    private final List<MetaModelData> itemsToChange;

    public ChangeItemsEvent(List<MetaModelData> itemsToChange)
    {
        this.itemsToChange = itemsToChange;
    }

    public static Type<ChangeItemsEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<ChangeItemsEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    public List<MetaModelData> getItemsToChange()
    {
        return itemsToChange;
    }

    @Override
    protected void dispatch(ChangeItemsEventHandler handler)
    {
        handler.handle(this);
    }
}
