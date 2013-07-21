package com.iglooit.core.base.client.presenter;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.account.client.presenter.UserRolePasswordUpdateActivePresenter;
import com.clarity.core.account.client.view.PasswordUpdateDisplay;
import com.clarity.core.account.iface.command.request.UserRoleReadActiveRequest;
import com.clarity.core.account.iface.command.response.UserRoleReadResponse;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.account.iface.i18n.AccountViewConstants;
import com.clarity.core.base.client.controller.GAsyncCallback;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.widget.misc.NavigationTopBar;
import com.clarity.core.base.iface.domain.ClarityEntryPoint;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.core.command.client.WrappingCallback;
import com.clarity.core.lib.client.ClientConstants;
import com.clarity.core.login.iface.command.request.ClearSessionRequest;
import com.clarity.core.login.iface.command.request.GetLoginInfoRequest;
import com.clarity.core.login.iface.command.response.ClearSessionResponse;
import com.clarity.core.login.iface.command.response.GetLoginInfoResponse;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.InvocationException;

public class NavigationTopBarPresenter extends DefaultPresenter<NavigationTopBar>
{
    protected static final AccountViewConstants AVC = I18NFactoryProvider.get(AccountViewConstants.class);

    private CommandServiceClientImpl commandService;

    private UserRolePasswordUpdateActivePresenter userRolePasswordUpdateActivePresenter;

    public NavigationTopBarPresenter(NavigationTopBar display,
                                     CommandServiceClientImpl commandService,
                                     HandlerManager eventBus)
    {
        super(display);
        this.commandService = commandService;

        userRolePasswordUpdateActivePresenter = new UserRolePasswordUpdateActivePresenter(commandService,
            new PasswordUpdateDisplay(AVC.resetPasswordDialogTitle()), eventBus);
    }

    @Override
    public void bind()
    {
        userRolePasswordUpdateActivePresenter.bind();
        getUserLoginInfoPrivileges();
        addLogoutListener();
        addChangePasswordListener();
        addHelpButtonListener();
        addMainHelpButtonListener();
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
                        redirectAfterLogout(Option.<String>none());
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

    public static native void reloadChildWindows()/*-{
      $wnd.reloadChildWindows();
    }-*/;

    private void redirectAfterLogout(Option<String> url)
    {
        if (url.isSome())
            getDisplay().redirectToUrl(url.value());
        else
        {
            reloadChildWindows();
            getDisplay().reloadPage();
        }
    }

    public void getUserLoginInfoPrivileges()
    {
        commandService.run(new GetLoginInfoRequest(), new GAsyncCallback<GetLoginInfoResponse>()
        {
            @Override
            public void onSuccess(GetLoginInfoResponse response)
            {
                UserRole userRole = response.getDomainEntity();
                getDisplay().setLoggedInUserName(userRole.getUsername());
            }
        });
    }

    private void addLogoutListener()
    {
        getDisplay().addLogoutListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                logoutFromServer();
            }
        });
    }

    private void addChangePasswordListener()
    {
        getDisplay().addChangePasswordListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                changePassword();
            }
        });
    }

    private void addHelpButtonListener()
    {
        getDisplay().addHelpButtonListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                Window.open(GWT.getHostPageBaseURL() +
                    "usermanual?module=" + getDisplay().getSelectedModule(), "Help", "");
            }
        }
        );
    }

    private void addMainHelpButtonListener()
    {
        getDisplay().addMainHelpButtonListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                Window.open(GWT.getHostPageBaseURL() + ClarityEntryPoint.HELP.getJsp(), "Help", "");
            }
        }
        );
    }
}
