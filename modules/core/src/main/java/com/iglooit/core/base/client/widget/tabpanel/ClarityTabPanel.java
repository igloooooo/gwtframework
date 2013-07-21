package com.iglooit.core.base.client.widget.tabpanel;

import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.user.client.Element;

import java.util.HashMap;
import java.util.Map;

public class ClarityTabPanel extends TabPanel
{
    /**
     * Widget position enumeration.
     */
    public enum WidgetPosition
    {
        LEFT, RIGHT;
    }

    private static final class TabWidget
    {
        private Html html;
        private int widgetWidth;
        private boolean autoWidth;
        private HashMap<String, String> styles;

        private TabWidget(Html html, int widgetWidth, boolean autoWidth)
        {
            this.html = html;
            this.widgetWidth = widgetWidth;
            this.autoWidth = autoWidth;

            styles = new HashMap<String, String>();
        }

        public Html getHtml()
        {
            return html;
        }

        public void setHtml(Html html)
        {
            this.html = html;
        }

        public int getWidgetWidth()
        {
            return widgetWidth;
        }

        public void setWidgetWidth(int widgetWidth)
        {
            this.widgetWidth = widgetWidth;
        }

        public boolean isAutoWidth()
        {
            return autoWidth;
        }

        public HashMap<String, String> getStyles()
        {
            return styles;
        }

        public void setStyles(HashMap<String, String> styles)
        {
            this.styles = styles;
        }
    }

    private TabWidget tabWidgetLeft;
    private TabWidget tabWidgetRight;
    private El leftEl, rightEl;
    private int originalHeadBarWidth = -1;
    private int originalHeaderBarHeight;

    private El bar, stripWrap, strip, edge;
    private El scrollLeft, scrollRight;
    //maybe bugs here, when using parent.setSelection()
    private boolean scrolling;

    private static final int MARGIN_VALUE = 3;

    public ClarityTabPanel()
    {

    }

    public void addWidget(Html html, WidgetPosition position)
    {
        this.addWidget(html, position, 0, true);
    }

    public void addWidget(Html html, WidgetPosition position, int width)
    {
        this.addWidget(html, position, width, false);
    }

    public void addWidget(Html html, WidgetPosition position, int width, boolean autoWidth)
    {
        if (position == WidgetPosition.LEFT)
        {
            tabWidgetLeft = new TabWidget(html, width, autoWidth);
        }
        else if (position == WidgetPosition.RIGHT)
        {
            tabWidgetRight = new TabWidget(html, width, autoWidth);
        }
        else
        {
            throw new AppX("WidgetPosition should only be left or right");
        }
    }

    public void setWidgetStyle(WidgetPosition position, HashMap<String, String> styles)
    {
        if (position == WidgetPosition.LEFT && tabWidgetLeft != null)
        {
            tabWidgetLeft.setStyles(styles);
        }
        else if (position == WidgetPosition.RIGHT && tabWidgetRight != null)
        {
            tabWidgetRight.setStyles(styles);
        }
        else
        {
            throw new AppX("Either WidgetPosition wrong or that widget does not exist");
        }
    }

    public void updateWidget(Html html, WidgetPosition position)
    {
        if (tabWidgetLeft != null)
        {
            leftEl.remove();
            if (position == WidgetPosition.LEFT)
            {
                tabWidgetLeft.setHtml(html);
            }
        }

        if (tabWidgetRight != null)
        {
            rightEl.remove();
            if (position == WidgetPosition.RIGHT)
            {
                tabWidgetRight.setHtml(html);
            }
        }

        renderCustomerWidget();
    }

    public void hideWidget(WidgetPosition position)
    {
        if (position == WidgetPosition.LEFT && tabWidgetLeft != null)
        {
            leftEl.hide();
        }
        else if (position == WidgetPosition.RIGHT && tabWidgetLeft != null)
        {
            rightEl.hide();
        }
    }

    public void showWidget(WidgetPosition position)
    {
        if (position == WidgetPosition.LEFT && tabWidgetLeft != null)
        {
            leftEl.show();
        }
        else if (position == WidgetPosition.RIGHT && tabWidgetLeft != null)
        {
            rightEl.show();
        }
    }

