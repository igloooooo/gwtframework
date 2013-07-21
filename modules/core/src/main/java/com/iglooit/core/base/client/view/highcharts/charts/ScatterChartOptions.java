package com.iglooit.core.base.client.view.highcharts.charts;

public class ScatterChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";

    public ScatterChartOptions()
    {
        set("type", "scatter");
        set(ALLOWPOINTSELECT, Boolean.FALSE);
    }

}
