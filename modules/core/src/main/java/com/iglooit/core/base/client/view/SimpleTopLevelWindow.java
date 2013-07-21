package com.iglooit.core.base.client.view;

import com.clarity.core.base.client.mvp.Presenter;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class SimpleTopLevelWindow extends LayoutContainer implements TopLevelWindow
{
    private TabPanel tabPanel;

    public SimpleTopLevelWindow()
    {
        setLayout(new FitLayout());
        final LayoutContainer vp = new LayoutContainer();
        VBoxLayout layout = new VBoxLayout();
        layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        vp.setLayout(layout);

        tabPanel = new TabPanel();
        tabPanel.setBorders(false);
        VBoxLayoutData tabPanelLayoutData = new VBoxLayoutData(new Margins(0, 0, 0, 0));
        tabPanelLayoutData.setFlex(1);
        vp.add(tabPanel, tabPanelLayoutData);

        vp.setWidth(Window.getClientWidth() + "px");
        vp.setBorders(false);
        vp.setSize(Window.getClientWidth(), Window.getClientHeight());

        Window.addResizeHandler(new ResizeHandler()
        {
            public void onResize(ResizeEvent event)
            {
                int height = event.getHeight();
                vp.setHeight(height + "px");
                vp.setWidth(event.getWidth() + "px");
            }
        });
        add(vp);
        layout();
    }


    public void addTabForGPanel(final GPanel gpanel)
    {
        TabItem tabItem = new TabItem(gpanel.getLabel());
        addTabForGPanel(gpanel, null, tabItem, true);
    }

    @Override
    public void addTabForGPanel(GPanel gpanel, TabItem tabItem)
    {
        addTabForGPanel(gpanel, null, tabItem, true);
    }

    public void addTabForPresenter(Presenter presenter, boolean closable)
    {
        TabItem tabItem = new TabItem(presenter.getDisplay().getLabel());
        addTabForGPanel(presenter.getDisplay().asWidget(), presenter, tabItem, closable);
    }

    private void addTabForGPanel(final Widget widget, final Presenter presenter, TabItem item, boolean closable)
    {
        item.setClosable(closable);
        item.setLayout(new FitLayout());

        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setLayout(new FitLayout());
        panel.add(widget);

        item.add(panel);
        item.addListener(Events.BeforeClose,
            new Listener<BaseEvent>()
            {
                public void handleEvent(BaseEvent beforeCloseEvent)
                {
                    if (presenter != null)
                        presenter.unbind();
                    if (widget instanceof GPanel)
                        ((GPanel)widget).beforeClose(beforeCloseEvent);
                }
            });

        tabPanel.add(item);
        tabPanel.setSelection(item);
    }
}

