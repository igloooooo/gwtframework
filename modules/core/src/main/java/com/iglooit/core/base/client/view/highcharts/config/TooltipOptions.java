package com.iglooit.core.base.client.view.highcharts.config;

import com.clarity.core.base.client.view.highcharts.ChartConfig;
import com.google.gwt.core.client.JavaScriptObject;

public class TooltipOptions extends ChartConfig
{
    public static final String BACKGROUNDCOLOR = "backgroundColor"; //"rgba(255, 255, 255, 0.9)",
    public static final String BORDERCOLOR = "borderColor"; //"auto",
    public static final String BORDERRADIUS = "borderRadius"; //5,
    public static final String BORDERWIDTH = "borderWidth"; //2,
    public static final String CROSSHAIRS = "crosshairs"; //null,
    public static final String ENABLED = "enabled"; //true,
    public static final String FORMATTER = "formatter"; //,
    public static final String SHADOW = "shadow"; //true,
    public static final String SHARED = "shared"; //false,
    public static final String SNAP = "snap"; //10
    public static final String STYLE = "style"; // NULL
    public static final String VALUESUFFIX = "valueSuffix";
    public static final String POINTFORMAT = "pointFormat";

    public TooltipOptions()
    {
        set(BACKGROUNDCOLOR, "rgba(255,255,255,0.9)");
        set(BORDERCOLOR, "auto");
        set(BORDERRADIUS, 5);
        set(BORDERWIDTH, 2);
        set(ENABLED, Boolean.TRUE);
        set(SHADOW, Boolean.TRUE);
        set(SHARED, Boolean.FALSE);
        set(SNAP, 10); // ASSURE-152, 10 for mouse-powered devices and 25 for touch devices
    }

    @Override
    public void initialise(JavaScriptObject obj)
    {

    }

    public void setFormatter(JavaScriptObject formatter)
    {
        set(FORMATTER, formatter);
    }

    public JavaScriptObject getFormatter()
    {
        return get(FORMATTER);
    }

    public void setValuesuffix(String suffix)
    {
        set(VALUESUFFIX, suffix);
    }
    public void setPointformat(String format)
    {
        set(POINTFORMAT, format);
    }

}
