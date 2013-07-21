package com.iglooit.core.lib.client;

import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.List;

public class HandlerHelper
{
    private final List<HandlerRegistration> registeredHandlers = new ArrayList<HandlerRegistration>();

    public void add(HandlerRegistration handlerRegistration)
    {
        registeredHandlers.add(handlerRegistration);
    }

    public final void clear()
    {
        for (HandlerRegistration registeredHandler : registeredHandlers)
            registeredHandler.removeHandler();
        registeredHandlers.clear();
    }

}
