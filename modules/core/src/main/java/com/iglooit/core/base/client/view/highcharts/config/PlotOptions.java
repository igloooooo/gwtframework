package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.clarity.core.base.client.view.highcharts.charts.SeriesOptions;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Default plot options for each series.
 * We can define a single configuration for each series type
 * and then reference that type when we add a 'SERIES' object
 * without having to re-create the configuration
 */
public class PlotOptions extends ChartConfig
{
    public static final String AREA = "area";
    public static final String AREASPLINE = "areaspline";
    public static final String BAR = "bar";
    public static final String COLUMN = "column";
    public static final String LINE = "line";
    public static final String PIE = "pie";
    public static final String SERIES = "series";
    public static final String SCATTER = "scatter";
    public static final String SPLINE = "spline";

    private Map<String, SeriesOptions> seriesMap = new HashMap<String, SeriesOptions>();

    public PlotOptions()
    {
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public void setSeries(String graphType, SeriesOptions option)
    {
        set(graphType, Util.getJsObject(option, 10));
        seriesMap.put(graphType, option);
    }

    public SeriesOptions getSeries(String graphType)
    {
        if (!seriesMap.containsKey(graphType))
            return null;

        return seriesMap.get(graphType);
    }
}
