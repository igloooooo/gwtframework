package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.core.base.client.comms.UniversalCommsService;
import com.clarity.core.base.client.controller.ClarityHandlerManager;
import com.clarity.core.command.client.CommandServiceClient;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppInjectorModule extends AbstractGinModule
{
    private static HandlerManager singletonHandlerManager;
    private static UniversalHandlerManager singletonUniversalHandlerManager;

    @Override
    protected void configure()
    {
        bind(SystemDateProvider.SystemDateUtil.class).to(SystemDateClientUtil.class).in(Singleton.class);
        bind(CommandServiceClient.class).to(CommandServiceClientImpl.class).in(Singleton.class);
        bind(UniversalCommsService.class).in(Singleton.class);
    }

    // Note: 'singleton' in gin is per module, this ensures that a true global singleton is returned
    // http://code.google.com/p/mvp4g/wiki/GinIntegration
    @Provides
    public HandlerManager getGlobalEventBus()
    {
        if (singletonHandlerManager == null)
            singletonHandlerManager = new ClarityHandlerManager();

        return singletonHandlerManager;
    }

    // Note: 'singleton' in gin is per module, this ensures that a true global singleton is returned
    // http://code.google.com/p/mvp4g/wiki/GinIntegration
    @Provides
    public UniversalHandlerManager getUniversalEventBus()
    {
        if (singletonUniversalHandlerManager == null)
            singletonUniversalHandlerManager = new UniversalHandlerManager(null);

        return singletonUniversalHandlerManager;
    }
}
