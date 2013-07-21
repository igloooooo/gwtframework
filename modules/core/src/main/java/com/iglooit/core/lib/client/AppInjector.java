package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.core.base.client.comms.UniversalCommsService;
import com.clarity.core.command.client.CommandServiceClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AppInjectorModule.class)
public interface AppInjector extends Ginjector
{
    AppInjector INSTANCE = GWT.create(AppInjector.class);

    SystemDateProvider.SystemDateUtil getSystemDateUtil();

    CommandServiceClient getCommandServiceClient();

    HandlerManager getGlobalEventBus();

    UniversalHandlerManager getUniversalEventBus();

    UniversalCommsService getUniversalCommsService();
}