    @Override
    protected void onRender(Element target, int index)
    {
        super.onRender(target, index);
        renderCustomerWidget();
    }

    public void renderCustomerWidget()
    {
        bar = getChildEl(el(), "x-tab-panel-header");
        if (originalHeadBarWidth == -1)
        {
            originalHeadBarWidth = bar.getWidth();
            originalHeaderBarHeight = bar.getHeight();
        }

        int headerBarWidth = originalHeadBarWidth;

        if (tabWidgetLeft != null)
        {
            renderLeftWidget();
            bar.setStyleAttribute("margin-left", String.valueOf(tabWidgetLeft.getWidgetWidth()));
            headerBarWidth = headerBarWidth - tabWidgetLeft.getWidgetWidth();
        }

        if (tabWidgetRight != null)
        {
            renderRightWidget();
            headerBarWidth = headerBarWidth - tabWidgetRight.getWidgetWidth() - MARGIN_VALUE;
        }

        bar.setWidth(headerBarWidth);
        stripWrap = getChildEl(bar, "x-tab-strip-wrap");
        stripWrap.setWidth(headerBarWidth);

        strip = stripWrap.firstChild();
        edge = getChildEl(strip, "x-tab-edge");
    }

    private void renderLeftWidget()
    {
        leftEl = el().insertFirst(tabWidgetLeft.getHtml().getHtml());
        leftEl.setStyleAttribute("width", String.valueOf(tabWidgetLeft.getWidgetWidth()));
        leftEl.setStyleAttribute("float", "left");
        leftEl.setStyleAttribute("position", "absolute");
        leftEl.setStyleAttribute("top", "0");
        leftEl.setStyleAttribute("left", "0");
        leftEl.setStyleAttribute("height", bar.getHeight());

        for (Map.Entry<String, String> entry : tabWidgetLeft.getStyles().entrySet())
        {
            leftEl.setStyleAttribute(entry.getKey(), entry.getValue());
        }
    }

