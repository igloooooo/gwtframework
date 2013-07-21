package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.account.client.presenter.UserRolePasswordUpdateActivePresenter;
import com.clarity.core.account.client.view.PasswordUpdateDisplay;
import com.clarity.core.account.iface.command.request.UserRoleCheckActivePrivilegesRequest;
import com.clarity.core.account.iface.command.request.UserRoleReadActiveRequest;
import com.clarity.core.account.iface.command.response.UserRoleCheckActivePrivilegesResponse;
import com.clarity.core.account.iface.command.response.UserRoleReadResponse;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.account.iface.i18n.AccountViewConstants;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.event.UserRoleCheckActivePrivilegesEvent;
import com.clarity.core.base.client.function.SubModuleDisplayAction;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.util.NavigationUtil;
import com.clarity.core.base.client.widget.misc.NavigationView;
import com.clarity.core.base.iface.command.request.GetNavigationCustomLogoRequest;
import com.clarity.core.base.iface.command.request.GetUserManagementStatusRequest;
import com.clarity.core.base.iface.command.response.GetNavigationCustomLogoResponse;
import com.clarity.core.base.iface.command.response.GetUserManagementStatusResponse;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.command.client.WrappingCallback;
import com.clarity.core.lib.client.ClientConstants;
import com.clarity.core.login.iface.command.request.ClearSessionRequest;
import com.clarity.core.login.iface.command.request.GetLoginInfoRequest;
import com.clarity.core.login.iface.command.response.ClearSessionResponse;
import com.clarity.core.login.iface.command.response.GetLoginInfoResponse;
import com.clarity.core.security.iface.access.domain.Privileges;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NavigationPresenter extends DefaultPresenter<NavigationView> implements NavigationView.IPresenter
{
    protected static final AccountViewConstants AVC = I18NFactoryProvider.get(AccountViewConstants.class);

    private HandlerManager eventBus;
    private CommandServiceClientImpl commandService;

    private UserRolePasswordUpdateActivePresenter userRolePasswordUpdateActivePresenter;
    private Set<String> userPrivilegeSet;
    private List<String> userPrivilegeList;
    private boolean passwordChangesAllowed = true;

    @Inject
    public NavigationPresenter(NavigationView display,
                               CommandServiceClientImpl commandService,
                               HandlerManager eventBus)
    {
        super(display);
        this.commandService = commandService;
        this.eventBus = eventBus;

        userPrivilegeList = new ArrayList<String>();
        userPrivilegeList.add(Privileges.EMPLOYEE_ADMIN);
        userPrivilegeList.add(Privileges.PROFILE_ADMIN);
        userPrivilegeList.add(Privileges.SYSTEM_ADMIN);

        display.setPresenter(this);
        bind();
    }

    private void logoutFromServer()
    {
        // try and remove the locale cookie.
        //Cookies.removeCookie(LocaleCookieName.NAME);

        // use a special wrapping callback; if we've already been logged out due to timeout,
        // there is no reason to log in just to log out
        commandService.run(new ClearSessionRequest(),
            new GAsyncCallback<ClearSessionResponse>()
            {
                public void onSuccess(ClearSessionResponse clearSessionResponse)
                {
                    redirectAfterLogout(Option.some(clearSessionResponse.getLogoutUrl()));
                }
            },
            new WrappingCallback<ClearSessionResponse>()
            {
                public void onFailure(Throwable throwable)
                {
                    if (throwable instanceof InvocationException
                        && throwable.getMessage() != null
                        && throwable.getMessage().contains(ClientConstants.LOGIN_PAGE_MARKER))
                        NavigationUtil.reloadPage();
                    else
                        getInnerCallback().onFailure(throwable);
                }

                public void onSuccess(ClearSessionResponse clearSessionResponse)
                {
                    getInnerCallback().onSuccess(clearSessionResponse);
                }
            }
        );
    }

    private void changePassword()
    {
        if (!passwordChangesAllowed)
            throw new AppX("In the current configuration, user passwords are not managed by thin client.");
        commandService.run(new UserRoleReadActiveRequest(), new GAsyncCallback<UserRoleReadResponse>()
        {
            public void onSuccess(UserRoleReadResponse response)
            {
                UserRole loginUser = response.getUserRole();
                if (loginUser == null)
                    throw new AppX("no login user!");
                userRolePasswordUpdateActivePresenter.setParty(loginUser);
                userRolePasswordUpdateActivePresenter.popDialog(false);
            }
        });
    }

    public void getUserLoginInfoPrivileges()
    {
        // set the logged in username
        // get privileges
        commandService.run(new GetLoginInfoRequest(), new GAsyncCallback<GetLoginInfoResponse>()
        {
            @Override
            public void onSuccess(GetLoginInfoResponse response)
            {
                UserRole userRole = response.getDomainEntity();
                getDisplay().setLoggedInUserName(userRole.getUsername());

                getDisplay().setModuleNavMap(response.getModules());
                setupPrivileges();
            }
        });
    }

    private void setupPrivileges()
    {
        getDisplay().layout();
        commandService.run(new UserRoleCheckActivePrivilegesRequest(userPrivilegeList),
            new GAsyncCallback<UserRoleCheckActivePrivilegesResponse>()
            {
                @Override
                public void onSuccess(UserRoleCheckActivePrivilegesResponse response)
                {
                    userPrivilegeSet = new HashSet<String>(response.getPrivilegeSet());
                    if (userPrivilegeSet.contains(Privileges.EMPLOYEE_ADMIN))
                        getDisplay().layout();
                    eventBus.fireEvent(new UserRoleCheckActivePrivilegesEvent(userPrivilegeSet));
                }
            });
    }

    public void addSubModule(String subModuleName, SubModuleDisplayAction action)
    {
        getDisplay().addSubModule(subModuleName, action);
    }

    private void redirectAfterLogout(Option<String> url)
    {
        if (url.isSome())
            NavigationUtil.redirectToUrl(url.value());
        else
        {
            NavigationUtil.reloadChildWindows();
            NavigationUtil.reloadPage();
        }
    }

    @Override
    public void bind()
    {
        commandService.run(new GetNavigationCustomLogoRequest(), new GAsyncCallback<GetNavigationCustomLogoResponse>()
        {
            @Override
            public void onSuccess(GetNavigationCustomLogoResponse result)
            {
                getDisplay().setCustomBrandLogo(result.getUrl(), result.getWidth());
            }
        });

        commandService.run(new GetUserManagementStatusRequest(), new GAsyncCallback<GetUserManagementStatusResponse>()
        {
            @Override
            public void onSuccess(GetUserManagementStatusResponse result)
            {
                NavigationPresenter.this.passwordChangesAllowed = result.isPasswordChangesAllowed();
                getDisplay().setPasswordChangesAllowed(result.isPasswordChangesAllowed());
                if (passwordChangesAllowed)
                {
                    userRolePasswordUpdateActivePresenter = new UserRolePasswordUpdateActivePresenter(commandService,
                        new PasswordUpdateDisplay(AVC.resetPasswordDialogTitle()), eventBus);
                    userRolePasswordUpdateActivePresenter.bind();
                }
            }
        });

        getUserLoginInfoPrivileges();
    }


    public void onLogout()
    {
        logoutFromServer();
    }

    public void onChangePassword()
    {
        changePassword();
    }

    public void onShowDashboard()
    {
        getDisplay().showDashboardDialog();
    }
    public void removeAll()
    {
        getDisplay().removeSubModules();
    }
}
