package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class DialOptions extends ChartConfig
{

    public static final String BACKGROUNDCOLOR = "backgroundColor";
    public static final String BASELENGTH = "baseLength";
    public static final String BASEWIDTH = "baseWidth";
    public static final String BORDERCOLOR = "borderColor";
    public static final String BORDERWIDTH = "borderWidth";
    public static final String RADIUS = "radius";
    public static final String REARLENGTH = "rearLength";
    public static final String TOPWIDTH = "topWidth";


    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public String getBackgroundColor()
    {
        return get(BACKGROUNDCOLOR);
    }

    public DialOptions setBackgroundcolor(String data)
    {
        set(BACKGROUNDCOLOR, data);
        return this;
    }
    public DialOptions setBaselength(String percentage)
    {
        set(BASELENGTH, percentage);
        return this;
    }
    public DialOptions setBasewidth(Double data)
    {
        set(BASEWIDTH, data);
        return this;
    }
    public DialOptions setBordercolor(String color)
    {
        set(BORDERCOLOR, color);
        return this;
    }
    public DialOptions setBorderwidth(Double width)
    {
        set(BORDERWIDTH, width);
        return this;
    }
    public DialOptions setRadius(String percentage)
    {
        set(RADIUS, percentage);
        return this;
    }
    public DialOptions setRearlength(String percentage)
    {
        set(REARLENGTH, percentage);
        return this;
    }
    public DialOptions setTopwidth(Double width)
    {
        set(TOPWIDTH, width);
        return this;
    }

}
