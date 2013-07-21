package com.iglooit.core.base.client.view.highcharts.charts;

public class SplineChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";

    public SplineChartOptions()
    {
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set("type", "spline");
    }


}
