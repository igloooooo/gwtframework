package com.iglooit.core.base.client.comms;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.core.account.iface.command.request.UserRoleReadActiveRequest;
import com.clarity.core.account.iface.command.response.UserRoleReadResponse;
import com.clarity.core.account.iface.i18n.AccountViewConstants;
import com.clarity.core.base.client.AbstractCommands;
import com.clarity.core.base.client.ClientI18NFactory;
import com.clarity.core.base.client.ClientUUIDFactory;
import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.StatusErrorEvent;
import com.clarity.core.base.client.event.StatusInfoEvent;
import com.clarity.core.base.client.event.StatusSuccessEvent;
import com.clarity.core.base.client.mvp.Presenter;
import com.clarity.core.base.client.presenter.NavigationPresenter;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.mxgraph.style.StyleConstants;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.core.base.client.widget.layoutcontainer.StatusDisplay;
import com.clarity.core.base.client.widget.misc.NavigationView;
import com.clarity.core.base.iface.domain.ClarityEntryPoint;
import com.clarity.core.base.iface.domain.FieldNameConstants;
import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.clarity.core.base.iface.validation.ValidationConstants;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.command.client.DefaultRequestCallback;
import com.clarity.core.command.iface.LoginMessageConstants;
import com.clarity.core.lib.client.AppInjector;
import com.clarity.core.lib.iface.TimeConstants;
import com.clarity.core.security.client.event.PrivilegeReadListEvent;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Date;


public abstract class AbstractCommsEntryPoint extends GWTCommsEntryPoint
{
    // take care, only use me after its initialized
    private BaseViewConstants bvc;

    private HandlerManager globalEventBus = new HandlerManager(null);
    private AbstractCommands commands;

    private final CommandServiceClientImpl commandService =
        (CommandServiceClientImpl)AppInjector.INSTANCE.getCommandServiceClient();
    private ClientI18NFactory clientI18NFactory;
    private PrivilegeResolver privilegeResolver;
    private final Viewport viewport = new Viewport();
    private LayoutContainer mainPanel;
    private DateCacheEntry dateCacheEntry;
    private NavigationView navigationView;
    private NavigationPresenter navigationPresenter;

    /**
     * Create and initialize the display
     */
    protected abstract void init();


    protected Date getCurrentSystemDate()
    {
        return dateCacheEntry.now();
    }

    protected BaseViewConstants getBvc()
    {
        return bvc;
    }

    @Override
    public final void onModuleLoad()
    {
        JpaDomainEntity.setUuidFactory(new ClientUUIDFactory());
        I18NFactoryProvider.setFactoryStatic(getClientI18NFactory());
        bvc = I18NFactoryProvider.get(BaseViewConstants.class);
        SystemDateProvider.setSystemDateUtilStatic(AppInjector.INSTANCE.getSystemDateUtil());
        privilegeResolver = new PrivilegeResolver();

        registerGenericGlobalEvents();
        commands = createCommands();
        initLocal();
        init();

        ClarityEntryPoint selectedEntryPoint = getSelectedEntryPoint();
        if (selectedEntryPoint != null && selectedEntryPoint.isShowModuleNavigation())
            configViewPortWithNavView(selectedEntryPoint.isEnableModuleNavigation());
        else
            configViewPortWithOutNavView();

        afterModuleLoad();
    }

    private void initLocal()
    {
        /* can do whatever useful */
    }

    protected void configViewPortWithNavView(boolean isEnableClarityNav)
    {
        RootPanel.get().add(viewport);
        viewport.setLayout(new BorderLayout());

        navigationView = new NavigationView(getSelectedEntryPoint(), isEnableClarityNav);
        viewport.add(navigationView,
            new BorderLayoutData(Style.LayoutRegion.NORTH, (float)NavigationView.DEFAULT_HEIGHT));
        mainPanel = new LayoutContainer();
        viewport.add(mainPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));
        mainPanel.setLayout(new FitLayout());

        navigationPresenter = new NavigationPresenter(navigationView, commandService,
            getGlobalEventBus());

