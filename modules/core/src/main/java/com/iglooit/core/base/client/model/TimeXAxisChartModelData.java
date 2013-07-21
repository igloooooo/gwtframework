package com.iglooit.core.base.client.model;

import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.client.view.highcharts.config.HighchartType;
import com.clarity.core.base.client.view.highcharts.helper.ChartColorHelper;

import java.util.HashMap;
import java.util.Map;

public class TimeXAxisChartModelData extends ChartModelData
{
    public static final String GROUPING_TITLE = "grouping_title";
    public static final String UNIT_AND_ROUNDING = "unit_and_rounding";
    public static final String Y1_AXIS_TITLE = "y1_axis_title";
    public static final String DATA_AGGREGATION_INTERVAL = "data_aggregation_interval";
    public static final String LEGEND_VALIGN = "chart_legend_valign";
    public static final String LEGEND_HALIGN = "chart_legend_halign";

    public static final String ZOOM_TYPE = "zoomType";
    public static final String COLOR_RESTRICTION_TYPE = "colorRestrictionType";
    public static final String COLOR_STATIC_MAPPING = "colorStaticMapping";

    public static final String POINT_CALCULATION_CONTEXT_NAME_ATTR = "calcContextName";
    public static final String POINT_CALCULATION_CONTEXT_VALUE_ATTR = "calcContextValue";

    public TimeXAxisChartModelData()
    {
        // set default options
        // please set the data aggregation interval
        set(UNIT_AND_ROUNDING, new HashMap<String, Tuple2<String, Integer>>());
        set(X_AXIS_TITLE, "");
        set(Y_AXIS_TITLE, "");
        set(Y1_AXIS_TITLE, "");
        set(CHART_TYPE, HighchartType.LINE);

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

    public void setUnitAndRounding(Map<String, Tuple2<String, Integer>> unit)
    {
        set(UNIT_AND_ROUNDING, unit);
    }

    public Map<String, Tuple2<String, Integer>> getUnitAndRounding()
    {
        return get(UNIT_AND_ROUNDING);
    }

    public void setY1AxisTitle(final String title)
    {
        set(Y1_AXIS_TITLE, title);
    }

    public String getY1AxisTitle()
    {
        return get(Y1_AXIS_TITLE);
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
}
