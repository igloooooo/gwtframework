package com.iglooit.core.base.client.widget.misc;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.function.SubModuleDisplayAction;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.layoutcontainer.FloatContainer;
import com.clarity.core.base.iface.domain.ClarityEntryPoint;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

public class NavigationTopBar extends FloatContainer implements Display
{
    private static final BaseViewConstants BC = I18NFactoryProvider.get(BaseViewConstants.class);

    public static final int CUSTOM_LOGO_DEFAULT_WIDTH = 110;
    public static final int CUSTOM_LOGO_MAX_WIDTH = 250;
    private String loggedInUserName;

    private MenuItem changePasswordMenuItem, logoutMenuItem;
    private MenuItem mainHelpMenuItem;
    private Button userButton;
    private Button helpButton;
    public static final int DEFAULT_HEIGHT = 31;
    private Button appNavContainer;
    private Html moduleTitle;
    private Html subModuleTitle;
    private ToolBar subModuleToolBar;
    private ClarityEntryPoint selectedModule;
    private Html customBrandLogoContainer;

    private Map<String, ToggleButton> buttons = new HashMap<String, ToggleButton>();

    public NavigationTopBar(ClarityEntryPoint selectedModule, boolean isClarityButtonActive)
    {
        this.selectedModule = selectedModule;
        this.loggedInUserName = "";

        addStyleName("app-top-bar");
        setHeight(DEFAULT_HEIGHT);

        if (isClarityButtonActive)
        {
            appNavContainer = new Button();
            appNavContainer.addStyleName("clarity-nav-button");
            appNavContainer.addStyleName(ClarityStyle.BUTTON_NOSTYLE);
            appNavContainer.setWidth(72);
            appNavContainer.setHeight(30);
            add(appNavContainer, LEFT, new Margins(0));
        }
        else
        {
            Html clarityLogo = new Html("<div class='clarity-nav-button'><h1>Clarity</h1></div>");
            clarityLogo.setHeight(30);
            clarityLogo.setWidth(72);
            add(clarityLogo, LEFT, new Margins(0));
        }

        // module title
        moduleTitle = new Html(selectedModule.getDisplayName());
        moduleTitle.setTitle("You're currently in: " + selectedModule.getDisplayName() + " - " +
            selectedModule.getDescription());
        moduleTitle.setHeight(30);
        add(moduleTitle, LEFT, new Margins(0));
        moduleTitle.addStyleName("module-breadcrumb");
        moduleTitle.addStyleName(ClarityStyle.TEXT_MEDIUM);
        moduleTitle.setStyleAttribute("padding", "0 10px");

        // sub module title
        subModuleTitle = new Html("");
        subModuleTitle.setHeight(30);
        add(subModuleTitle, LEFT, new Margins(0));
        subModuleTitle.addStyleName("module-breadcrumb sub-module");
        subModuleTitle.addStyleName(ClarityStyle.TEXT_MEDIUM);
        subModuleTitle.setStyleAttribute("padding", "0 10px");
        subModuleTitle.setVisible(false);

        // Toolbar for sub module navigation
        subModuleToolBar = new ToolBar();
        subModuleToolBar.setSpacing(5);
        subModuleToolBar.addStyleName("sub-module-toolbar");
        add(subModuleToolBar, LEFT, new Margins(2, 0, 0, 2));

        ToolBar toolBar = new ToolBar();
        toolBar.addStyleName("user-nav-container");
        toolBar.setAlignment(Style.HorizontalAlignment.RIGHT);

        changePasswordMenuItem = new MenuItem(BC.changePassword());
        logoutMenuItem = new MenuItem(BC.logout());

        userButton = new Button(loggedInUserName);
        userButton.setIcon(Resource.ICONS_SIMPLE.user());
        Menu menu = new Menu();
        menu.add(changePasswordMenuItem);
        menu.add(new SeparatorMenuItem());
        menu.add(logoutMenuItem);
        userButton.setMenu(menu);
        userButton.setEnabled(isClarityButtonActive);
        toolBar.add(userButton);

        helpButton = new Button();
        helpButton.setIcon(Resource.ICONS_SIMPLE.question());
        helpButton.setVisible(!selectedModule.isHelp());
        toolBar.add(helpButton);
        menu = new Menu();
        mainHelpMenuItem = new MenuItem(BC.help());

        menu.add(mainHelpMenuItem);

        // need to set a width here so it renders on the same line on chrome when using FloatLayout
        if (GXT.isChrome)
            toolBar.setWidth(150);

        add(toolBar, RIGHT, new Margins(2, 5, 0, 5));
    }

