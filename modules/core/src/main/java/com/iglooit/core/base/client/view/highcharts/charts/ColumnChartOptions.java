package com.iglooit.core.base.client.view.highcharts.charts;

public class ColumnChartOptions extends BarChartOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";

    public ColumnChartOptions()
    {
        super();
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set("type", "column");
    }

}
