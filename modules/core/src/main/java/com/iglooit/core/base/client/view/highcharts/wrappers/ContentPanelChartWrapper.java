package com.iglooit.core.base.client.view.highcharts.wrappers;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.highcharts.charts.ButtonOptions;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;

@Deprecated
public class ContentPanelChartWrapper extends ContentPanel
{
    private ChartWrapper innerChartWrapper;

    public ContentPanelChartWrapper(ChartWrapper innerChartWrapper)
    {
        this.innerChartWrapper = innerChartWrapper;
        addCollapseButton();

        setCollapsible(true);
        setBodyBorder(false);
        setLayout(new FitLayout());
        addExpandStyle();
        addExpandCollapseHandler();
        add(innerChartWrapper);
        setSize(1180, 400);
    }

    public void drawChart()
    {
        innerChartWrapper.drawChart();
        setHeading(innerChartWrapper.getChartTitle());
    }

    private void addExpandCollapseHandler()
    {
        final Timer timer = new Timer()
        {
            @Override
            public void run()
            {
                innerChartWrapper.resizeChart();
            }
        };

        this.addListener(Events.Expand, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent be)
            {
                addExpandStyle();
                timer.schedule(500);
            }
        });

        this.addListener(Events.Collapse, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent be)
            {
                addCollapseStyle();
            }
        });
    }

    private void addExpandStyle()
    {
        this.addStyleName(ClarityStyle.PANEL_EMPTY);
        this.getHeader().addStyleName("hideHeader");
        this.removeStyleName(ClarityStyle.PANEL_STYLE_3);
    }

    private void addCollapseStyle()
    {
        this.addStyleName(ClarityStyle.PANEL_STYLE_3);
        this.removeStyleName(ClarityStyle.PANEL_EMPTY);
        this.getHeader().removeStyleName("hideHeader");
    }

    private void addCollapseButton()
    {
        ButtonOptions b = new ButtonOptions();
        b.setId("collapse");
        //todo here: this tooltip has to be in highchart.options.lang
        b.setTitlekey("collapse");
        b.setSymbol("circle");
        b.setX(-20);
        b.setSymbolFill("#bada55");
        b.setOnClick(collapseOnClick(this));

        innerChartWrapper.getModelData().addButton(b);
        /*MenuItemOptions m = new MenuItemOptions();
        m.setText("Click me");
        m.setOnClick(createOnClickCallBack(this));
        List<MenuItemOptions> ms = new ArrayList<MenuItemOptions>();
        ms.add(m);
        b.setMenuItems(ms);*/
    }

    private native JavaScriptObject collapseOnClick(ContentPanelChartWrapper chartWrapper)/*-{
      return function()
      {
        chartWrapper.@com.clarity.core.base.client.view.highcharts.wrappers.ContentPanelChartWrapper::collapse()();
      }
    }-*/;

    public void setPieChartSize(String pieChartSize)
    {
        innerChartWrapper.getModelData().setPieChartSize(pieChartSize);
    }

    public void setTitleFontSize(String titleFontSize)
    {
        innerChartWrapper.getModelData().setTitleFontSize(titleFontSize);
    }

    public void setEventCallback(ChartWrapper.ICallback eventCallback)
    {
        innerChartWrapper.setEventCallback(eventCallback);
    }

    public void hideSeries(String seriesName)
    {
        innerChartWrapper.hideSeries(seriesName);
    }

    public void showSeries(String seriesName)
    {
        innerChartWrapper.showSeries(seriesName);
    }

    public String getChartTitle()
    {
        return innerChartWrapper.getChartTitle();
    }

    public boolean isSeriesVisible(String seriesName)
     {
         return innerChartWrapper.isSeriesVisible(seriesName);
     }
}
