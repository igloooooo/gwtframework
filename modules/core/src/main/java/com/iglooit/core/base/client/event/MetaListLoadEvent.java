package com.iglooit.core.base.client.event;

import com.clarity.commons.iface.domain.meta.Meta;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public abstract class MetaListLoadEvent<DE extends Meta>
    extends GwtEvent<MetaListLoadEvent.Handler>
{
    private List<DE> domainEntities;

    public MetaListLoadEvent(List<DE> domainEntities)
    {
        this.domainEntities = domainEntities;
    }

    public List<DE> getDomainEntities()
    {
        return domainEntities;
    }

    public void setDomainEntities(List<DE> domainEntities)
    {
        this.domainEntities = domainEntities;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler<E extends MetaListLoadEvent> extends EventHandler
    {
        void handle(E event);
    }

}
