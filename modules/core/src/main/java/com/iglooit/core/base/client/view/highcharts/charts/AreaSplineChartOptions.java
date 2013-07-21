package com.iglooit.core.base.client.view.highcharts.charts;

public class AreaSplineChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";

    public AreaSplineChartOptions()
    {
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set("type", "areaspline");
    }
}
