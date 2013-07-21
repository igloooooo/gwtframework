package com.iglooit.core.base.client.view.highcharts.wrappers;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.model.NonTimeXAxisChartModelData;
import com.clarity.core.base.client.view.highcharts.charts.BarChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.ColumnChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.LineChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.PieChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.SeriesOptions;
import com.clarity.core.base.client.view.highcharts.config.AxisLabel;
import com.clarity.core.base.client.view.highcharts.config.CSSObject;
import com.clarity.core.base.client.view.highcharts.config.ChartLegendOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartTitleOptions;
import com.clarity.core.base.client.view.highcharts.config.TooltipOptions;
import com.clarity.core.base.client.view.highcharts.config.XAxisOptions;
import com.clarity.core.base.client.view.highcharts.config.YAxisOptions;
import com.clarity.core.base.client.view.highcharts.data.PointDataArray;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NonTimeXAxisChartWrapper<MD extends NonTimeXAxisChartModelData>
        extends ChartWrapper<NonTimeXAxisChartModelData>
{

    public NonTimeXAxisChartWrapper(MD modelData)
    {
        super(modelData);
    }

    @Override
    protected void checkValidation()
    {
        if (getModelData() == null || getModelData().getSeriesData() == null)
            throw new AppX("no data to generate chart");
    }

    @Override
    protected XAxisOptions createXAxisOptions(String xAxisTitle)
    {
        // x axis option
        ChartTitleOptions xAxisTitleOption = new ChartTitleOptions(false);
        xAxisTitleOption.setText(xAxisTitle);
        xAxisTitleOption.setMargin(15);
        AxisLabel xLabel = new AxisLabel(false);
        xLabel.setAlign("right");
        xLabel.setDoubleRotation(290D);
        //xLabel.setX(5);
        XAxisOptions xAxis = new XAxisOptions(false);
        xAxis.setLabels(xLabel);
        xAxis.setTitle(xAxisTitleOption);
        xAxis.setTickmarkPlacement("on");
        xAxis.setCategories(getModelData().getXCategories());
        return xAxis;
    }

    @Override
    protected YAxisOptions createYAxisOptions(String yAxisTitle)
    {
        // y axis option
        ChartTitleOptions yAxisTitleOption = new ChartTitleOptions(false);
        //todo i18n
        yAxisTitleOption.setText(yAxisTitle);
        YAxisOptions yAxis = new YAxisOptions(false);
        yAxis.setTitle(yAxisTitleOption);
        return yAxis;
    }

    @Override
    protected ChartLegendOptions createChartLegendOptions()
    {
        // legend option
        ChartLegendOptions legend = new ChartLegendOptions(false);
        legend.setVerticalAlign("top");
        legend.setFloating(true);
        legend.setY(35);
        legend.setLayout("horizontal");
        CSSObject legendStyle = new CSSObject();
        legendStyle.set("fontSize", "10px");
        legend.setItemStyle(legendStyle);
        return legend;
    }

    @Override
    protected ChartOptions createChartOptions()
    {
        ChartOptions chartOptions = new ChartOptions(false);
        chartOptions.setBorderRadius(3);
        chartOptions.setMarginTop(70);
        chartOptions.setMarginRight(30);
        chartOptions.setZoomType("x");
        chartOptions.remove(ChartOptions.MARGINLEFT);
        return chartOptions;
    }

    @Override
    protected ChartTitleOptions createChartTitleOptions()
    {
        if (StringUtil.isBlank(getModelData().getChartTitle()))
            return null;

        ChartTitleOptions chartTitleOptions = new ChartTitleOptions();
        if (getModelData().getTitleFontSize() != null)
        {
            CSSObject mainTitleStyle = new CSSObject();
            //mainTitleStyle.set("fontWeight", "bold");
            //mainTitleStyle.set("color", "#3E576F");  // default color
            mainTitleStyle.set("fontSize", getModelData().getTitleFontSize());
            chartTitleOptions.setStyle(mainTitleStyle);
        }
        chartTitleOptions.setText(getModelData().getChartTitle());
        chartTitleOptions.setY(5);
        return chartTitleOptions;
    }

    @Override
    protected ChartTitleOptions createChartSubTitleOptions()
    {
        if (StringUtil.isBlank(getModelData().getChartSubTitle()))
            return null;

        ChartTitleOptions chartSubTitleOptions = new ChartTitleOptions();
        chartSubTitleOptions.setText(getModelData().getChartSubTitle());
        if (getModelData().getTitleFontSize() != null)
        {
            CSSObject subTitleStyle = new CSSObject();
            //subTitleStyle.set("fontWeight", "normal");
            //subTitleStyle.set("color", "#6D869F");  // default color
            subTitleStyle.set("fontSize", getModelData().getTitleFontSize());
            chartSubTitleOptions.setStyle(subTitleStyle);
        }
        chartSubTitleOptions.setY(25);

        return chartSubTitleOptions;
    }

    @Override
    protected TooltipOptions createToolTips()
    {
        TooltipOptions tooltips = new TooltipOptions();
        tooltips.setFormatter(createTooltipFormatter(getModelData().isShowPercentage()));
        tooltips.set(TooltipOptions.BORDERCOLOR, "rgba(62, 69, 79, 0.5)");
        tooltips.set(TooltipOptions.SHARED, getModelData().getShared());
        return tooltips;
    }

    //if show percentage, the requirement "is will expose both the raw value and percentage value for each plot"
    //here percentage value is point.y value in highchart, and we have to save raw value to somewhere in point JS
    //object or we have to get it from Java code. Now I choose same it in point.name.
    private native JavaScriptObject createTooltipFormatter(boolean isShowPercentage)/*-{
    return function()
    {
        if( this.series.type == 'pie')
            return this.point.name +': '+ this.y +' (' + this.point.percentage.toPrecision(3) + '%)';

        var stackedInfo = this.point.stackTotal;
        if (stackedInfo != null)
            stackedInfo = '<br/>Total: ' + this.point.stackTotal;
        else
            stackedInfo = '';

        var tooltipValue = '<b>' + this.x + '</b><br/>' + this.series.name + ': ';
        if (isShowPercentage)
            return tooltipValue + this.point.name + ' (' + this.y + '%)' + stackedInfo;
        else
            return tooltipValue + this.y + stackedInfo;
    }
    }-*/;

    @Override
    protected List<SeriesOptions> calculateSeries()
    {
        switch (getModelData().getChartType())
        {
            case PIE:
                return calculatePieSeries();
            case LINE:
                return calculateLineSeries();
            case COLUMN:
                return calculateColumnSeries();
            case BAR:
                return calculateBarSeries();
            default:
                throw new AppX("Graph type is not supported");
        }
    }

    private List<SeriesOptions> calculateColumnSeries()
    {
        List<SeriesOptions> series = new ArrayList<SeriesOptions>();
        Map<String, PointDataArray> dataArray = getModelData().getSeriesData();
        for (Map.Entry<String, PointDataArray> entry : dataArray.entrySet())
        {
            ColumnChartOptions seriesOptions = new ColumnChartOptions();
            seriesOptions.setStacking("normal");
            seriesOptions.setShowInLegend(true);
            populateSeriesOption(entry, seriesOptions);
            series.add(seriesOptions);
        }
        return series;
    }

    private List<SeriesOptions> calculateBarSeries()
    {
        List<SeriesOptions> series = new ArrayList<SeriesOptions>();
        Map<String, PointDataArray> dataArray = getModelData().getSeriesData();
        for (Map.Entry<String, PointDataArray> entry : dataArray.entrySet())
        {
            BarChartOptions seriesOptions = new BarChartOptions();
            seriesOptions.setStacking("normal");
            seriesOptions.setShowInLegend(true);
            populateSeriesOption(entry, seriesOptions);
            series.add(seriesOptions);
        }
        return series;
    }

    private List<SeriesOptions> calculateLineSeries()
    {
        List<SeriesOptions> series = new ArrayList<SeriesOptions>();
        Map<String, PointDataArray> dataArray = getModelData().getSeriesData();
        for (Map.Entry<String, PointDataArray> entry : dataArray.entrySet())
        {
            LineChartOptions seriesOptions = new LineChartOptions();
            seriesOptions.setShowInLegend(true);
            populateSeriesOption(entry, seriesOptions);
            series.add(seriesOptions);
        }
        return series;
    }

    private void populateSeriesOption(Map.Entry<String, PointDataArray> entry, SeriesOptions seriesOptions)
    {
        seriesOptions.setId(entry.getKey());
        seriesOptions.setName(entry.getKey());
        seriesOptions.setDataArray(entry.getValue());
        seriesOptions.setColor(entry.getValue().getDataPointsColour());
    }

    private List<SeriesOptions> calculatePieSeries()
    {
        List<SeriesOptions> series = new ArrayList<SeriesOptions>();
        Map<String, PointDataArray> dataArray = getModelData().getSeriesData();
        PieChartOptions seriesOptions = new PieChartOptions();
        seriesOptions.setShowInLegend(true);
        seriesOptions.setId(getModelData().getChartTitle());
        seriesOptions.setName(getModelData().getChartTitle());
        seriesOptions.setDataArray(dataArray.get(getModelData().getChartTitle()));
        series.add(seriesOptions);
        return series;
    }

    /*
     * This is commented out because it's using an non-standard data-structure to generate CSV data
     * Any one who wants to use this, implement using Map<String, PointDataArray> to represent series data.
     */
    public String toCSV()
    {
        return "";
    }
}
