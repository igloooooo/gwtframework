package com.iglooit.core.base.client.view.highcharts;

import com.clarity.core.base.client.view.highcharts.charts.ButtonOptions;
import com.clarity.core.base.client.view.highcharts.charts.SeriesOptions;
import com.clarity.core.base.client.view.highcharts.config.CSSObject;
import com.clarity.core.base.client.view.highcharts.config.ChartLegendOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartTitleOptions;
import com.clarity.core.base.client.view.highcharts.config.PaneOptions;
import com.clarity.core.base.client.view.highcharts.config.PlotOptions;
import com.clarity.core.base.client.view.highcharts.config.TooltipOptions;
import com.clarity.core.base.client.view.highcharts.config.XAxisOptions;
import com.clarity.core.base.client.view.highcharts.config.YAxisOptions;
import com.clarity.core.base.client.view.highcharts.data.Axis;
import com.clarity.core.base.client.view.highcharts.data.Point;
import com.clarity.core.base.client.view.highcharts.data.Series;
import com.clarity.core.base.client.view.highcharts.helper.ExportType;
import com.clarity.core.base.client.view.highcharts.helper.HighChartUtil;
import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart extends GPanel
{
    public static final String TYPE_COLUMN = "column";

    private JavaScriptObject chartObject;
    private ChartOptions chartOptions = new ChartOptions();
    private PlotOptions plotOptions = new PlotOptions();
    private ChartLegendOptions legendOptions = new ChartLegendOptions();
    private ChartTitleOptions titleOptions = new ChartTitleOptions();
    private ChartTitleOptions subTitleOptions = new ChartTitleOptions();
    private XAxisOptions xAxisOptions = new XAxisOptions();
    private YAxisOptions yAxisOptions = new YAxisOptions();
    private YAxisOptions y1AxisOptions = null;
    private List<SeriesOptions> seriesOptions = new ArrayList<SeriesOptions>();
    private List<ButtonOptions> buttonOptions = new ArrayList<ButtonOptions>();
    private boolean useUTC = true;
    private TooltipOptions tooltipOptions = new TooltipOptions();

    public void setPaneOptions(PaneOptions paneOptions)
    {
        this.paneOptions = paneOptions;
    }

    private PaneOptions paneOptions = new PaneOptions();

    // chart model that stores our data 

    public Chart()
    {
        setStyleAttribute("position", "relative");
    }


    private JavaScriptObject jgetChartOptions()
    {
        return Util.getJsObject(chartOptions, 10);
    }

    private JavaScriptObject jgetLegendOptions()
    {
        if (legendOptions == null)
            return Util.getJsObject(new BaseModel());
        return Util.getJsObject(legendOptions, 10);
    }

    private JavaScriptObject jgetToolTipOptions()
    {
        return tooltipOptions == null ? Util.getJsObject(new BaseModel()) : Util.getJsObject(tooltipOptions, 10);
    }

    private JavaScriptObject jgetTitleOptions()
    {
        return titleOptions == null ? Util.getJsObject(new BaseModel()) : Util.getJsObject(titleOptions, 10);
    }

    private JavaScriptObject jgetSubTitleOptions()
    {
        return subTitleOptions == null ? Util.getJsObject(new BaseModel()) : Util.getJsObject(subTitleOptions, 10);
    }

    private JavaScriptObject jgetPlotOptions()
    {
        return plotOptions == null ? Util.getJsObject(new BaseModel()) : Util.getJsObject(plotOptions, 10);
    }

    private JavaScriptObject jgetxAxisOptions()
    {
        if (xAxisOptions == null)
            return Util.getJsObject(new BaseModel());
        return Util.getJsObject(xAxisOptions, 10);
    }

    private JavaScriptObject jgetyAxisOptions()
    {
        if (yAxisOptions == null)
            return Util.getJsObject(new BaseModel());
        if (y1AxisOptions != null)
        {
            return createJsObject(Util.getJsObject(yAxisOptions, 10), Util.getJsObject(y1AxisOptions, 10));
        }
        return Util.getJsObject(yAxisOptions, 10);
    }

    private native JavaScriptObject createJsObject(JavaScriptObject jsObject, JavaScriptObject jsObject1)/*-{
        return [jsObject, jsObject1];
    }-*/;

    private JavaScriptObject jgetSeries()
    {
        JsArray arr = JavaScriptObject.createArray().cast();

        for (SeriesOptions option : seriesOptions)
        {
            arr.push(Util.getJsObject(option, 10));
        }

        return arr;
    }

    private JavaScriptObject jgetPaneOptions()
    {
        return Util.getJsObject(paneOptions, 10);
    }

    //feel free to override this method if not happy with the default CSV converting
    protected String convertSeriesDataToCVS()
    {
        List<Series> series = getSeries();

        if (series == null || series.isEmpty())
            return "";

        // series is the header
        //
        StringBuilder header = new StringBuilder();
        Map<String, List<Point>> points = new HashMap<String, List<Point>>();
        // order ascending based on date
        StringBuilder content = new StringBuilder();
        header.append("\"\"");
        for (Series s : series)
        {
            if (header.length() != 0)
            {
                header.append(",");
            }

            header.append("\"").append(s.getName()).append("\"");
            points.put(s.getName(), s.getPoints());
        }

        Series s1 = series.get(0);
        boolean firstColumn = true;
        for (int i = 0; i < s1.getPoints().size(); ++i)
        {
            firstColumn = true;
            for (Series s : series)
            {
                List<Point> pts = points.get(s.getName());
                if (pts.isEmpty() || i >= pts.size())
                    continue;
                Point p = pts.get(i);
                if (firstColumn)
                {
                    firstColumn = false;
                    Double x = p.getX();
                    content.append("\"");
                    content.append(x);
                    content.append("\",");
                }
                else
                {
                    content.append(",");
                }
                content.append("\"");
                content.append(p.getY());
                content.append("\"");
            }
            content.append("\n");
        }


        return header.toString() + "\n" + content.toString();
    }


    public void export(ExportType type)
    {
        if (ExportType.CSV == type)
            HighChartUtil.exportToCSV(convertSeriesDataToCVS());
        else
            HighChartUtil.export(this, chartObject, type);
    }

    //this method is designed for subclass to override when they want to export something after chart is rendered
    //refer to ASSURE-71 Missing units within the Column when exporting to CSV from graph view
    //One example is to export an additional table of data when export to PDF, as does in PerfDiagnoseChart.java
    protected native void onChartLoad(JavaScriptObject jsChart, Chart javaChart)/*-{
    }-*/;

    //this method is designed for subclass to override when they want to export something after chart is rendered
    //set the extra height properly to make the export correct.
    protected int getExtraExportDataHeight()
    {
        return 0;
    }

    /**
     * Javascript related functions
     */

    private native JavaScriptObject doRender(Chart instance, Element element)/*-{

        var chartOptions = instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetChartOptions()();
        chartOptions.renderTo = element;
        chartOptions.events =
        {
            //for some reason, it only works here if needed extra info to be exported.
            load: function()
            {
                instance.@com.clarity.core.base.client.view.highcharts.Chart::onChartLoad(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/clarity/core/base/client/view/highcharts/Chart;)(this, instance);
                var customButtons = instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetButtons()();
                var n;
                if (this.options.exporting.enabled !== false)
                {
                  for (n in customButtons)
                  {
                     this.addButton(customButtons[n]);
                  }
                }
            }
        };
        var chart = new $wnd.Highcharts.Chart({
        chart:chartOptions,
            credits:{enabled:false},
            //colors: ['#4572A7', '#AA4643'], // by default 9 available colors supported by highchart
            global: {useUTC:true},
            pane: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetPaneOptions()(),
            //labels: {...},
            //lang: {},
            legend: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetLegendOptions()(),
            //loading: {...},
            plotOptions: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetPlotOptions()(),
            //point: {...},
            series: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetSeries()(),
            subtitle: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetSubTitleOptions()(),
            //symbols: [...]
            title: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetTitleOptions()(),
            tooltip: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetToolTipOptions()(),
            xAxis: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetxAxisOptions()(),
            yAxis: instance.@com.clarity.core.base.client.view.highcharts.Chart::jgetyAxisOptions()(),
            exporting:{
                url: @com.clarity.core.base.client.view.highcharts.helper.HighChartUtil::getExportServletURL()(),
                enabled: true,
                buttons:{ exportButton: {enabled: false}, printButton: {enabled: false} }
            }
        });

        return chart;
        }-*/;

    private native void redraw(JavaScriptObject obj)/*-{        
        obj.redraw();
    }-*/;

    private native void destroy(JavaScriptObject obj)/*-{

        obj.destroy();
    }-*/;

    private native void addSeries(JavaScriptObject chart, JavaScriptObject series, boolean redraw)/*-{
        var obj = chart.addSeries(series, redraw);


        //chart.options.series.push(series);


    }-*/;

    private native void setTitle(JavaScriptObject chart, JavaScriptObject title, JavaScriptObject subTitle)/*-{
        chart.setTitle(title, subTitle);
        chart.options.title = title;
        chart.options.subtitle = subTitle;

    }-*/;

    private native void setXAxisCategoriesJs(JavaScriptObject chart, JsArrayString categories)/*-{
          if (chart == null || chart.xAxis == null || chart.xAxis.length == 0) return;
          var xAxis = chart.xAxis[0];
          if (xAxis == null) return;
          xAxis.setCategories(categories, true);
        }-*/;

    private native void setXAxisTitleJs(JavaScriptObject chart, JavaScriptObject title)/*-{
          if (chart == null || chart.xAxis == null || chart.xAxis.length == 0) return;
          var xAxis = chart.xAxis[0];
          if (xAxis == null) return;
          xAxis.setTitle(title, true);
        }-*/;

    private native void setYAxisTitleJs(JavaScriptObject chart, JavaScriptObject title)/*-{
          if (chart == null || chart.yAxis == null || chart.yAxis.length == 0) return;
          var yAxis = chart.yAxis[0];
          if (yAxis == null) return;
          yAxis.setTitle(title, true);
        }-*/;


    private native void setSize(JavaScriptObject chartObject, int width, int height)/*-{        
        chartObject.setSize(width, height);
    }-*/;

    private native JsArray<JavaScriptObject> getSeries(JavaScriptObject chart)/*-{
            return chart.series;
    }-*/;

    private native JavaScriptObject getItem(JavaScriptObject chart, String id)/*-{
        var obj = chart.get(id)
        
        return chart.get(id);
    }-*/;

    public Series getSeries(String id)
    {
        if (chartObject == null)
            return null;
        Series s = new Series();
        JavaScriptObject obj = getItem(chartObject, id);
        if (obj == null)
            return null;

        s.initialise(obj);

        return s;
    }

    public Axis getAxis(String id)
    {
        if (chartObject == null)
            return null;
        Axis axis = new Axis();
        axis.initialise(getItem(chartObject, id));

        return axis;
    }

    public void addSeriesOption(SeriesOptions option)
    {
        this.seriesOptions.add(option);
    }

    public void setSeriesOptions(List<SeriesOptions> options)
    {
        this.seriesOptions = options;
    }


    public List<SeriesOptions> getSeriesOptions()
    {
        return seriesOptions;
    }

    public ChartOptions getChartOptions()
    {
        return chartOptions;
    }

    public void setChartOptions(ChartOptions chartOptions)
    {
        this.chartOptions = chartOptions;
    }

    public PlotOptions getPlotOptions()
    {
        return plotOptions;
    }

    public void setPlotOptions(PlotOptions plotOptions)
    {
        this.plotOptions = plotOptions;
    }

    public ChartLegendOptions getLegendOptions()
    {
        return legendOptions;
    }

    public void setLegendOptions(ChartLegendOptions legendOptions)
    {
        this.legendOptions = legendOptions;
    }

    public ChartTitleOptions getTitleOptions()
    {
        return titleOptions;
    }

    public void setTitleOptions(ChartTitleOptions titleOptions)
    {
        this.titleOptions = titleOptions;
    }

    public ChartTitleOptions getSubTitleOptions()
    {
        return subTitleOptions;
    }

    public void setSubTitleOptions(ChartTitleOptions subTitleOptions)
    {
        this.subTitleOptions = subTitleOptions;
    }

    public XAxisOptions getxAxisOptions()
    {
        return xAxisOptions;
    }

    public void setxAxisOptions(XAxisOptions xAxisOptions)
    {
        this.xAxisOptions = xAxisOptions;
    }

    public YAxisOptions getyAxisOptions()
    {
        return yAxisOptions;
    }

    public void setYAxisOptions(YAxisOptions yAxisOptions)
    {
        this.yAxisOptions = yAxisOptions;
    }

    public boolean isUseUTC()
    {
        return useUTC;
    }

    public void setUseUTC(boolean useUTC)
    {
        this.useUTC = useUTC;
        jsetUseUTC(useUTC);
    }

    private native void jsetUseUTC(boolean useUTC)/*-{        
        $wnd.Highcharts.setOptions({
            global: {
                useUTC : useUTC
            }
        });
    }-*/;

    private native void jsetGlobalStyle(JavaScriptObject style)/*-{    
        $wnd.Highcharts.setOptions({
            chart:
            {
                style: style
            }
        });
        
    }-*/;


    public void setGlobalStyle(CSSObject object)
    {
        jsetGlobalStyle(Util.getJsObject(object, 10));
    }

    @Override
    public void doOnRender(Element element, int i)
    {
        chartObject = doRender(this, element);
    }

    public void addSeries(SeriesOptions series, boolean redraw)
    {
        //any time when added something to the js, remember to add them to the Java Object as well
        //then the chart can be redrawn for export
        this.getSeriesOptions().add(series);
        if (chartObject != null)
            addSeries(chartObject, Util.getJsObject(series, 10), redraw);
    }

    public void setTitle(ChartTitleOptions title)
    {
        setTitle(title, null);
    }

    public void setSubTitle(ChartTitleOptions subTitle)
    {
        setTitle(null, subTitle);
    }


    public void setTitle(ChartTitleOptions title, ChartTitleOptions subTitle)
    {
        if (this.chartObject == null)
        {
            if (title != null)
                setTitleOptions(title);
            if (subTitle != null)
                setSubTitleOptions(subTitle);
            return;
        }
        JavaScriptObject titleObj = JavaScriptObject.createObject();
        if (title == null)
            titleObj = Util.getJsObject(new ChartTitleOptions());
        else
            titleObj = Util.getJsObject(title, 10);

        JavaScriptObject subTitleObj = JavaScriptObject.createObject();
        if (subTitle == null)
            subTitleObj = Util.getJsObject(new ChartTitleOptions());
        else
            subTitleObj = Util.getJsObject(subTitle, 10);

        setTitle(chartObject, titleObj, subTitleObj);
    }

    public void setXAxisCategories(List<String> categories)
    {
        if (this.chartObject != null && categories != null)
        {
            JsArrayString arr = JavaScriptObject.createArray().cast();
            for (String s : categories)
            {
                arr.push(s);
            }
            setXAxisCategoriesJs(this.chartObject, arr);
        }
    }

    /**
     * This is just a CONVENIENT method to set categories for X axis
     *
     * @param categories
     */
    public void setXCategories(List<String> categories)
    {
        getxAxisOptions().setCategories(categories);
        setXAxisCategories(categories);
    }

    public void redraw()
    {
        if (chartObject == null)
            return;
        if (!isVisible())
            return;
        redraw(chartObject);
    }

    public void destroy()
    {
        if (chartObject == null)
            return;
        destroy(chartObject);

    }

    public void setSize(int width, int height)
    {
        if (isVisible())
        {
            setSize(chartObject, width, height);
        }
    }

    public List<Series> getSeries()
    {
        if (chartObject == null)
        {
            return Collections.emptyList();
        }
        JsArray<JavaScriptObject> arr = getSeries(chartObject);

        List<Series> series = new ArrayList<Series>();

        for (int i = 0; i < arr.length(); ++i)
        {
            Series s = new Series();
            s.initialise(arr.get(i));
            series.add(s);
        }

        return series;
    }

    @Override
    public String getLabel()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setYAxisTitle(String title)
    {
        setYAxisTitle(title, this.chartObject);
    }

    public void setXAxisTitle(String title)
    {
        setXAxisTite(title, this.chartObject);
    }

    public void setXAxisTitleAndApply(ChartTitleOptions title)
    {
        setXAxisTitleJs(this.chartObject, Util.getJsObject(title, 10));
    }

    public void setYAxisTitleAndApply(ChartTitleOptions title)
    {
        this.getyAxisOptions().setTitle(title);
        setYAxisTitleJs(this.chartObject, Util.getJsObject(title, 10));
    }

    public native JavaScriptObject getEmptyObject()/*-{
        return {};
    }-*/;


    private native void setXAxisTite(String title, JavaScriptObject chart)/*-{
       if (chart == null)
       {
            return; // do nothing
       }
       if (chart.options != undefined && chart.options.xAxis != undefined)
       {
           chart.options.xAxis.title = {text: title};
       }

       if (chart.xAxis == undefined)
       {
           return;
       }
       if (chart.xAxis[0].axisTitle == undefined)
       {
           return;
       }

       $wnd.$(chart.xAxis[0].axisTitle.element).text(title);
    }-*/;


    private native void setYAxisTitle(String title, JavaScriptObject chart)/*-{
       if (chart == null)
       {
            return; // do nothing
       }
       if (chart.options != undefined && chart.options.yAxis != undefined)
            chart.options.yAxis.title = {text:title};

       if (chart.yAxis == undefined)
       {
           return;
       }
       if (chart.yAxis[0].axisTitle == undefined)
       {
            return;
       }
        
       $wnd.$(chart.yAxis[0].axisTitle.element).text(title);
    }-*/;


    public void setToolTip(TooltipOptions tooltips)
    {
        this.tooltipOptions = tooltips;
    }

    public TooltipOptions getTooltipOptions()
    {
        return this.tooltipOptions;
    }

    public void showLegend(boolean showLegend)
    {
        showLegend(chartObject, showLegend);
    }

    private native void showLegend(JavaScriptObject chart, boolean show)/*-{
        if (!show)
        {
            chart.options.legend.enabled = false;
            $wnd.$('.highcharts-legend', chart.container).hide();
        }
        else
        {
            chart.options.legend.enabled = true;            
            $wnd.$('.highcharts-legend', chart.container).show();
        }
    }-*/;

    public PaneOptions getPaneOptions()
    {
        return paneOptions;
    }

    private native void setXAxisExtremes(Number min, Number max, Boolean redraw, JavaScriptObject chart)/*-{
        if (chart == null)
        {
            return; // do nothing
        }
        if (chart.xAxis[0] == undefined)
        {
            return;
        }

        chart.xAxis[0].setExtremes(min, max, redraw);
    }-*/;

    public void setXAxisExtremes(Number min, Number max, Boolean redraw)
    {
        setXAxisExtremes(min, max, redraw, chartObject);
    }

    public void clearSeries()
    {
        for (Series s : getSeries())
        {
            s.remove(false);
        }
    }

    public boolean isChartRendered()
    {
        return !(chartObject == null);
    }

    public JavaScriptObject getChartObject()
    {
        return chartObject;
    }

    public void sety1AxisOptions(YAxisOptions yAxisOption)
    {
        this.y1AxisOptions = yAxisOption;
    }

    public void setButtonOptions(List<ButtonOptions> buttonOptions)
    {
        this.buttonOptions = buttonOptions;
    }

    private JavaScriptObject jgetButtons()
    {
        JsArray arr = JavaScriptObject.createArray().cast();

        if (buttonOptions != null)
        {
            for (ButtonOptions option : buttonOptions)
            {
                arr.push(Util.getJsObject(option, 10));
            }
        }

        return arr;
    }

    public void hideSeries(String seriesName)
    {
        if (seriesName == null)
            return;

        List<Series> seriesList = getSeries();
        for (Series s : seriesList)
        {
            if (seriesName.equals(s.getName()))
                s.hide();
        }
    }

    public void showSeries(String seriesName)
    {
        if (seriesName == null)
            return;

        List<Series> seriesList = getSeries();
        for (Series s : seriesList)
        {
            if (seriesName.equals(s.getName()))
                s.show();
        }
    }

    public boolean isSeriesVisible(String seriesName)
    {
        if (seriesName == null)
            return false;

        List<Series> seriesList = getSeries();
        for (Series s : seriesList)
        {
            if (seriesName.equals(s.getName()))
                return s.getVisible();
        }
        return false;
    }
}
