package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.mvp.Presenter;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPresenterEvent extends GwtEvent<AddPresenterEvent.Handler>
{
    private final Presenter presenter;
    private final boolean inNewTab;
    private List<String> parameterList;
    public static final Type<Handler> TYPE = new Type<Handler>();

    public AddPresenterEvent(Presenter presenter)
    {
        this.presenter = presenter;
        this.inNewTab = false;
        this.parameterList = new ArrayList<String>();
    }

    public AddPresenterEvent(Presenter presenter, boolean inNewTab, String... parameters)
    {
        this.inNewTab = inNewTab;
        this.presenter = presenter;
        this.parameterList = Arrays.asList(parameters);
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public Presenter getPresenter()
    {
        return presenter;
    }

    public boolean isInNewTab()
    {
        return inNewTab;
    }

    public List<String> getParameterList()
    {
        return parameterList;
    }

    public interface Handler extends EventHandler
    {
        void handle(AddPresenterEvent event);
    }
}
