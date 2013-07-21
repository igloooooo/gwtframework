package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class BackgroundOptions extends ChartConfig
{

    public static final String BACKGROUNDCOLOR = "backgroundColor";
    public static final String BORDERWIDTH = "borderWidth";
    public static final String BORDERCOLOR = "borderColor";
    public static final String OUTERRADIUS = "outerRadius";

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public BackgroundOptions setBackgroundcolor(GradientColor color)
    {
//        set(BACKGROUNDCOLOR, Util.getJsObject(color));
        set(BACKGROUNDCOLOR, color);
        return this;
    }
    public BackgroundOptions setBorderwidth(Integer width)
    {
        set(BORDERWIDTH, width);
        return this;
    }

    public BackgroundOptions setBordercolor(String color)
    {
        set(BORDERCOLOR, color);
        return this;
    }
    public BackgroundOptions setOuterradius(String percentage)
    {
        set(OUTERRADIUS, percentage);
        return this;
    }

}
