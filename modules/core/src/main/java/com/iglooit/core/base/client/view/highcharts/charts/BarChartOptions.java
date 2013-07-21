package com.iglooit.core.base.client.view.highcharts.charts;

import com.google.gwt.core.client.JavaScriptObject;

public class BarChartOptions extends CommonSeriesOptions
{
    public static final String ALLOWPOINTSELECT = "allowPointSelect";
    public static final String BORDERCOLOR = "borderColor";
    public static final String BORDERRADIUS = "borderRadius";
    public static final String BORDERWIDTH = "borderWidth";
    public static final String COLORBYPOINT = "colorByPoint";
    public static final String GROUPPADDING = "groupPadding";
    public static final String MINPOINTLENGTH = "minPointLength";
    public static final String POINTPADDING = "pointPadding";
    public static final String POINTWIDTH = "pointWidth";

    public BarChartOptions()
    {
        set(ALLOWPOINTSELECT, Boolean.FALSE);
        set(BORDERCOLOR, "#FFFFFF");
        set(BORDERRADIUS, 0);
        set(BORDERWIDTH, 1);
        set(COLORBYPOINT, Boolean.FALSE);
        set(GROUPPADDING, 0.2);
        set(MINPOINTLENGTH, 0);
        set(POINTPADDING, 0.1);
        set(POINTWIDTH, null);
        set("type", "bar");
        
    }
    @Override
    public void initialise(JavaScriptObject obj)
    {

    }
    public String getBorderColor()
    {
        return get(BORDERCOLOR);
    }
    public void setBorderColor(String data)
    {
        set(BORDERCOLOR, data);
    }
    public int getBorderRadius()
    {
        return (Integer)get(BORDERRADIUS);
    }
    public void setBorderRadius(int data)
    {
        set(BORDERRADIUS, data);
    }
    public int getBorderWidth()
    {
        return (Integer)get(BORDERWIDTH);
    }
    public void setBorderWidth(int data)
    {
        set(BORDERWIDTH, data);
    }
    public boolean getColorByPoint()
    {
        return (Boolean)get(COLORBYPOINT);
    }
    public void setColorByPoint(boolean data)
    {
        set(COLORBYPOINT, data);
    }
    public double getGroupPadding()
    {
        return (Double)get(GROUPPADDING);
    }
    public void setGroupPadding(double data)
    {
        set(GROUPPADDING, data);
    }
    public int getMinPointLength()
    {
        return (Integer)get(MINPOINTLENGTH);
    }
    public void setMinPointLength(int data)
    {
        set(MINPOINTLENGTH, data);
    }
    public double getPointPadding()
    {
        return (Double)get(POINTPADDING);
    }
    public void setPointPadding(double data)
    {
        set(POINTPADDING, data);
    }
    public int getPointWidth()
    {
        return (Integer)get(POINTWIDTH);
    }
    public void setPointWidth(int data)
    {
        set(POINTWIDTH, data);
    }
}
