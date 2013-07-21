package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class PivotOptions extends ChartConfig
{
    public static final String RADIUS = "radius";
    public static final String BACKGROUNDCOLOR = "backgroundColor";
    public static final String BORDERCOLOR = "borderColor";
    public static final String BORDERWIDTH = "borderWidth";

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public PivotOptions setRadius(Double radius)
    {
        set(RADIUS, radius);
        return this;
    }
    public PivotOptions setBackgroundcolor(GradientColor color)
    {
        set(BACKGROUNDCOLOR, color);
        return this;
    }

    public PivotOptions setBordercolor(String color)
    {
        set(BORDERCOLOR, color);
        return this;
    }

    public PivotOptions setBorderwidth(Double width)
    {
        set(BORDERWIDTH, width);
        return this;
    }
}

