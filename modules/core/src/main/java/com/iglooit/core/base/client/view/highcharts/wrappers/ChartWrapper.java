package com.iglooit.core.base.client.view.highcharts.wrappers;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.client.model.ChartModelData;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.highcharts.Chart;
import com.clarity.core.base.client.view.highcharts.charts.AreaSplineChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.BarChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.ButtonOptions;
import com.clarity.core.base.client.view.highcharts.charts.ColumnChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.LineChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.PieChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.SeriesOptions;
import com.clarity.core.base.client.view.highcharts.charts.SplineChartOptions;
import com.clarity.core.base.client.view.highcharts.config.CSSObject;
import com.clarity.core.base.client.view.highcharts.config.ChartLegendOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartTitleOptions;
import com.clarity.core.base.client.view.highcharts.config.DataLabelOptions;
import com.clarity.core.base.client.view.highcharts.config.PointOptions;
import com.clarity.core.base.client.view.highcharts.config.TooltipOptions;
import com.clarity.core.base.client.view.highcharts.config.XAxisOptions;
import com.clarity.core.base.client.view.highcharts.config.YAxisOptions;
import com.clarity.core.base.client.view.highcharts.helper.HighChartUtil;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;

import java.util.List;

public abstract class ChartWrapper<D extends ChartModelData> extends LayoutContainer
{
    public interface ICallback
    {
        void onLegendItemClicked(String seriesName, String chartTitle);
    }

    private final D modelData;
    private String chartTitle;
    private Chart chart;
    private ICallback eventCallback;

    private ContentPanel panel;

    public ChartWrapper(D modelData)
    {
        this.modelData = modelData;
        setLayout(new FitLayout());

        panel = new ContentPanel();
        panel.setSize(1000, 500);
        panel.setCollapsible(true);
        panel.setBodyBorder(false);
        panel.setLayout(new FitLayout());
        panel.setHeading(getModelData().getChartTitle());
        addExpandStyle();
        addExpandCollapseHandler();
    }

    public ContentPanel asPanel()
    {
        return panel;
    }

    public void drawChart()
    {
        checkValidation();
        initialiseChart();
        List<SeriesOptions> series = calculateSeries();

        chart.setSeriesOptions(series);
        add(chart);
    }

    protected abstract void checkValidation();

    protected void initialiseChart()
    {
        chartTitle = modelData.getChartTitle();
        createChart();
        TooltipOptions tooltips = createToolTips();
        XAxisOptions xAxis = createXAxisOptions(modelData.getXAxisTitle());
        YAxisOptions yAxis = createYAxisOptions(modelData.getYAxisTitle());
        ChartLegendOptions legend = createChartLegendOptions();
        ChartOptions chartOptions = createChartOptions();
        CSSObject style = createChartStyle();
        ChartTitleOptions chartTitle = createChartTitleOptions();
        ChartTitleOptions chartSubTitle = createChartSubTitleOptions();

        chart.setToolTip(tooltips);
        chart.setGlobalStyle(style);
        chart.setChartOptions(chartOptions);
        chart.setxAxisOptions(xAxis);
        chart.setYAxisOptions(yAxis);
        chart.setLegendOptions(legend);

        if (modelData.getChartHeight() != null)
            chart.setHeight(modelData.getChartHeight());

        if (modelData.getChartWidth() != null)
            chart.setWidth(modelData.getChartWidth());

        if (chartTitle != null)
            chart.setTitle(chartTitle);

        if (chartSubTitle != null)
            chart.setSubTitle(chartSubTitle);

        if (modelData.isCollapsible())
        {
            addCollapseButton();
            panel.add(this);
        }
        chart.setButtonOptions(modelData.getButtons());
    }

    protected void createChart()
    {
        chart = new Chart();
        HighChartUtil.deferChartResizeOnContainerResize(chart, this, false);
    }

    protected abstract TooltipOptions createToolTips();

    protected abstract XAxisOptions createXAxisOptions(String xAxisTitle);

    protected abstract YAxisOptions createYAxisOptions(String yAxisTitle);

    protected abstract ChartLegendOptions createChartLegendOptions();

    protected abstract ChartOptions createChartOptions();

    protected abstract ChartTitleOptions createChartTitleOptions();

    protected abstract ChartTitleOptions createChartSubTitleOptions();

    protected abstract List<SeriesOptions> calculateSeries();

    private CSSObject createChartStyle()
    {
        CSSObject style = new CSSObject();
        style.set("fontFamily", "Arial");
        return style;
    }

