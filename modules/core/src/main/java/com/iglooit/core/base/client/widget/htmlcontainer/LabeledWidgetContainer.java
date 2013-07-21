package com.iglooit.core.base.client.widget.htmlcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

public class LabeledWidgetContainer extends LayoutContainer
{
    public static final int MY_HEIGHT = 35;
    public static final int MY_MARGIN = 5;
    public static final String MY_STYLE = "labeled-widget-container";
    private Html label;
    private Html html;
    private Margins labelMargins;
    private Margins widgetMargins;

    public LabeledWidgetContainer(String labelText, Widget widget)
    {
        this(labelText, widget, false);
    }

    public LabeledWidgetContainer(String labelText, Widget widget, boolean isExpand)
    {
        this(labelText, widget, isExpand, MY_STYLE);
    }

    public LabeledWidgetContainer(String labelText, Widget widget,
                                  boolean isExpand, String style)
    {
        this(labelText, widget, isExpand, style, null);
    }

    public LabeledWidgetContainer(String labelText, Widget widget,
                                  boolean isExpand, String style, AbstractImagePrototype icon)
    {
        this(labelText, widget, isExpand, style, icon, new Margins(MY_MARGIN), new Margins(MY_MARGIN));

    }

    public LabeledWidgetContainer(String labelText, Widget widget,
                                  boolean isExpand, String style, AbstractImagePrototype icon,
                                  Margins labelMargins, Margins widgetMargins)
    {
        setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setHeight(MY_HEIGHT);
        addStyleName(StringUtil.emptyStringIfNull(style));
        this.labelMargins = labelMargins;
        this.widgetMargins = widgetMargins;
        label = new Html(labelText);
        label.setTagName("div");
        label.addStyleName(ClarityStyle.FLOAT_LEFT);
        label.addStyleName(ClarityStyle.TEXT_LABEL);
        if (icon == null)
            html = new Html("<span></span>");
        else
            html = new Html(icon.getHTML());
        add(html, new RowData(-1, -1, this.labelMargins));
        
        if (isExpand)
        {
            label.setAutoWidth(true);
            add(label, new RowData(1, -1, this.labelMargins));
        }
        else
        {
            add(label, new RowData(-1, -1, this.labelMargins));
        }

        if (widget != null)
        {
            widget.addStyleName("labeled-widget");
            add(widget, new RowData(-1, -1, this.widgetMargins));

        }
    }

    public void setLabelWidth(int lw)
    {
        label.setWidth(lw);
    }

    public void setLabelText(String text)
    {
        label.setHtml(text);
    }

    public void setMyHeight(int height)
    {
        setHeight(height);
    }

    public void reLayout(Widget w, int lableW, int widgetW)
    {
        removeAll();
        add(label, new RowData(lableW, -1, this.labelMargins));
        w.addStyleName("labeled-widget");
        add(w, new RowData(widgetW, -1, this.widgetMargins));
        layout();
    }
}
