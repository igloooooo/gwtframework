package com.iglooit.core.base.client.event;

import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.base.client.event.UserRoleReadActiveEvent.Handler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UserRoleReadActiveEvent extends GwtEvent<UserRoleReadActiveEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private UserRole userRole;

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public UserRoleReadActiveEvent()
    {
    }

    public UserRoleReadActiveEvent(UserRole userRole)
    {
        this.userRole = userRole;
    }

    public UserRole getUserRole()
    {
        return userRole;
    }

    @Override
    public void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(UserRoleReadActiveEvent event);
    }
}