        getCommandService().run(new UserRoleReadActiveRequest(), new GAsyncCallback<UserRoleReadResponse>()
        {
            public void onSuccess(UserRoleReadResponse response)
            {
                DefaultRequestCallback.setCachedUserRole(response.getUserRole());
                commands.privilegeReadList(getGlobalEventBus());
                SystemDateProvider.getDate(new SystemDateProvider.SystemDateCallback()
                {
                    @Override
                    public void dateReady(DateCacheEntry dateCacheEntry)
                    {
                        AbstractCommsEntryPoint.this.dateCacheEntry = dateCacheEntry;
                        navigationPresenter.bind();
                    }
                });
            }
        });
    }


    protected void addSubTitle(String title)
    {
        if (navigationView != null)
        {
            navigationView.addSubModuleTitle(title);
        }
    }

    protected HandlerManager getGlobalEventBus()
    {
        return globalEventBus;
    }

    private void configViewPortWithOutNavView()
    {
        RootPanel.get().add(viewport);
        viewport.setLayout(new FitLayout());
        mainPanel = new LayoutContainer();
        mainPanel.setLayout(new FitLayout());
        viewport.add(mainPanel);

        getCommandService().run(new UserRoleReadActiveRequest(), new GAsyncCallback<UserRoleReadResponse>()
        {
            public void onSuccess(UserRoleReadResponse response)
            {
                DefaultRequestCallback.setCachedUserRole(response.getUserRole());
                commands.privilegeReadList(getGlobalEventBus());
                SystemDateProvider.getDate(new SystemDateProvider.SystemDateCallback()
                {
                    @Override
                    public void dateReady(DateCacheEntry dateCacheEntry)
                    {
                        AbstractCommsEntryPoint.this.dateCacheEntry = dateCacheEntry;
                    }
                });
            }
        });
    }

    protected void afterModuleLoad()
    {
    }

    protected abstract ClarityEntryPoint getSelectedEntryPoint();

    protected abstract AbstractCommands createCommands();

    private void registerGenericGlobalEvents()
    {
        getGlobalEventBus().addHandler(StatusSuccessEvent.TYPE, new StatusSuccessEvent.Handler()
        {
            @Override
            public void handle(StatusSuccessEvent event)
            {
                final String message = event.getMessage();
                if (message != null)
                {
                    StatusDisplay.popupMessage("Success", message, event.getMessageType());
                }
            }
        });
        getGlobalEventBus().addHandler(StatusErrorEvent.TYPE, new StatusErrorEvent.Handler()
        {
            @Override
            public void handle(StatusErrorEvent event)
            {
                final String message = event.getMessage();
                if (message != null)
                {
                    MessageBoxHtml.MessageType type = event.getMessageType();
                    if (type.equals(MessageBoxHtml.MessageType.ERROR_STRONG))
                    {
                        StatusDisplay.popupStaticMessage("Error", message, event.getMessageType());
                    }
                    else
                        StatusDisplay.popupMessage("Error", message, event.getMessageType());
                }
            }
        });
        getGlobalEventBus().addHandler(StatusInfoEvent.TYPE, new StatusInfoEvent.Handler()
        {
            @Override
            public void handle(StatusInfoEvent event)
            {
                final String message = event.getMessage();
                if (message != null)
                {
                    StatusDisplay.popupMessage("Info", message, event.getMessageType());
                }
            }
        });

        //get privileges
        getGlobalEventBus().addHandler(PrivilegeReadListEvent.TYPE, new PrivilegeReadListEvent.Handler()
        {
            @Override
            public void handle(PrivilegeReadListEvent event)
            {
                privilegeResolver.addPrivileges(event.getPrivileges());
                Presenter presenter = createContainer();
                if (presenter != null)
                {
                    mainPanel.add(presenter.getDisplay().asWidget());
                    viewport.layout(true);
                    presenter.bind();
                    postLoad();
                }
            }
        });

    }

    protected void postLoad()
    {
    }

    protected abstract Presenter createContainer();

    public ClientI18NFactory getClientI18NFactory()
    {
        if (clientI18NFactory == null)
        {
            clientI18NFactory = new ClientI18NFactory();
            clientI18NFactory.addImplementation(LoginMessageConstants.class, GWT.create(LoginMessageConstants.class));
            clientI18NFactory.addImplementation(BaseViewConstants.class, GWT.create(BaseViewConstants.class));
            clientI18NFactory.addImplementation(AccountViewConstants.class, GWT.create(AccountViewConstants.class));
            clientI18NFactory.addImplementation(FieldNameConstants.class, GWT.create(FieldNameConstants.class));
            clientI18NFactory.addImplementation(StyleConstants.class, GWT.create(StyleConstants.class));
            clientI18NFactory.addImplementation(ValidationConstants.class, GWT.create(ValidationConstants.class));
            clientI18NFactory.addImplementation(TimeConstants.class, GWT.create(TimeConstants.class));
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

    public LayoutContainer getMainPanel()
    {
        return mainPanel;
    }

    protected void setMainPanel(LayoutContainer mainPanel)
    {
        this.mainPanel = mainPanel;
    }

    public AbstractCommands getCommands()
    {
        return commands;
    }

    public void refreshViewPort()
    {
        viewport.layout();
    }

    public NavigationView getNavigationView()
    {
        return navigationView;
    }

    public NavigationPresenter getNavigationPresenter()
    {
        return navigationPresenter;
    }

    protected Viewport getViewport()
    {
        return viewport;
    }

    protected void setDateCacheEntry(DateCacheEntry dateCacheEntry)
    {
        this.dateCacheEntry = dateCacheEntry;
    }
}
