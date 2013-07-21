package com.iglooit.core.base.client.event;

import com.clarity.core.lib.client.MetaModelData;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PreEditItemEvent extends GwtEvent<PreEditItemEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private final MetaModelData itemEditing;

    public PreEditItemEvent(MetaModelData item)
    {
        this.itemEditing = item;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public MetaModelData getItemEditing()
    {
        return itemEditing;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler<E extends PreEditItemEvent> extends EventHandler
    {
        void handle(E event);
    }
}
