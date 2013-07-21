package com.iglooit.core.base.client.view.highcharts.charts;

import com.google.gwt.core.client.JavaScriptObject;

public class LineChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";
    public static final String STEP = "step";

    public LineChartOptions()
    {
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set("type", "line");
        set(STEP, Boolean.FALSE);
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public void setStep(boolean isStep)
    {
        set(STEP, isStep);
    }

    public boolean getStep()
    {
        return (Boolean)get(STEP);
    }

}
