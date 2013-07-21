package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

public class IconWidgetContainer<W extends Widget> extends LayoutContainer
{
    public static final int MY_MARGIN = 5;

    private AbstractImagePrototype icon;
    private W widget;

    private RowData iconRowData = new RowData(-1, -1, new Margins(MY_MARGIN));
    private RowData widgetRowData = new RowData(1, 1, new Margins(MY_MARGIN));

    public IconWidgetContainer(W widget)
    {
        //default icon is spinner
        this(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER), widget);
    }

    public IconWidgetContainer(AbstractImagePrototype icon, W widget)
    {
        //default orientation is horizontal
        this(icon, widget, Style.Orientation.HORIZONTAL);
    }

    public IconWidgetContainer(AbstractImagePrototype icon, W widget, Style.Orientation ori)
    {
        this.icon = icon;
        this.widget = widget;

        setLayout(new RowLayout(ori));
        add(icon.createImage(), iconRowData);
        add(widget, widgetRowData);
    }

    public W getWidget()
    {
        return widget;
    }

    public void showIcon()
    {
        removeAll();
        add(icon.createImage(), iconRowData);
        add(widget, widgetRowData);
        layout(true);
    }

    public void hideIcon()
    {
        removeAll();
        add(widget, widgetRowData);
        layout(true);
    }
}