    protected PieChartOptions createPieChartOptions(List<PointOptions> pieChartData)
    {
        PieChartOptions pieChartSeriesOptions = new PieChartOptions();
        pieChartSeriesOptions.setShowInLegend(false);
        pieChartSeriesOptions.setAllowPointSelect(true);
        pieChartSeriesOptions.setSize(modelData.getPieChartSize());
        pieChartSeriesOptions.setCenter(new Tuple2<String, String>("10%", "20%"));
        DataLabelOptions dataLabelOptions = new DataLabelOptions();
        dataLabelOptions.setEnabled(Boolean.FALSE);
        pieChartSeriesOptions.setDataLabels(dataLabelOptions);
        pieChartSeriesOptions.setDataArray(pieChartData);

        return pieChartSeriesOptions;
    }

    protected SeriesOptions createSeriesOptions()
    {
        final SeriesOptions seriesOptions;
        switch (modelData.getChartType())
        {
            case LINE:
                seriesOptions = new LineChartOptions();
                break;
            case SPLINE:
                seriesOptions = new SplineChartOptions();
                break;
            case AREASPLINE:
                seriesOptions = new AreaSplineChartOptions();
                if (modelData.isStacking()) seriesOptions.setStacking("normal");
                break;
            case COLUMN:
                seriesOptions = new ColumnChartOptions();
                if (modelData.isStacking()) seriesOptions.setStacking("normal");
                break;
            case PIE:
                seriesOptions = new PieChartOptions();
                seriesOptions.setShowInLegend(false);
                break;
            case BAR:
                seriesOptions = new BarChartOptions();
                if (modelData.isStacking()) seriesOptions.setStacking("normal");
                break;
            default:
                throw new AppX("chart type '" + modelData.getChartType() + "' not supported");
        }

        if (eventCallback != null)
        {
            seriesOptions.addListener(SeriesOptions.LEGENDITEMCLICKED, new Listener<BaseEvent>()
            {
                @Override
                public void handleEvent(BaseEvent be)
                {
                    eventCallback.onLegendItemClicked(seriesOptions.getName(), getModelData().getChartTitle());
                }
            });
        }

        return seriesOptions;
    }

    public D getModelData()
    {
        return modelData;
    }

    public Chart getChart()
    {
        return chart;
    }

    public String getChartTitle()
    {
        return chartTitle;
    }

    public JavaScriptObject getJsChart()
    {
        return chart.getChartObject();
    }

    public void resizeChart()
    {
        chart.setSize(this.getWidth(), this.getHeight());
        chart.layout(true);
    }

    public void setEventCallback(ICallback eventCallback)
    {
        this.eventCallback = eventCallback;
    }

    public void hideSeries(String seriesName)
    {
        chart.hideSeries(seriesName);
    }

    public void showSeries(String seriesName)
    {
        chart.showSeries(seriesName);
    }

    public boolean isSeriesVisible(String seriesName)
    {
        return chart.isSeriesVisible(seriesName);
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
        b.setOnClick(collapseOnClick(panel));

        getModelData().addButton(b);
        /*MenuItemOptions m = new MenuItemOptions();
        m.setText("Click me");
        m.setOnClick(createOnClickCallBack(this));
        List<MenuItemOptions> ms = new ArrayList<MenuItemOptions>();
        ms.add(m);
        b.setMenuItems(ms);*/
    }

    private native JavaScriptObject collapseOnClick(ContentPanel contentPanel)/*-{
      return function()
      {
        contentPanel.@com.extjs.gxt.ui.client.widget.ContentPanel::collapse()();
      }
    }-*/;

    private void addExpandStyle()
    {
        panel.addStyleName(ClarityStyle.PANEL_EMPTY);
        panel.getHeader().addStyleName("hideHeader");
        panel.removeStyleName(ClarityStyle.PANEL_STYLE_3);
    }

    private void addCollapseStyle()
    {
        panel.addStyleName(ClarityStyle.PANEL_STYLE_3);
        panel.removeStyleName(ClarityStyle.PANEL_EMPTY);
        panel.getHeader().removeStyleName("hideHeader");
    }

    private void addExpandCollapseHandler()
    {
        final Timer timer = new Timer()
        {
            @Override
            public void run()
            {
                resizeChart();
            }
        };

        panel.addListener(Events.Expand, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent be)
            {
                addExpandStyle();
                timer.schedule(500);
            }
        });

        panel.addListener(Events.Collapse, new Listener<ComponentEvent>()
        {
            @Override
            public void handleEvent(ComponentEvent be)
            {
                addCollapseStyle();
            }
        });
    }

    public void updateTitle()
    {
        chart.setTitle(createChartTitleOptions(), createChartSubTitleOptions());
    }
}
