package com.iglooit.core.base.client.widget.tabpanel;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

/**
 * Convenience class for tab panels nested within a page. This class sets attributes and styles to provide
 * consistency across the application.
 */
public class PageTabPanel extends TabPanel
{
    private HandlerManager handlerManager = new HandlerManager(null);

    public PageTabPanel()
    {
        super();
        setPlain(true);
        setBodyBorder(false);
        addStyleName(ClarityStyle.TAB_PAGE);
    }

    public static MarginData getDefaultMargins()
    {
        return new MarginData(5, 0, 0, 0);
    }

    @Override
    public void onComponentEvent(ComponentEvent ce)
    {
        super.onComponentEvent(ce);
        if (ce.getEventTypeInt() == Event.ONCLICK)
        {
            El target = ce.getTargetEl();
            TabPanelClickEvent event = new TabPanelClickEvent(target);
            handlerManager.fireEvent(event);
        }
    }

    public HandlerRegistration addSelectionHandler(TabPanelClickEventHandler clickEventHandler)
    {
        final HandlerRegistration selectionReg = handlerManager.addHandler(TabPanelClickEvent.getType(),
            clickEventHandler);
        return selectionReg;
    }

    public static class TabPanelClickEvent extends GwtEvent<TabPanelClickEventHandler>
    {
        private static final Type<TabPanelClickEventHandler> TYPE =
            new Type<TabPanelClickEventHandler>();

        private El target;

        public TabPanelClickEvent(El target)
        {
            this.target = target;
        }

        public static Type<TabPanelClickEventHandler> getType()
        {
            return TYPE;
        }

        public El getTarget()
        {
            return target;
        }

        @Override
        public Type<TabPanelClickEventHandler> getAssociatedType()
        {
            return TYPE;
        }

        @Override
        protected void dispatch(TabPanelClickEventHandler handler)
        {
            handler.handle(this);
        }
    }

    public interface TabPanelClickEventHandler extends EventHandler
    {
        void handle(TabPanelClickEvent event);
    }

    /**
     * Adjusts CSS to fix a bug in IE where the bottom borders for a non-selected tab item do not appear correctly
     */
    public void setTabItemBorderFix(boolean enable)
    {
        String styleName = "tab-item-bottom-border-fix";
        if (GXT.isIE)
        {
            if (enable)
                addStyleName(styleName);
            else
                removeStyleName(styleName);
        }
    }
}