    public void setLoggedInUserName(String loggedInUserName)
    {
        this.loggedInUserName = loggedInUserName;
        userButton.setText(loggedInUserName);
    }

    public void setCustomBrandLogo(String url, String width)
    {
        if (!Util.isEmptyString(url))
        {
            if (customBrandLogoContainer == null)
            {
                customBrandLogoContainer = new Html();
                customBrandLogoContainer.addStyleName("custom-brand-logo-container");
                // insert after clarity button, module title, sub-module title, sub-module toolbar
                insert(customBrandLogoContainer, RIGHT, new Margins(0), 4);
            }
            int defaultWidth = CUSTOM_LOGO_DEFAULT_WIDTH;
            if (Util.isInteger(width))
            {
                // set a maximum width to reduce the risk of the logo affecting the whole nav bar layout
                if (Integer.valueOf(width) < CUSTOM_LOGO_MAX_WIDTH)
                    defaultWidth = Integer.valueOf(width);
            }

            AbstractImagePrototype logo = IconHelper.createPath(url, defaultWidth, 30);
            customBrandLogoContainer.setHtml(logo.getHTML());
            layout(true);
        }
    }

    public void addChangePasswordListener(SelectionListener listener)
    {
        changePasswordMenuItem.addSelectionListener(listener);
    }

    public void addLogoutListener(SelectionListener listener)
    {
        logoutMenuItem.addSelectionListener(listener);
    }

    public void addHelpButtonListener(SelectionListener<ButtonEvent> listener)
    {
        helpButton.addSelectionListener(listener);
    }

    public void addMainHelpButtonListener(SelectionListener listener)
    {
        mainHelpMenuItem.addSelectionListener(listener);
    }

    public void addNavMenuButtonStyle(String styleName)
    {
        if (appNavContainer != null)
            appNavContainer.addStyleName(styleName);
    }

    public void removeNavMenuButtonStyle(String styleName)
    {
        if (appNavContainer != null)
            appNavContainer.removeStyleName(styleName);
    }

    public void addSubModule(String subModuleName, AbstractImagePrototype icon, final SubModuleDisplayAction action)
    {
        final ToggleButton btn = new ToggleButton(subModuleName);
        btn.setToggleGroup("sub-module-group");
        btn.addStyleName(ClarityStyle.BUTTON_MIN_STYLE);
        btn.setAllowDepress(false);

        if (icon != null)
            btn.setIcon(icon);
        btn.addListener(Events.Toggle, new Listener<BaseEvent>()
        {
            @Override
            public void handleEvent(BaseEvent be)
            {
                if (((ToggleButton)((ButtonEvent)be).getButton()).isPressed())
                    action.displayModule();
            }
        });
        subModuleToolBar.add(btn);
        buttons.put(subModuleName, btn);
    }

    public void removeSubModules()
    {
        subModuleToolBar.removeAll();
        buttons.clear();
        layout(true);
    }

    public void setSubModuleActivated(String subModuleName, boolean activated)
    {
        if (buttons.containsKey(subModuleName))
            buttons.get(subModuleName).toggle(activated);
    }

    public void addSubModuleTitle(String title)
    {
        subModuleTitle.setHtml(title);
        subModuleTitle.setVisible(true);
    }

    public String getElementId()
    {
        return getElement().getId();
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }

    @Override
    public String getLabel()
    {
        return "Navigation View";
    }

    public void addLogoSelectionListener(SelectionListener<ButtonEvent> listener)
    {
        if (appNavContainer != null)
            appNavContainer.addSelectionListener(listener);
    }

    public void redirectToUrl(String url)
    {
        Window.Location.assign(url);
    }

    public void reloadPage()
    {
        Window.Location.reload();
    }

    public ClarityEntryPoint getSelectedModule()
    {
        return selectedModule;
    }

    public void setPasswordChangesAllowed(boolean passwordChangesAllowed)
    {
        Menu menu = new Menu();
        if (passwordChangesAllowed)
        {
            menu.add(changePasswordMenuItem);
            menu.add(new SeparatorMenuItem());
        }
        menu.add(logoutMenuItem);
        userButton.setMenu(menu);
    }
}
