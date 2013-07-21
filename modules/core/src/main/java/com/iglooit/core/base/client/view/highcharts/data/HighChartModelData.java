package com.iglooit.core.base.client.view.highcharts.data;

import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.client.view.highcharts.config.HighchartType;
import com.clarity.core.base.client.view.highcharts.helper.ChartColorHelper;
import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference class PerfTimeXAxisChartModelData
 */
public class HighChartModelData extends BaseModelData
{
    private Boolean timedCharted;

    public static final String GROUPING_TITLE = "grouping_title";
    public static final String CHART_TYPE = "chart_type";
    public static final String UNIT_AND_ROUNDING = "unit_and_rounding";
    public static final String X_AXIS_TITLE = "x_axis_title";
    public static final String Y_AXIS_TITLE = "y_axis_title";
    public static final String Y1_AXIS_TITLE = "y1_axis_title";
    public static final String DATA_AGGREGATION_INTERVAL = "data_aggregation_interval";
    public static final String CHART_HEIGHT = "chart_height";
    public static final String CHART_WIDTH = "chart_width";
    public static final String CHART_TITLE = "chart_title";
    public static final String CHART_SUB_TITLE = "chart_sub_title";
    public static final String LEGEND_VALIGN = "chart_legend_valign";
    public static final String LEGEND_HALIGN = "chart_legend_halign";
    public static final String SERIES_DATA = "series_data";
    public static final String SHOW_PIE_CHART = "show_pie_chart";
    public static final String STACKING = "stacking";
    public static final String SHARED = "shared";
    public static final String ZOOM_TYPE = "zoomType";
    public static final String X_CATEGORIES = "x_categories";
    public static final String COLOR_RESTRICTION_TYPE = "colorRestrictionType";
    public static final String COLOR_STATIC_MAPPING = "colorStaticMapping";

    public HighChartModelData()
    {
        this(Boolean.FALSE);
    }

    public HighChartModelData(Boolean timedCharted)
    {
        this.timedCharted = timedCharted;
        //set default options
        set(UNIT_AND_ROUNDING, new HashMap<String, Tuple2<String, Integer>>());
        set(X_AXIS_TITLE, "");
        set(Y_AXIS_TITLE, "");
        set(Y1_AXIS_TITLE, "");
        //set(DATA_AGGREGATION_INTERVAL, TemplateConstants.AggregateTimeInterval.QUARTERHOURLY);
        if (timedCharted)
            set(CHART_TYPE, HighchartType.LINE);
        else
            set(CHART_TYPE, HighchartType.BAR);

        set(CHART_HEIGHT, 500);
        set(CHART_WIDTH, 1000);
        set(SHARED, Boolean.FALSE);

        set(GROUPING_TITLE, "");
        set(CHART_TITLE, "");
        set(CHART_SUB_TITLE, "");
        setLegendPosition("top", "center");
        set(SHOW_PIE_CHART, Boolean.FALSE);
        set(STACKING, Boolean.FALSE);

        set(COLOR_RESTRICTION_TYPE, ChartColorHelper.ColorRestrictionType.SEVERITY_SAFE);
    }

    public void setColorStaticMapping(Map<String, String> colorMapping)
    {
        set(COLOR_STATIC_MAPPING, colorMapping);
    }

    public Map<String, String> getColorStaticMapping()
    {
        return get(COLOR_STATIC_MAPPING);
    }

    public void setColorRestrictionType(ChartColorHelper.ColorRestrictionType colorRestrictionType)
    {
        set(COLOR_RESTRICTION_TYPE, colorRestrictionType);
    }

    public ChartColorHelper.ColorRestrictionType getColorRestrictionType()
    {
        return get(COLOR_RESTRICTION_TYPE);
    }

    // x, y, xy see http://api.highcharts.com/highcharts#chart.zoomType
    public void setZoomType(String zoomType)
    {
        set(ZOOM_TYPE, zoomType);
    }

    public String getZoomType()
    {
        return get(ZOOM_TYPE);
    }

    public void setShared(Boolean shared)
    {
        set(SHARED, shared);
    }

