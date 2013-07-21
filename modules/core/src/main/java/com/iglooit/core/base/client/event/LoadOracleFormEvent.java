package com.iglooit.core.base.client.event;

import com.clarity.core.legacy.iface.model.OracleFormsLink;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LoadOracleFormEvent extends GwtEvent<LoadOracleFormEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private OracleFormsLink oracleFormsLink;
    private String id;

    public LoadOracleFormEvent(String id, OracleFormsLink oracleFormsLink)
    {
        this.id = id;
        this.oracleFormsLink = oracleFormsLink;
    }

    public String getId()
    {
        return id;
    }

    public OracleFormsLink getOracleFormsLink()
    {
        return oracleFormsLink;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler loadOracleFormLink)
    {
        loadOracleFormLink.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(LoadOracleFormEvent event);
    }
}
