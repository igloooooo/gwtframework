package com.iglooit.core.base.client;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.account.iface.command.request.UserRoleReadActiveRequest;
import com.clarity.core.account.iface.command.response.UserRoleReadResponse;
import com.clarity.core.account.iface.i18n.AccountViewConstants;
import com.clarity.core.base.client.composition.display.StatusInfo;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.PreLoadCompletedEvent;
import com.clarity.core.base.client.event.StatusErrorEvent;
import com.clarity.core.base.client.event.StatusInfoEvent;
import com.clarity.core.base.client.event.StatusSuccessEvent;
import com.clarity.core.base.client.mvp.Presenter;
import com.clarity.core.base.client.presenter.NavigationPresenter;
import com.clarity.core.base.client.util.DialogUtil;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.widget.misc.NavigationView;
import com.clarity.core.base.iface.domain.ClarityEntryPoint;
import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.command.client.DefaultRequestCallback;
import com.clarity.core.command.iface.LoginMessageConstants;
import com.clarity.core.lib.client.AppInjector;
import com.clarity.core.lib.client.JoinableEventBus;
import com.clarity.core.security.client.event.PrivilegeReadListEvent;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Date;

/**
 * Provides all the commonly repeated components for building an entry point.
 */
public abstract class AbstractEntryPoint implements EntryPoint
{
    private HandlerManager globalEventBus = AppInjector.INSTANCE.getGlobalEventBus();

    private AbstractCommands commands;

    private final CommandServiceClientImpl commandService =
        (CommandServiceClientImpl)AppInjector.INSTANCE.getCommandServiceClient();
    private ClientI18NFactory clientI18NFactory;
    private PrivilegeResolver privilegeResolver;
    private final Viewport viewport = new Viewport();
    private LayoutContainer mainPanel;
    private DateCacheEntry dateCacheEntry;

    private NavigationPresenter navigationPresenter = null;
    private JoinableEventBus joinableEventBus;

    /**
     * Create and initialize the display
     */
    protected abstract void init();


    protected Date getCurrentSystemDate()
    {
        return dateCacheEntry.now();
    }

    @Override
    public final void onModuleLoad()
    {
        I18NFactoryProvider.setFactoryStatic(getClientI18NFactory());
        JpaDomainEntity.setUuidFactory(new ClientUUIDFactory());
        SystemDateProvider.setSystemDateUtilStatic(AppInjector.INSTANCE.getSystemDateUtil());
        privilegeResolver = new PrivilegeResolver();

        //viewport.setBorders(true);
        RootPanel.get().add(viewport);
        viewport.setLayout(new BorderLayout());
        //TODO RL: remove for now by add back in when styling complete

        String navStyle = Window.Location.getParameter("navStyle");
        if (navStyle == null || !navStyle.equals("hidden"))
        {
            final NavigationView navView = new NavigationView(getSelectedEntryPoint());
            viewport.add(navView, new BorderLayoutData(Style.LayoutRegion.NORTH, (float)NavigationView.DEFAULT_HEIGHT));

            navigationPresenter = new NavigationPresenter(navView, commandService,
                globalEventBus);
        }

        mainPanel = new LayoutContainer();
        viewport.add(mainPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));
        mainPanel.setLayout(new FitLayout());

