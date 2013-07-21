package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PreAddItemEvent extends GwtEvent<PreAddItemEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private final MetaModelData itemToAdd;

    public PreAddItemEvent(MetaModelData item)
    {
        this.itemToAdd = item;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public MetaModelData getItemToAdd()
    {
        return itemToAdd;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler<E extends PreAddItemEvent> extends EventHandler
    {
        void handle(E event);
    }
}
