package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import java.util.Set;

public class UserRoleCheckActivePrivilegesEvent extends GwtEvent<UserRoleCheckActivePrivilegesEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private Set<String> userRolePrivileges;

    public UserRoleCheckActivePrivilegesEvent(Set<String> userRolePrivileges)
    {
        this.userRolePrivileges = userRolePrivileges;
    }

    public Set<String> getUserRolePrivileges()
    {
        return userRolePrivileges;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    @Override
    public Type getAssociatedType()
    {
        return TYPE;
    }

    public interface Handler extends EventHandler
    {
        void handle(UserRoleCheckActivePrivilegesEvent event);
    }
}
