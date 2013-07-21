package com.iglooit.core.base.client;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.core.account.iface.command.request.UserRoleReadActiveRequest;
import com.iglooit.core.account.iface.command.response.UserRoleReadResponse;
import com.iglooit.core.base.client.controller.EventAsyncCallback;
import com.iglooit.core.base.client.controller.GAsyncCallback;
import com.iglooit.core.base.client.event.DomainEntityLoadEvent;
import com.iglooit.core.base.client.event.MetaListLoadEvent;
import com.iglooit.core.base.client.event.PreLoadCompletedEvent;
import com.iglooit.core.base.client.event.StatusErrorEvent;
import com.iglooit.core.base.client.event.UserRoleReadActiveEvent;
import com.iglooit.core.base.iface.command.DomainEntityListResponse;
import com.iglooit.core.base.iface.command.DomainEntityResponse;
import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.domain.DomainEntity;
import com.iglooit.core.command.client.CommandServiceClientImpl;
import com.iglooit.core.security.client.event.PrivilegeReadListEvent;
import com.iglooit.core.security.iface.access.request.PrivilegeReadListRequest;
import com.iglooit.core.security.iface.access.response.PrivilegeReadListResponse;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

import java.util.HashSet;

public class AbstractCommands
{
    private final CommandServiceClientImpl commandService;
    private final HandlerManager globalEventBus;

    public AbstractCommands(HandlerManager globalEventBus, CommandServiceClientImpl commandService)
    {
        this.globalEventBus = globalEventBus;
        this.commandService = commandService;
    }

    public CommandServiceClientImpl getCommandService()
    {
        return commandService;
    }

    public HandlerManager getGlobalEventBus()
    {
        return globalEventBus;
    }

    /**
     * Read the privilege list from the server
     *
     * @param eventBus
     */
    public void privilegeReadList(final HandlerManager eventBus)
    {
        getCommandService().run(new PrivilegeReadListRequest(),
            new EventAsyncCallback<PrivilegeReadListResponse>(getGlobalEventBus(), null, "Could not load privileges")
            {
                @Override
                public void onSuccess(PrivilegeReadListResponse response)
                {
                    eventBus.fireEvent(new PrivilegeReadListEvent(new HashSet(response.getDomainEntityList())));
                }
            });
    }

    /**
     * This method compromises on object creation (event must exist prior to server side call) in exchange for providing
     * common logic for a domain entity loaded UI event.
     *
     * @param <E>        the domainEntityEvent to fire on the client.
     * @param <RESPONSE> the domain entity response from the server
     * @param <DE>       the domain entity in question referenced by the domain entity response and the
     *            domainEntityEvent
     * @param request
     * @param eventBus
     * @param event
     * @param errorEvent
     */
    public <RESPONSE extends DomainEntityResponse<DE>, DE extends DomainEntity, E extends DomainEntityLoadEvent<DE>,
        ERRE extends GwtEvent>
    void requestEntity(Request<RESPONSE> request, final HandlerManager eventBus, final E event,
                       final ERRE errorEvent)
    {
        getCommandService().run(request, new EventAsyncCallback<RESPONSE>(eventBus)
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                if (errorEvent == null)
                {
                    super.onFailure(throwable);
                }
                else
                {
                    eventBus.fireEvent(errorEvent);
                }
            }

            @Override
            public void onSuccess(RESPONSE response)
            {
                event.setDomainEntity(response.getDomainEntity());
                eventBus.fireEvent(event);
            }
        });
    }

    public <RESPONSE extends DomainEntityListResponse<DE>, DE extends Meta, E extends MetaListLoadEvent<DE>>
    void requestEntityList(Request<RESPONSE> request, final HandlerManager eventBus,
                           final E successEvent, final StatusErrorEvent errorEvent)
    {
        getCommandService().run(request, new EventAsyncCallback<RESPONSE>(eventBus)
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                if (errorEvent == null)
                {
                    super.onFailure(throwable);
                }
                else
                {
                    eventBus.fireEvent(errorEvent);
                }
            }

            @Override
            public void onSuccess(RESPONSE response)
            {
                successEvent.setDomainEntities(response.getDomainEntityList());
                eventBus.fireEvent(successEvent);
            }
        });
    }

    public void getUserRoleActiveRequest(final HandlerManager eventBus)
    {
        getCommandService().run(new UserRoleReadActiveRequest(), new GAsyncCallback<UserRoleReadResponse>()
        {
            @Override
            public void onSuccess(UserRoleReadResponse result)
            {
                eventBus.fireEvent(new UserRoleReadActiveEvent(result.getUserRole()));
            }
        });
    }

    /**
     * Override to load any additional settings asynchronously before the application is first binded and laid out.
     * Must fire PreLoadCompletedEvent on completion otherwise the application will not load.
     * By default it fires PreLoadCompletedEvent immediately.
     *
     * @param eventBus
     */
    public void preLoadApplication(HandlerManager eventBus)
    {
        eventBus.fireEvent(new PreLoadCompletedEvent());
    }
}
