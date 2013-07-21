package com.iglooit.core.base.client.mvp;

import com.clarity.commons.iface.type.AppX;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DefaultPresenter<D extends Display> implements Presenter, HandlerRegistration
{
    private final D display;
    private HandlerManager localEventBus = new HandlerManager(null);
    private HandlerManager sharedEventBus;
    private final List<HandlerRegistration> registeredHandlers = new ArrayList<HandlerRegistration>();

    public DefaultPresenter(D display)
    {
        this.display = display;
    }

    public DefaultPresenter(D display, HandlerManager sharedEventBus)
    {
        this.display = display;
        this.sharedEventBus = sharedEventBus;
    }

    public void setSharedEventBus(HandlerManager sharedEventBus)
    {
        this.sharedEventBus = sharedEventBus;
    }

    public HandlerManager getSharedEventBus()
    {
        return sharedEventBus;
    }

    public HandlerManager getLocalEventBus()
    {
        return localEventBus;
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

    protected void doOnUnbind()
    {
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
            registeredHandler.removeHandler();
        registeredHandlers.clear();
        doOnUnbind();
    }

    public D getDisplay()
    {
        return display;
    }

    public void removeHandler()
    {
        unbind();
    }

    public void setLocalEventBus(HandlerManager localEventBus)
    {
        this.localEventBus = localEventBus;
    }
}
