package com.iglooit.core.base.client.view.highcharts.charts;

import com.clarity.core.base.client.view.highcharts.config.MarkerOptions;
import com.google.gwt.core.client.JavaScriptObject;

public class AreaChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";
    public static final String FILLCOLOR = "fillColor";
    public static final String FILLOPACITY = "fillOpacity";
    public static final String LINECOLOR = "lineColor";
    public static final String THRESHOLD = "threshold";
    public static final String MARKER = "marker";

    public AreaChartOptions()
    {
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set(FILLOPACITY, .75);
        set(THRESHOLD, 0);
        set("type", "area");
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public String getFillColor()
    {
        return get(FILLCOLOR);
    }
    public void setFillColor(String data)
    {
        set(FILLCOLOR, data);
    }
    public double getFillOpacity()
    {
        return (Double)get(FILLOPACITY);
    }
    public void setFillOpacity(double data)
    {
        set(FILLOPACITY, data);
    }
    public String getLineColor()
    {
        return get(LINECOLOR);
    }
    public void setLineColor(String data)
    {
        set(LINECOLOR, data);
    }
    public int getThreshold()
    {
        return (Integer)get(THRESHOLD);
    }
    public void setThreshold(int data)
    {
        set(THRESHOLD, data);
    }

    public MarkerOptions getMarker()
    {
        return (MarkerOptions)get(MARKER);
    }

    public void setMarker(MarkerOptions data)
    {
        set(MARKER, data);
    }
}
