package com.iglooit.core.command.client;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.base.client.util.NavigationUtil;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.dialog.GInputDialog;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.clarity.core.command.iface.LoginMessageConstants;
import com.clarity.core.lib.client.ClientConstants;
import com.clarity.core.security.iface.domain.PasswordNoLongerValidX;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.InvocationException;

public class DefaultRequestCallback<ResponseType> extends WrappingCallback<ResponseType>
{
    private static GInputDialog errorDialog;
    private static UserRole cachedUserRole;

    private static final LoginMessageConstants LMC = I18NFactoryProvider.get(LoginMessageConstants.class);

    public static void setCachedUserRole(UserRole userRole)
    {
        cachedUserRole = userRole;
    }

    public static UserRole getCachedUserRole()
    {
        return cachedUserRole;
    }

    public void onFailure(Throwable throwable)
    {
        GWT.log("DefaultRequestCallback onFailure: ", throwable);

        if (throwable instanceof InvocationException
            && throwable.getMessage() != null
            && throwable.getMessage().contains(ClientConstants.LOGIN_PAGE_MARKER))
            showErrorDialog();
        else if (throwable instanceof PasswordNoLongerValidX)
            showErrorDialog();
        else
            getInnerCallback().onFailure(throwable);
    }

    private synchronized void showErrorDialog()
    {
        if (errorDialog != null)
        {
            GWT.log("Error dialog not null, being shown twice, error", null);
            errorDialog.hide();
            errorDialog = null;
        }

        final ErrorPanel panel = new ErrorPanel();
        panel.setWidth(350);
        errorDialog = new GInputDialog(panel);
        errorDialog.addStyleName("session-timeout");
        errorDialog.addStyleName(ClarityStyle.DIALOG_NO_TITLE);
        errorDialog.setHeading("");
        errorDialog.setClosable(false);
        errorDialog.setWidth(350);
        errorDialog.setHeight(160);
        errorDialog.setModal(true);
        errorDialog.setButtons(Dialog.OK);
        errorDialog.getButtonById(Dialog.OK).setText(LMC.refreshButton());
        errorDialog.setButtonAlign(Style.HorizontalAlignment.CENTER);
        errorDialog.setHideOnButtonClick(false);
        errorDialog.getOkButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent clickEvent)
            {
                NavigationUtil.reloadPage();
            }
        });

        errorDialog.show();
    }

    public void onSuccess(ResponseType t)
    {
        getInnerCallback().onSuccess(t);
    }

    private final class ErrorPanel extends GPanel
    {
        public void doOnRender(Element element, int i)
        {
            setLayout(new RowLayout());

            RowData rowData = new RowData(1, -1, new Margins(5));
            Html imageHtml = new Html(Resource.IMAGES.logoSmall().getHTML());
            imageHtml.addStyleName(ClarityStyle.IMAGE_WRAPPER);
            add(imageHtml, rowData);
            add(new Html("<h2 class='title'>" + LMC.sessionTimeoutTitle() + "</h2>"), rowData);
            add(new Html(LMC.sessionTimeoutMessage()), rowData);

            layout();
        }

        public String getLabel()
        {
            return LMC.sessionTimeoutTitle();
        }
    }
}