        getCommandService().run(new UserRoleReadActiveRequest(), new GAsyncCallback<UserRoleReadResponse>()
        {
            public void onSuccess(final UserRoleReadResponse response)
            {
                SystemDateProvider.getDate(new SystemDateProvider.SystemDateCallback()
                {
                    @Override
                    public void dateReady(DateCacheEntry dateCacheEntry)
                    {
                        // initialise dateCacheEntry before calling init() method
                        AbstractEntryPoint.this.dateCacheEntry = dateCacheEntry;
                        init();
                        registerGenericGlobalEvents();
                        commands = createCommands();

                        DefaultRequestCallback.setCachedUserRole(response.getUserRole());
                        commands.privilegeReadList(joinableEventBus);
                        commands.preLoadApplication(joinableEventBus);


                        if (navigationPresenter != null)
                            navigationPresenter.bind();
                    }
                });
            }
        });
    }

    /**
     * Override this method if need to add navigations
     *
     * @return NavigationView
     */
    protected NavigationView createNavigationView()
    {
        return new NavigationView(getSelectedEntryPoint());
    }

    protected abstract ClarityEntryPoint getSelectedEntryPoint();

    protected abstract AbstractCommands createCommands();

    private void registerGenericGlobalEvents()
    {
        joinableEventBus = new JoinableEventBus();
        getGlobalEventBus().addHandler(StatusSuccessEvent.TYPE, new StatusSuccessEvent.Handler()
        {
            @Override
            public void handle(StatusSuccessEvent event)
            {

                final String message = event.getMessage();
                final String title = StringUtil.isEmpty(event.getTitle()) ? "Success" : event.getTitle();
                if (message != null)
                {
                    StatusInfo.displayWithStyle(title, message, StatusInfo.STYLE_SUCCESS);
                }
            }
        });
        getGlobalEventBus().addHandler(StatusErrorEvent.TYPE, new StatusErrorEvent.Handler()
        {
            @Override
            public void handle(StatusErrorEvent event)
            {

                final String message = event.getMessage();
                final String title = StringUtil.isEmpty(event.getTitle()) ? "Error" : event.getTitle();
                if (message != null)
                {
                    /*
                      This Dialog box have an ok button to acknowledge the error message. The end user need time
                      to read the error message. We cannot use the StatusInfo message as used in a Success event
                    */
                    DialogUtil.showError(title, event.getMessage());
                }
            }
        });

        getGlobalEventBus().addHandler(StatusInfoEvent.TYPE, new StatusInfoEvent.Handler()
        {
            @Override
            public void handle(StatusInfoEvent event)
            {

                final String message = event.getMessage();
                final String title = StringUtil.isEmpty(event.getTitle()) ? "Info" : event.getTitle();
                if (message != null)
                {
                    StatusInfo.displayWithStyle(title, message, StatusInfo.STYLE_INFO);
                }
            }
        });

        //get privileges
        joinableEventBus.addHandler(PrivilegeReadListEvent.TYPE, new PrivilegeReadListEvent.Handler()
        {
            @Override
            public void handle(PrivilegeReadListEvent event)
            {
                //ensure we stop listening after first handle
                joinableEventBus.removeHandler(PrivilegeReadListEvent.TYPE, this);
                privilegeResolver.addPrivileges(event.getPrivileges());
            }
        });

        joinableEventBus.addHandler(PreLoadCompletedEvent.TYPE, new PreLoadCompletedEvent.Handler()
        {
            @Override
            public void handle(PreLoadCompletedEvent event)
            {
                joinableEventBus.removeHandler(PreLoadCompletedEvent.TYPE, this);
            }
        });

        joinableEventBus.track(PrivilegeReadListEvent.TYPE);
        joinableEventBus.track(PreLoadCompletedEvent.TYPE);
        joinableEventBus.setOnNotifyCommand(new Command()
        {
            @Override
            public void execute()
            {
                // clean up
                joinableEventBus.stop();
                joinableEventBus = null;

                Presenter presenter = createContainer();
                mainPanel.add(presenter.getDisplay().asWidget());
                viewport.layout(true);
                presenter.bind();
            }
        });
        joinableEventBus.start();

    }

    protected abstract Presenter createContainer();

    protected HandlerManager getGlobalEventBus()
    {
        return globalEventBus;
    }

    public ClientI18NFactory getClientI18NFactory()
    {
        if (clientI18NFactory == null)
        {
            clientI18NFactory = new ClientI18NFactory();
            clientI18NFactory.addImplementation(LoginMessageConstants.class, GWT.create(LoginMessageConstants.class));
            clientI18NFactory.addImplementation(BaseViewConstants.class, GWT.create(BaseViewConstants.class));
            clientI18NFactory.addImplementation(AccountViewConstants.class, GWT.create(AccountViewConstants.class));
        }
        return clientI18NFactory;
    }


    protected PrivilegeResolver getPrivilegeResolver()
    {
        return privilegeResolver;
    }

    public CommandServiceClientImpl getCommandService()
    {
        return commandService;
    }

    public NavigationPresenter getNavigationPresenter()
    {
        return navigationPresenter;
    }
}
