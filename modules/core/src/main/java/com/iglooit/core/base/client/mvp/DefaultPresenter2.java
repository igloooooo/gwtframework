package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.client.navigator.INavigator;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO - merge into DefaultPresenter?
public abstract class DefaultPresenter2<D extends Display> implements Presenter, HandlerRegistration
{
    private final D display;
    private HandlerManager sharedEventBus;
    private CommandServiceClientImpl commandService;
    private INavigator navigator;

    private HandlerManager localEventBus;

    private final List<HandlerRegistration> registeredHandlers = new ArrayList<HandlerRegistration>();

    // TODO - Ditch this constructor and just have it all injected.
    public DefaultPresenter2(D display, HandlerManager sharedEventBus, CommandServiceClientImpl commandService,
                             INavigator navigator)
    {
        this.display = display;
        this.sharedEventBus = sharedEventBus;
        this.commandService = commandService;
        this.navigator = navigator;
    }

    public D getDisplay()
    {
        return display;
    }

    public HandlerManager getSharedEventBus()
    {
        return sharedEventBus;
    }

    public CommandServiceClientImpl getCommandService()
    {
        return commandService;
    }

    public INavigator getNavigator()
    {
        return navigator;
    }

    // If this wasn't running as javascript then we'd have to add the "synchronized" keyword.
    public HandlerManager getLocalEventBus()
    {
        if (localEventBus == null)
        {
            localEventBus = new HandlerManager(null);
        }
        return localEventBus;
    }

    @Override
    public void bind()
    {
        GWT.log("Binding " + this.getClass().getName());
    }

    public final void unbind()
    {
        GWT.log("unbinding" + this.getClass().getName());

        for (HandlerRegistration registeredHandler : registeredHandlers)
        {
            registeredHandler.removeHandler();
        }
        registeredHandlers.clear();
        doOnUnbind();
    }

    protected void doOnUnbind()
    {
    }

    public HandlerRegistration addHandlerRegistration(HandlerRegistration handlerRegistration)
    {
        registeredHandlers.add(handlerRegistration);
        return handlerRegistration;
    }

    public void addHandlerRegistration(Collection<HandlerRegistration> handlerRegistration)
    {
        registeredHandlers.addAll(handlerRegistration);
    }

    protected void removeHandlerRegistration(HandlerRegistration handlerRegistration)
    {
        if (!registeredHandlers.remove(handlerRegistration))
            throw new AppX("Couldn't remove handler registration");
    }

    public void removeHandler()
    {
        unbind();
    }
}