    private void renderRightWidget()
    {
        rightEl = el().insertFirst(tabWidgetRight.getHtml().getHtml());

        if (tabWidgetRight.isAutoWidth())
        {
            TextMetrics metrics = TextMetrics.get();
            metrics.bind(rightEl);

            if (GXT.isIE)
            {
                //IE
                String[] splits = tabWidgetRight.getHtml().getHtml().split("<.+?>");
                StringBuilder sb = new StringBuilder();
                for (String split : splits)
                {
                    sb.append(split);
                }

                int stringWidth = metrics.getWidth(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                for (int i = 0; i < (stringWidth - 210) / 100; i++)
                {
                    sb2.append("W");
                }

                sb.append(sb2.toString());
                tabWidgetRight.setWidgetWidth(metrics.getWidth(sb.toString()));
            }
            else
            {
                //FireFox
                tabWidgetRight.setWidgetWidth(metrics.getWidth(tabWidgetRight.getHtml().getHtml()));
            }
        }

        rightEl.setStyleAttribute("width", String.valueOf(tabWidgetRight.getWidgetWidth()));
        rightEl.setStyleAttribute("position", "absolute");
        rightEl.setStyleAttribute("top", "0");
        rightEl.setStyleAttribute("right", MARGIN_VALUE);
        rightEl.setStyleAttribute("height", bar.getHeight());
//        rightEl.setStyleAttribute("height", originalHeaderBarHeight);

        for (Map.Entry<String, String> entry : tabWidgetRight.getStyles().entrySet())
        {
            rightEl.setStyleAttribute(entry.getKey(), entry.getValue());
        }
//        rightEl.setStyleAttribute("z-index", "10");
    }

    public static El getChildEl(El parent, String className)
    {
        int i = 0;
        El childEl = parent.getChild(i);
        while (childEl != null)
        {
            if (childEl.getStyleName().contains(className))
            {
                return childEl;
            }
            else
            {
                i++;
                childEl = parent.getChild(i);
            }
        }
        throw new AppX("Child El should exist:" + className);
    }


    @Override
    protected void onResize(int width, int height)
    {
        Size frameWidth = el().getFrameSize();

        int myHeight = height - frameWidth.height + bar.getHeight();
        int myWidth = width - frameWidth.width;  // minus widget widths here

        getLayoutTarget().setSize(myWidth, myHeight, true);

        int barWidth = width;
        if (tabWidgetLeft != null)
        {
            barWidth = barWidth - tabWidgetLeft.getWidgetWidth();
            leftEl.setStyleAttribute("height", bar.getHeight());
        }

        if (tabWidgetRight != null)
        {
            barWidth = barWidth - tabWidgetRight.getWidgetWidth() - MARGIN_VALUE;
            rightEl.setStyleAttribute("height", bar.getHeight());
        }

        bar.setWidth(barWidth, true);

        delegateUpdates();
    }

    private void delegateUpdates()
    {
        if (getResizeTabs() && rendered)
        {
            autoSizeTabs();
        }
        if (getTabScroll() && rendered)
        {
            autoScrollTabs();
        }
    }

    private void autoSizeTabs()
    {
        int count = getItemCount();
        if (count == 0) return;
        int aw = bar.getClientWidth();
        int each = (int)Math.max(Math.min(Math.floor((Double)(aw - 4.0) / count) - getTabMargin(), getTabWidth()),
            getMinTabWidth());

        for (int i = 0; i < count; i++)
        {
            El el = getItem(i).getHeader().el();
            Element inner = el.childNode(1).firstChild().firstChild().dom;
            int tw = el.getWidth();
            int iw = fly(inner).getWidth();
            fly(inner).setWidth(each - (tw - iw));
        }
    }

    private void autoScrollTabs()
    {
        int count = getItemCount();
        int tw = bar.getClientWidth();

        int cw = stripWrap.getWidth();
        int pos = stripWrap.getScrollLeft();
        int l = edge.getOffsetsTo(stripWrap.dom).x + pos;

        if (!getTabScroll() || count < 1 || cw < 20)
        {
            return;
        }

        if (l <= tw)
        {

            stripWrap.setWidth(tw);
            if (scrolling)
            {
                stripWrap.setScrollLeft(0);
                scrolling = false;
                bar.removeStyleName("x-tab-scrolling");
                scrollLeft.setVisible(false);
                scrollRight.setVisible(false);
            }
        }
        else
        {
            if (!scrolling)
            {
                bar.addStyleName("x-tab-scrolling");
            }
            tw -= 36;
            stripWrap.setWidth(tw > 20 ? tw : 20);
            if (!scrolling)
            {
                if (scrollLeft == null)
                {
                    createScrollers();
                }
                else
                {
                    scrollLeft.setVisible(true);
                    scrollRight.setVisible(true);
                }
            }
            scrolling = true;
            if (pos > (l - tw))
            {
                stripWrap.setScrollLeft(l - tw);
            }
            else
            {
                scrollToTab(getSelectedItem(), false);
            }
            updateScrollButtons();
        }
    }

    private void updateScrollButtons()
    {
        int pos = stripWrap.getScrollLeft();
        scrollLeft.setStyleName("x-tab-scroller-left-disabled", pos == 0);
        scrollRight.setStyleName("x-tab-scroller-right-disabled", pos >= (getScrollWidth() - getScrollArea() - 2));
    }

    private int getScrollWidth()
    {
        return edge.getOffsetsTo(stripWrap.dom).x + stripWrap.getScrollLeft();
    }

    private int getScrollArea()
    {
        return Math.max(0, stripWrap.getClientWidth());
    }

    private void createScrollers()
    {
        int h = stripWrap.getHeight();
        scrollLeft = bar.insertFirst("<div class='x-tab-scroller-left'></div>");
        addStyleOnOver(scrollLeft.dom, "x-tab-scroller-left-over");
        scrollLeft.setHeight(h);

        scrollRight = bar.insertFirst("<div class='x-tab-scroller-right'></div>");
        addStyleOnOver(scrollRight.dom, "x-tab-scroller-right-over");
        scrollRight.setHeight(h);
    }

}
