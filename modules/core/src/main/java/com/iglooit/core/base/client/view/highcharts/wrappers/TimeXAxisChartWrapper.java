package com.iglooit.core.base.client.view.highcharts.wrappers;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Tuple2;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.model.TimeXAxisChartModelData;
import com.clarity.core.base.client.view.highcharts.charts.PieChartOptions;
import com.clarity.core.base.client.view.highcharts.charts.SeriesOptions;
import com.clarity.core.base.client.view.highcharts.config.AxisLabel;
import com.clarity.core.base.client.view.highcharts.config.CSSObject;
import com.clarity.core.base.client.view.highcharts.config.ChartLegendOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartOptions;
import com.clarity.core.base.client.view.highcharts.config.ChartTitleOptions;
import com.clarity.core.base.client.view.highcharts.config.MarkerOptions;
import com.clarity.core.base.client.view.highcharts.config.PointOptions;
import com.clarity.core.base.client.view.highcharts.config.TooltipOptions;
import com.clarity.core.base.client.view.highcharts.config.XAxisOptions;
import com.clarity.core.base.client.view.highcharts.config.YAxisOptions;
import com.clarity.core.base.client.view.highcharts.data.PointDataArray;
import com.clarity.core.base.client.view.highcharts.helper.ChartColorHelper;
import com.clarity.core.base.client.view.highcharts.helper.HighChartUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeXAxisChartWrapper extends ChartWrapper<TimeXAxisChartModelData>
{
    public TimeXAxisChartWrapper(TimeXAxisChartModelData modelData)
    {
        super(modelData);
    }

    @Override
    protected void initialiseChart()
    {
        super.initialiseChart();
        if (!StringUtil.isBlank(getModelData().getY1AxisTitle()))
        {
            getChart().getChartOptions().setMarginRight(60);
            YAxisOptions y1 = createYAxisOptions(getModelData().getY1AxisTitle());
            y1.setOpposite(true);
            y1.setId("Y1_AXIS");
            getChart().sety1AxisOptions(y1);
        }
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
        ChartTitleOptions xAxisTitleOption = new ChartTitleOptions(false);
        xAxisTitleOption.setText(xAxisTitle);

        AxisLabel xLabel = new AxisLabel(false);
        xLabel.setAlign("right");
        xLabel.setDoubleRotation(290D);
        xLabel.setX(5);

        XAxisOptions xAxis = new XAxisOptions();
        xAxis.setType("datetime");
        xAxis.setLabels(xLabel);
        xAxis.setTitle(xAxisTitleOption);
        xAxis.setStartOnTick(false);
        xAxis.setEndOnTick(false);
        xAxis.setShowFirstLabel(true);
        xAxis.setShowLastLabel(true);
        xAxis.setGridLineWidth(1);
        return xAxis;
    }

    @Override
    protected YAxisOptions createYAxisOptions(String yAxisTitle)
    {
        YAxisOptions yAxis = new YAxisOptions();
        yAxis.setStartOnTick(true);
        yAxis.setEndOnTick(true);
        yAxis.setShowFirstLabel(true);
        yAxis.setShowLastLabel(true);
        yAxis.setGridLineWidth(1);
        ChartTitleOptions yChartTitle = new ChartTitleOptions(false);
        yChartTitle.setText(StringUtil.isBlank(yAxisTitle) ? HighChartUtil.UNICODE_BLANK : yAxisTitle);
        yChartTitle.setAlign("middle");
        yAxis.setTitle(yChartTitle);
        return yAxis;
    }

    @Override
    protected ChartLegendOptions createChartLegendOptions()
    {
        ChartLegendOptions legend = new ChartLegendOptions(false);
        legend.setEnabled(true);
        legend.setAlign(getModelData().getLegendHAlign());
        legend.setVerticalAlign(getModelData().getLegendVAlign());
        legend.setFloating(true);
        legend.setY(12);
        legend.setLayout("horizontal");
        CSSObject legendStyle = new CSSObject();
        legendStyle.set("fontSize", "10px");
        legend.setItemStyle(legendStyle);
        return legend;
    }

    @Override
    protected ChartOptions createChartOptions()
    {
        ChartOptions chartOptions = new ChartOptions(true);
        chartOptions.setMarginRight(30);
        chartOptions.setMarginTop(50);
        if (!StringUtil.isBlank(getModelData().getZoomType())) chartOptions.setZoomType(getModelData().getZoomType());

        chartOptions.remove(ChartOptions.MARGINLEFT);
        return chartOptions;
    }

    @Override
    protected ChartTitleOptions createChartTitleOptions()
    {
        if (StringUtil.isBlank(getModelData().getChartTitle()))
            return null;

        ChartTitleOptions chartTitleOptions = new ChartTitleOptions();
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
        return chartSubTitleOptions;
    }

    @Override
    protected List<SeriesOptions> calculateSeries()
    {
        List<String> colorsInUse = new ArrayList<String>();
        //first string is name, second string is color, Double is value
        List<PointOptions> pieChartData = null;
        if (getModelData().isShowPieChart())
            pieChartData = new ArrayList<PointOptions>();

        Map<String, Tuple2<String, Integer>> unitRounding = getModelData().getUnitAndRounding();
        String yAxis = getModelData().getYAxisTitle();

        List<SeriesOptions> series = new ArrayList<SeriesOptions>();
        for (Map.Entry<String, PointDataArray> entry : getModelData().getSeriesData().entrySet())
        {
            SeriesOptions seriesOptions = createSeriesOptions();
            seriesOptions.setId(entry.getKey());
            seriesOptions.setName(entry.getKey());
            seriesOptions.setDataArray(entry.getValue());
            if (unitRounding.containsKey(entry.getKey())
                    && !StringUtil.stringsEqualIgnoreCaseOrBothNull(yAxis, unitRounding.get(entry.getKey()).getFirst()))
            {
                seriesOptions.set(SeriesOptions.YAXIS, StringUtil.isBlank(getModelData().getY1AxisTitle()) ? 0 : 1);
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.setSymbol(MarkerOptions.MarkerSymbol.CIRCLE);
            markerOptions.setRadius(4);
            seriesOptions.setMarker(markerOptions);

            // deal with color
            String color = "";
            Map<String, String> colorMapping = getModelData().getColorStaticMapping();
            if (colorMapping != null && colorMapping.containsKey(entry.getKey()))
                color = colorMapping.get(entry.getKey());
            else
                color = ChartColorHelper.getNextAvailable(colorsInUse, getModelData().getColorRestrictionType());
            seriesOptions.setColor(color);
            colorsInUse.add(color);
            series.add(seriesOptions);

            if (getModelData().isShowPieChart())
            {
                PointOptions p = new PointOptions();
                p.setName(entry.getKey());
                p.setColor(color);
                p.setY(getSumOfAllPointValue(entry.getValue()));

                pieChartData.add(p);
            }
        }

        if (pieChartData != null)
        {
            PieChartOptions pieChartSeriesOptions = createPieChartOptions(pieChartData);
            series.add(pieChartSeriesOptions);
        }

        return series;
    }

    private double getSumOfAllPointValue(PointDataArray pointArray)
    {
        double sum = 0;
        for (PointOptions point : pointArray.getDataPointObjects())
        {
            sum += point.getY();
        }
        return sum;
    }

    public String toCSV(String firstColumnHeading, boolean needHeader)
    {
        Map<String, PointDataArray> seriesMap = getModelData().getSeriesData();
        if (seriesMap == null || seriesMap.isEmpty())
            return "";

        List<String> seriesNameList = new ArrayList<String>(seriesMap.keySet());

        // series is the header
        StringBuilder header = new StringBuilder();
        // order ascending based on date
        StringBuilder content = new StringBuilder();
        header.append("\"").append(firstColumnHeading).append("\",")
                .append("\"timestamp\"");

        for (String seriesName : seriesNameList)
        {
            if (header.length() != 0)
                header.append(",");

            header.append("\"").append(seriesName).append("\"");
            String calculationContextName = seriesMap.get(seriesName).getDataPointObjects().get(0)
                    .get(TimeXAxisChartModelData.POINT_CALCULATION_CONTEXT_NAME_ATTR);
            if (calculationContextName != null)
                header.append(",\"").append(seriesName).append("(").append(calculationContextName).append(")\"");
        }

        List<PointOptions> s1 = seriesMap.get(seriesNameList.get(0)).getDataPointObjects();
        boolean isTimetampColumn;
        for (int i = 0; i < s1.size(); ++i)
        {
            isTimetampColumn = true;
            for (String seriesName : seriesNameList)
            {
                List<PointOptions> pts = seriesMap.get(seriesName).getDataPointObjects();
                if (pts.isEmpty() || i >= pts.size())
                    continue;

                PointOptions p = pts.get(i);
                if (isTimetampColumn)
                {
                    isTimetampColumn = false;
                    content.append("\"").append(getModelData().getChartTitle()).append("\"");
                    content.append(",\"").append(p.getXAsDate()).append("\",");
                }
                else
                {
                    content.append(",");
                }
                content.append("\"").append(p.getY()).append("\"");
                if (p.get(TimeXAxisChartModelData.POINT_CALCULATION_CONTEXT_NAME_ATTR) != null)
                    content.append(",\"").append(p.get(TimeXAxisChartModelData.POINT_CALCULATION_CONTEXT_VALUE_ATTR))
                            .append("\"");
            }
            //do not add "\n" to the last line
            if (i < (s1.size() - 1))
                content.append("\n");
        }

        if (needHeader)
            return header.toString() + "\n" + content.toString();
        else
            return content.toString();
    }

    @Override
    protected TooltipOptions createToolTips()
    {
        TooltipOptions tooltips = new TooltipOptions();
        tooltips.set(TooltipOptions.BORDERCOLOR, "rgba(62, 69, 79, 0.5)");
        tooltips.set(TooltipOptions.SHARED, getModelData().getShared());
        return tooltips;
    }
}