    public Boolean getShared()
    {
        return get(SHARED);
    }

    /**
     * Warn: should use <code>dataPointObjects</code>
     * in {@link PointDataArray} to hold the series data.
     *
     * @param series series name to series data array map
     */
    public void setSeriesData(final Map<String, PointDataArray> series)
    {
        if (timedCharted)
            throw new UnsupportedOperationException();
        set(SERIES_DATA, series);
        //validate the series data
        for (PointDataArray points : series.values())
            if (points.getDataPointObjects() == null || points.getDataPointObjects().isEmpty())
                throw new IllegalArgumentException("Should use dataPointObjects in PointDataArray!");
    }

    public Map<String, PointDataArray> getSeriesData()
    {
        if (timedCharted)
            throw new UnsupportedOperationException();
        return get(SERIES_DATA);
    }

    public void setUnitAndRounding(Map<String, Tuple2<String, Integer>> unit)
    {
        set(UNIT_AND_ROUNDING, unit);
    }

    public Map<String, Tuple2<String, Integer>> getUnitAndRounding()
    {
        return get(UNIT_AND_ROUNDING);
    }

    public void setXAxisTitle(final String title)
    {
        set(X_AXIS_TITLE, title);
    }

    public String getXAxisTitle()
    {
        return get(X_AXIS_TITLE);
    }

    public void setYAxisTitle(final String title)
    {
        set(Y_AXIS_TITLE, title);
    }

    public String getYAxisTitle()
    {
        return get(Y_AXIS_TITLE);
    }

    public void setY1AxisTitle(final String title)
    {
        set(Y1_AXIS_TITLE, title);
    }

    public String getY1AxisTitle()
    {
        return get(Y1_AXIS_TITLE);
    }

    public void setChartType(HighchartType chartType)
    {
        set(CHART_TYPE, chartType);
    }

    public HighchartType getChartType()
    {
        return get(CHART_TYPE);
    }

    public void setChartHeight(Integer height)
    {
        set(CHART_HEIGHT, height);
    }

    public Integer getChartHeight()
    {
        return get(CHART_HEIGHT);
    }

    public void setChartWidth(Integer width)
    {
        set(CHART_WIDTH, width);
    }

    public Integer getChartWidth()
    {
        return get(CHART_WIDTH);
    }

    public void setChartTitle(String title)
    {
        set(CHART_TITLE, title);
    }

    public String getChartTitle()
    {
        return get(CHART_TITLE);
    }

    // a grouping title is the grouping key given to a particular item in the graph
    // it is used when the series name does not give us a result for the unitRounding thing
    // i separated from the title so that in the future if we decide to change
    // the format of the chart title it won't break our other logic
    public void setGroupingTitle(String title)
    {
        set(GROUPING_TITLE, title);
    }

    public String getGroupingTitle()
    {
        return get(GROUPING_TITLE);
    }

    public void setChartSubTitle(String subTitle)
    {
        set(CHART_SUB_TITLE, subTitle);
    }

    public String getChartSubTitle()
    {
        return get(CHART_SUB_TITLE);
    }

    public void setLegendPosition(String vAlign, String hAlign)
    {
        set(LEGEND_HALIGN, hAlign);
        set(LEGEND_VALIGN, vAlign);
    }

    public String getLegendVAlign()
    {
        return get(LEGEND_VALIGN);
    }

    public String getLegendHAlign()
    {
        return get(LEGEND_HALIGN);
    }

    public void setShowPieChart(Boolean show)
    {
        set(SHOW_PIE_CHART, show);
    }

    public Boolean isShowPieChart()
    {
        return get(SHOW_PIE_CHART);
    }

    public void setXCategories(List<String> categories)
    {
        set(X_CATEGORIES, categories);
    }

    public List<String> getXCategories()
    {
        return get(X_CATEGORIES);
    }

    /**
     * @param stacking Sets the stacking option to "normal" in the series when true.
     */
    public void setStacking(Boolean stacking)
    {
        set(STACKING, stacking);
    }

    public Boolean isStacking()
    {
        return get(STACKING);
    }

}
