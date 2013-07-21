package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Widget;

public class DescriptionWidgetContainer extends LayoutContainer
{
    public static final Margins MY_MARGIN = new Margins(5);
    public static final int MY_HEIGHT = 30;
    private Label descLabel;

    public DescriptionWidgetContainer(Widget widget, String description)
    {
        descLabel = new Label(description);
        setHeight(MY_HEIGHT);
        setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        add(widget, new RowData(-1, -1, MY_MARGIN));
        add(descLabel, new RowData(1, -1, MY_MARGIN));
    }

    public void updateWidgetDescription(String description)
    {
        descLabel.setText(description);
        layout(true);
    }
}
