package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.html.HeadingHtml;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.ui.Widget;

public class TitledLayoutContainer extends LayoutContainer
{
    private LayoutContainer headingContainer;

    public TitledLayoutContainer(String title, Display display)
    {
        setLayout(new BorderLayout());
        addStyleName(ClarityStyle.TITLED_CONTAINER);

        // north region - page title
        LayoutContainer north = new LayoutContainer();
        north.setLayout(new FlowLayout());

        headingContainer = new LayoutContainer();
        headingContainer.setLayout(new HBoxLayout());
        headingContainer.add(new HeadingHtml(title, HeadingHtml.HeadingType.PAGE), new HBoxLayoutData());
        north.add(headingContainer);

        BorderLayoutData northData = new BorderLayoutData(Style.LayoutRegion.NORTH, 20);
        northData.setMargins(new Margins(5));
        add(north, northData);

        // center region - widget or display
        LayoutContainer center = new LayoutContainer();
        center.setLayout(new FitLayout());
        center.add(display.asWidget());

        BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 5, 5, 5));
        add(center, centerData);
    }

    public void addWidgetToTitle(Widget widget)
    {
        headingContainer.add(widget, new HBoxLayoutData(new Margins(0, 0, 0, 10)));
    }

    public void resetHeading(String heading)
    {
        headingContainer.removeAll();
        headingContainer.add(new HeadingHtml(heading, HeadingHtml.HeadingType.PAGE), new HBoxLayoutData());
    }
}
