package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.htmlcontainer.ActionLinkHtml;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.List;

public class CRUDActionListDisplay extends GPanel implements Display
{
    private ActionItemsDisplay actionItemsDisplay;
    private ContentPanel panel;
    private StatusDisplay actionStatusDisplay = new StatusDisplay();

    private static final RowData ACTION_ITEMS_ROWDATA = new RowData(0.95, -1, new Margins(10, 10, 0, 15));

    public CRUDActionListDisplay()
    {
        panel = new ContentPanel();
        panel.setFrame(true);
        panel.setHeading("...");
        panel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        panel.setStyleName("crud-actionlist-panel");
        panel.setScrollMode(Style.Scroll.AUTOY);

        setLayout(new FitLayout());
        add(panel);
    }

    public void setCRUDDisplay(CRUDDisplay crudDisplay)
    {
        panel.add(crudDisplay, new RowData(-1, 1, new Margins(0)));
    }

    protected List<Widget> getActionItemsPanelWidgets()
    {
        return Arrays.asList((Widget)actionStatusDisplay, actionItemsDisplay);
    }

    public void setActionItemDisplayTitle(String title)
    {
        if (actionItemsDisplay != null)
            throw new AppX("action items title shouldn't be set twice");

        actionItemsDisplay = new ActionItemsDisplay(title);
        LayoutContainer wrapper = new LayoutContainer();
        wrapper.setLayout(new RowLayout(Style.Orientation.VERTICAL));
        wrapper.setStyleName("sidebar");
        wrapper.setHeight(560);

        for (Widget actionItemsPanelWidget : getActionItemsPanelWidgets())
            wrapper.add(actionItemsPanelWidget, ACTION_ITEMS_ROWDATA);

        panel.add(wrapper, new RowData(0.95, 1));
    }

    public void addActionItem(ActionLinkHtml link, String descriptionText)
    {
        actionItemsDisplay.add(link, descriptionText);
    }

    public void addActionItem(ActionLinkHtml linkHtml, Text description)
    {
        actionItemsDisplay.add(linkHtml, description);
    }

    public void removeAllActionItems()
    {
        actionItemsDisplay.removeAll();
    }

    public NonSerOpt<ActionItemsDisplay> getActionItemsDisplay()
    {
        return NonSerOpt.option(actionItemsDisplay);
    }

    public StatusDisplay getActionStatusDisplay()
    {
        return actionStatusDisplay;
    }

    public void setPanelHeading(String string)
    {
        panel.setHeading(string);
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }

    @Override
    public void doOnRender(Element element, int i)
    {
        layout();
    }

    @Override
    public String getLabel()
    {
        return "";
    }
}
