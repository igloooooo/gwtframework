   package com.iglooit.core.base.client.view.highcharts.containers;

   import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.highcharts.wrappers.NonTimeXAxisChartWrapper;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.ClarityButton;
import com.clarity.core.base.client.widget.menu.ClarityMenu;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

   /**
 * This is a very basic chart container that has addition more action menu item
 */
public class BasicMenuChartContainer extends LayoutContainer
{
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private LayoutContainer toolbarContainer;
    private ClarityMenu menu;

    public BasicMenuChartContainer()
    {
        setLayout(new RowLayout());

        menu = new ClarityMenu();

        ClarityButton moreAction = new ClarityButton(BVC.moreActionMenu());
        moreAction.setIcon(Resource.ICONS.gear());
        moreAction.addStyleName(ClarityStyle.BUTTON_CSS);
        moreAction.addStyleName(ClarityStyle.TOOLBAR_BUTTON);
        moreAction.setMenu(menu);

        ButtonBar functionToolbar = new ButtonBar();
        //functionToolbar.addStyleName(ClarityStyle.BUTTONBAR);
        functionToolbar.setSpacing(5);
        functionToolbar.setAlignment(Style.HorizontalAlignment.CENTER);

        functionToolbar.add(moreAction);

        toolbarContainer = new LayoutContainer();
        toolbarContainer.add(functionToolbar);

        layout(true);
    }

    public ClarityMenu getMenu()
    {
        return menu;
    }

    public void addChartAndConfig(NonTimeXAxisChartWrapper chartWrapper)
    {
        add(chartWrapper);
        add(toolbarContainer);

    }
}
