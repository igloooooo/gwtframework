package com.iglooit.core.base.client.widget.misc;

import com.clarity.core.base.client.function.SubModuleDisplayAction;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.navigator.SimpleNavigator;
import com.clarity.core.base.client.util.NavigationUtil;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.menu.NavigationMenuItem;
import com.clarity.core.base.iface.domain.ClarityEntryPoint;
import com.clarity.core.base.iface.domain.NavigationBarItem;
import com.clarity.core.base.iface.domain.NavigationBarItem.NavigationModuleCategory;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class NavigationView extends LayoutContainer implements Display
{
    public static final int DEFAULT_HEIGHT = 31;

    private static final int UNIT_ROW_MARGIN_TOP = 10;
    private static final int UNIT_ROW_MARGIN_BOTTOM = 10;
    private static final int UNIT_ROW_MARGIN_LEFT = 5;
    private static final int UNIT_ROW_MARGIN_RIGHT = 5;

    private static final int UNIT_COLUMN_WIDTH = 210;
    private static final int UNIT_INNER_ROW_HEIGHT = 55;
    private static final int UNIT_ROW_HEIGHT = UNIT_INNER_ROW_HEIGHT + 15 + UNIT_ROW_MARGIN_TOP +
        UNIT_ROW_MARGIN_BOTTOM;

    /**
     * Additional height for the outer-most dialog. This figure composes of: <ul> <li>style 'global-menu-panel' (padding
     * = 5) <li>the height of module category title (40) <li>the top and bottom margin (10) </ul>
     */
    private static final int DIALOG_HEIGHT_EXTRA = 60;

    private static final int MIN_ITEM_PER_COLUMN = 4;
    private static final int TOOLBOX_WIDTH = 180;
    private static final int TOOLBOX_UNIT_HEIGHT = 45;

    private ClarityEntryPoint currentEntryPoint;
    private NavigationTopBar navigationTopBar;
    private Map<String, NavigationBarItem> moduleNavMap;
    private HandlerRegistration dashboardDisplayRegistration;
    private Dialog dashboardDialog;
    private boolean navBarVisible = false;

    private IPresenter presenter;

    public void setSubModuleActivated(String subModuleName, boolean activated)
    {
        navigationTopBar.setSubModuleActivated(subModuleName, activated);
    }

    public void setPasswordChangesAllowed(boolean passwordChangesAllowed)
    {
        navigationTopBar.setPasswordChangesAllowed(passwordChangesAllowed);
    }

    public void removeSubModules()
    {
        navigationTopBar.removeSubModules();
    }

    public interface IPresenter
    {
        void onLogout();

        void onChangePassword();

        void onShowDashboard();

        void addSubModule(String moduleName, SubModuleDisplayAction action);
    }

    public NavigationView(ClarityEntryPoint selected)
    {
        this(selected, true);
    }

    @Inject
    public NavigationView(@Named("selectedEP") ClarityEntryPoint selected, @Named("isActive") boolean active)
    {
        this.currentEntryPoint = selected;
        navigationTopBar = new NavigationTopBar(selected, active);

        navigationTopBar.addLogoutListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                presenter.onLogout();
            }
        });

        navigationTopBar.addChangePasswordListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                presenter.onChangePassword();
            }
        });

        navigationTopBar.addLogoSelectionListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                presenter.onShowDashboard();
            }
        });

        navigationTopBar.addHelpButtonListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                // TFT-3031	Inbox module will be different from others
                if (currentEntryPoint.equals(ClarityEntryPoint.INBOX))
                {
                    SimpleNavigator navigator = new SimpleNavigator();
                    String relativePath = ClarityEntryPoint.HELP.getJsp();
                    navigator.openNew(relativePath, "");
                }
                else
                {
                    Window.open(GWT.getHostPageBaseURL() +
                        "usermanual?module=" + getCurrentEntryPoint(), "Help", "");
                }

            }
        });

        navigationTopBar.addMainHelpButtonListener(new SelectionListener()
        {
            @Override
            public void componentSelected(ComponentEvent ce)
            {
                Window.open(GWT.getHostPageBaseURL() + ClarityEntryPoint.HELP.getJsp(), "Help", "");
            }
        });

        setLayout(new FitLayout());
        setHeight(DEFAULT_HEIGHT);
        add(navigationTopBar);

    }

    public void setPresenter(IPresenter presenter)
    {
        this.presenter = presenter;
    }

    public void addSubModule(String name, SubModuleDisplayAction action)
    {
        navigationTopBar.addSubModule(name, null, action);
    }

    public void addSubModule(String name, AbstractImagePrototype icon, SubModuleDisplayAction action)
    {
        navigationTopBar.addSubModule(name, icon,  action);
    }

    private List<NavigationBarItem> getModulesByCategory(NavigationModuleCategory category)
    {
        List<NavigationBarItem> sortedModules = new LinkedList<NavigationBarItem>();

        for (NavigationBarItem item : moduleNavMap.values())
        {
            if (item.getColumnPreference() == -1)
                continue;

            if (item.getColumnPreference() == category.getColumnPreference())
            {
                sortedModules.add(item);
            }
        }

        Collections.sort(sortedModules, new Comparator<NavigationBarItem>()
        {
            @Override
            public int compare(NavigationBarItem o1, NavigationBarItem o2)
            {
                return o1.getOrderPriority() - o2.getOrderPriority();
            }
        });

        return sortedModules;
    }

    private Map<NavigationModuleCategory, List<NavigationBarItem>> getNonToolboxModuleCategories()
    {
        Map<NavigationModuleCategory, List<NavigationBarItem>> resultMap =
            new LinkedHashMap<NavigationModuleCategory, List<NavigationBarItem>>();

        List<NavigationModuleCategory> categories = NavigationModuleCategory.getNonToolboxValues();
        for (NavigationModuleCategory category : categories)
        {
            List<NavigationBarItem> sortedModules = getModulesByCategory(category);
            resultMap.put(category, sortedModules);
        }

        return resultMap;
    }

    private Event.NativePreviewHandler buildDashboardHideRegistration()
    {
        return new Event.NativePreviewHandler()
        {
            public void onPreviewNativeEvent(Event.NativePreviewEvent event)
            {
                if (Event.as(event.getNativeEvent()).getTypeInt() == Event.ONCLICK)
                {
                    Element element = (Element)event.getSource();
                    if (element == null || !element.getId().equals(navigationTopBar.getElementId()))
                    {
                        hideDashboardDialog();
                    }
                }
            }
        };
    }

    private LayoutContainer renderToolboxPanel(List<NavigationBarItem> toolboxModules)
    {
        LayoutContainer toolBox = new LayoutContainer();
        toolBox.setLayout(new RowLayout());
        toolBox.setBorders(true);
        toolBox.addStyleName("toolbox-menu-panel");

        Text toolboxTitle = new Text(NavigationModuleCategory.TOOLBOX.getDefaultTitle()); // TODO [26 April 2012] I18N?
        toolboxTitle.addStyleName(ClarityStyle.TEXT_TITLE);
        toolBox.add(toolboxTitle, new RowData(1, -1, new Margins(10, 10, 0, 15)));

        fillToolboxModules(toolboxModules, toolBox);
        return toolBox;
    }

    private void fillToolboxModules(List<NavigationBarItem> toolboxModules, LayoutContainer targetPanel)
    {
        String href = Window.Location.getHref();

        for (NavigationBarItem item : toolboxModules)
        {
            String target = !href.contains(item.getModuleURL()) ? "target=\"_blank\"" : "";

            Html moduleLink = new NavigationMenuItem(
                NavigationUtil.buildRedirectURL(item.getModuleURL()) + "\"" + target, item.getTitle());
            targetPanel.add(moduleLink, new RowData(1, -1, new Margins(10)));
        }
    }

    private LayoutContainer renderModuleCategoryPanel(NavigationModuleCategory category, float perColumnWidth,
                                                      List<NavigationBarItem> modules)
    {

        LayoutContainer moduleCategoryContainer = new LayoutContainer(new RowLayout());
        moduleCategoryContainer.addStyleName("modules-menu-panel");

        Text ossTitle = new Text(category.getDefaultTitle()); // TODO [26 April 2012] I18N?
        ossTitle.addStyleName(ClarityStyle.TEXT_TITLE);
        moduleCategoryContainer.add(ossTitle, new RowData(1, -1, new Margins(10, 10, 0, 15)));

        fillModuleCategory(perColumnWidth, modules, moduleCategoryContainer);
        return moduleCategoryContainer;
    }

    private LayoutContainer prepareModuleColumnContainer(
        LayoutContainer modulesPredicateContainer, float columnWidthPercent)
    {
        LayoutContainer columnContainer = new LayoutContainer(new RowLayout());
        modulesPredicateContainer.add(columnContainer, new ColumnData(columnWidthPercent));
        return columnContainer;
    }

    private void fillModuleCategory(float columnWidthPercent, List<NavigationBarItem> modules,
                                    LayoutContainer modulesContainer)
    {
        LayoutContainer modulesPredicateContainer = new LayoutContainer(new ColumnLayout());
        modulesContainer.add(modulesPredicateContainer, new RowData(-1, -1, ClarityStyle.DEFAULT_ROW_MARGINS));

        int maxRowPerColumn = (int)Math.ceil((double)modules.size() / 2);
        String href = Window.Location.getHref();

        int i = 0;

        LayoutContainer columnContainer = prepareModuleColumnContainer(modulesPredicateContainer, columnWidthPercent);
        for (NavigationBarItem item : modules)
        {
            String target = "";
            if (!href.contains(item.getModuleURL()))
            {
                target = "target=\"_blank\"";
            }

            Html moduleLink = new NavigationMenuItem(
                NavigationUtil.buildRedirectURL(item.getModuleURL()) + "\"" + target,
                item.getTitle(), item.getDescription(), UNIT_INNER_ROW_HEIGHT);
            columnContainer.add(moduleLink, new RowData(1, -1, new Margins(UNIT_ROW_MARGIN_TOP,
                UNIT_ROW_MARGIN_RIGHT, UNIT_ROW_MARGIN_BOTTOM, UNIT_ROW_MARGIN_LEFT)));

            i++;
            if (i == maxRowPerColumn)
            {
                columnContainer = prepareModuleColumnContainer(modulesPredicateContainer, columnWidthPercent);
                i = 0;
            }
        }
    }

    private int calculateMaxRowPerColumn(List<NavigationBarItem> modules)
    {
        int maxRowPerColumn = modules.size();
        if (modules.size() > MIN_ITEM_PER_COLUMN)
        {
            maxRowPerColumn = (int)Math.ceil((double)modules.size() / 2);
        }
        return maxRowPerColumn;
    }

    private int calculateMaxRowPerColumn(
        Map<NavigationModuleCategory, List<NavigationBarItem>> moduleCategoryMap)
    {
        int grandMaxRowPerColumn = -1;
        for (Map.Entry<NavigationModuleCategory, List<NavigationBarItem>> entry : moduleCategoryMap.entrySet())
        {
            List<NavigationBarItem> modules = entry.getValue();

            int maxRowPerColumn = calculateMaxRowPerColumn(modules);
            if (maxRowPerColumn > grandMaxRowPerColumn)
            {
                grandMaxRowPerColumn = maxRowPerColumn;
            }
        }
        return grandMaxRowPerColumn;
    }

    private int calculateMaxColumnCount(List<NavigationBarItem> modules)
    {
        int columnCount = 0;
        if (modules.size() > MIN_ITEM_PER_COLUMN)
        {
            columnCount = 2;
        }
        else if (!modules.isEmpty())
        {
            columnCount = 1;
        }
        else
        {
            // no column is added
        }
        return columnCount;
    }

    private int calculateMaxColumnCount(Map<NavigationModuleCategory, List<NavigationBarItem>> moduleCategoryMap)
    {
        int columnCount = 0;
        for (Map.Entry<NavigationModuleCategory, List<NavigationBarItem>> entry : moduleCategoryMap.entrySet())
        {
            columnCount += calculateMaxColumnCount(entry.getValue());
        }
        return columnCount;
    }

    private void renderDashboardDialog()
    {

        //Toggle the nav button
        navigationTopBar.addNavMenuButtonStyle("x-btn-active");

        List<NavigationBarItem> toolboxModules = getModulesByCategory(NavigationModuleCategory.TOOLBOX);
        Map<NavigationModuleCategory, List<NavigationBarItem>> moduleCategoryMap = getNonToolboxModuleCategories();

        int maxColumn = calculateMaxColumnCount(moduleCategoryMap);
        int maxRowPerColumn = calculateMaxRowPerColumn(moduleCategoryMap);
        int baseHeightOfRows = maxRowPerColumn * UNIT_ROW_HEIGHT;
        if (toolboxModules != null)
        {
            // Category rows are approx. twice the height of toolBox rows
            if (toolboxModules.size() * TOOLBOX_UNIT_HEIGHT > baseHeightOfRows)
                baseHeightOfRows = toolboxModules.size() * TOOLBOX_UNIT_HEIGHT;
        }

        renderDashboardPopup(maxColumn, baseHeightOfRows);

        // main menu panel
        LayoutContainer menuPanel = new LayoutContainer(new BorderLayout());
        menuPanel.setStyleAttribute("position", "relative"); // fix layout problem with border layout in container
        dashboardDialog.add(menuPanel);

        // (left side) toolbox panel
        LayoutContainer toolBox = renderToolboxPanel(toolboxModules);
        BorderLayoutData westData = new BorderLayoutData(Style.LayoutRegion.WEST, TOOLBOX_WIDTH);
        westData.setMargins(new Margins(0, 5, 14, 12));
        menuPanel.add(toolBox, westData);

        // (right side) module categories (OSS, IM, MP...)
        LayoutContainer categoryHolderContainer = new LayoutContainer(new ColumnLayout());
        for (Map.Entry<NavigationModuleCategory, List<NavigationBarItem>> entry : moduleCategoryMap.entrySet())
        {
            List<NavigationBarItem> modules = entry.getValue();
            if (!modules.isEmpty())
            {
                int maxColumnPerCategory = calculateMaxColumnCount(modules);
                float perColumnWidth = 1f / maxColumnPerCategory;
                LayoutContainer moduleCategoryContainer = renderModuleCategoryPanel(
                    entry.getKey(), perColumnWidth, modules);

                float categoryHolderColumnPercent = (float)maxColumnPerCategory / maxColumn;
                categoryHolderContainer.add(moduleCategoryContainer, new ColumnData(categoryHolderColumnPercent));
            }
        }
        BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 14, 14, 0));
        menuPanel.add(categoryHolderContainer, centerData);

        dashboardDialog.show();
        dashboardDisplayRegistration = Event.addNativePreviewHandler(buildDashboardHideRegistration());
    }

    private void renderDashboardPopup(int maxColumn, int baseHeightOfRows)
    {

        dashboardDialog = new Dialog();
        dashboardDialog.addStyleName("global-menu-panel");
        dashboardDialog.addStyleName(ClarityStyle.DIALOG_NO_TITLE);
        dashboardDialog.setLayout(new FitLayout());
        dashboardDialog.setHeading("&nbsp"); // need to set a space entity here for IE to display correctly
        dashboardDialog.setWidth(TOOLBOX_WIDTH + (maxColumn * UNIT_COLUMN_WIDTH) + 10);
        dashboardDialog.setHeight(baseHeightOfRows + DIALOG_HEIGHT_EXTRA);
        dashboardDialog.setResizable(false);
        dashboardDialog.setDraggable(false);
        dashboardDialog.setClosable(false);
        dashboardDialog.setHideOnButtonClick(true);
        dashboardDialog.setButtons("");
        dashboardDialog.setPosition(0, DEFAULT_HEIGHT);
        dashboardDialog.setModal(false);
    }

    public void showDashboardDialog()
    {
        if (navBarVisible)
        {
            hideDashboardDialog();
            return;
        }
        navBarVisible = true;

        renderDashboardDialog();
    }

    private void hideDashboardDialog()
    {
        navBarVisible = false;
        navigationTopBar.removeNavMenuButtonStyle("x-btn-active");
        dashboardDialog.hide();
        dashboardDisplayRegistration.removeHandler();
    }

    public void setLoggedInUserName(String loggedInUserName)
    {
        navigationTopBar.setLoggedInUserName(loggedInUserName);
    }

    public void setCustomBrandLogo(String logoUrl, String logoWidth)
    {
        navigationTopBar.setCustomBrandLogo(logoUrl, logoWidth);
    }

    public void addHelpButtonListener(SelectionListener<ButtonEvent> listener)
    {
        navigationTopBar.addHelpButtonListener(listener);
    }

    public void addMainHelpButtonListener(SelectionListener listener)
    {
        navigationTopBar.addMainHelpButtonListener(listener);
    }

    public void reloadPage()
    {
        NavigationUtil.reloadPage();
    }

    public void reloadChildWindows()
    {
        NavigationUtil.reloadChildWindows();
    }

    public void redirectToUrl(String url)
    {
        Window.Location.assign(url);
    }

    public ClarityEntryPoint getCurrentEntryPoint()
    {
        return currentEntryPoint;
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

    public Map<String, NavigationBarItem> getModuleNavMap()
    {
        return moduleNavMap;
    }

    public void setModuleNavMap(Map<String, NavigationBarItem> moduleNavMap)
    {
        this.moduleNavMap = moduleNavMap;
    }

    public NavigationTopBar getNavigationTopBar()
    {
        return navigationTopBar;
    }

    public void addSubModuleTitle(String title)
    {
        navigationTopBar.addSubModuleTitle(title);
    }

}
