package com.iglooit.core.security.client.event;

import com.clarity.core.security.iface.access.domain.Privilege;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import java.util.Set;


public class PrivilegeReadListEvent extends GwtEvent<PrivilegeReadListEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private Set<Privilege> privileges;


    public PrivilegeReadListEvent(Set<Privilege> privileges)
    {
        this.privileges = privileges;
    }

    public Set<Privilege> getPrivileges()
    {
        return privileges;
    }


    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(PrivilegeReadListEvent.Handler handler)
    {
        handler.handle(this);

    }

    public interface Handler extends EventHandler
    {
        void handle(PrivilegeReadListEvent event);
    }


}