package com.iglooit.core.base.client.widget.tabpanel;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Frame;

import java.util.HashMap;
import java.util.Map;


public class VerticalTabPanel extends LayoutContainer
{
    private BorderLayout borderLayout;
    private LayoutContainer west;
    private LayoutContainer center;
    private CardLayout cardLayout;
    private Map<String, Button> buttonMap;

    public VerticalTabPanel()
    {
        buttonMap = new HashMap<String, Button>();
        borderLayout = new BorderLayout();
        setLayout(borderLayout);

        west = new LayoutContainer();
        BorderLayoutData westData = new BorderLayoutData(Style.LayoutRegion.WEST, 100);
        westData.setSplit(false);
        westData.setCollapsible(false);
        westData.setMargins(new Margins(0, 5, 0, 0));
        add(west, westData);

        west.setLayout(new RowLayout(Style.Orientation.VERTICAL));

        center = new LayoutContainer();
        BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0));
        add(center, centerData);

        cardLayout = new CardLayout();
        center.setLayout(cardLayout);
    }

    public void addItem(String title, String url)
    {
        Button itemTitle = new Button(title);
        west.add(itemTitle, new RowData(1, -1, new Margins(4)));
        final LayoutContainer c = new LayoutContainer(new FitLayout());
        Frame frame = new Frame(url);
        c.add(frame.asWidget());
        center.add(c);

        itemTitle.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                cardLayout.setActiveItem(c);
            }
        });
        buttonMap.put(title, itemTitle);
//        itemTitle.addListener(Events.CellClick, new Listener<BaseEvent>()
//        {
//            @Override
//            public void handleEvent(BaseEvent be)
//            {
//                cardLayout.setActiveItem(c);
//            }
//        });
    }

    public Button getItem(String title)
    {
        return buttonMap.get(title);
    }
}
